package nz.co.greenjersey.inventorytracker.AssignmentViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import nz.co.greenjersey.inventorytracker.Objects.Bike;
import nz.co.greenjersey.inventorytracker.Objects.Customer;
import nz.co.greenjersey.inventorytracker.R;

public class CustomerBikeAssign extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    String fName;
    String lName;
    String bookingId;
    String location;

    private ArrayList<String> idNumbers;
    private ArrayList<String> idNumbers2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bike_assign);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        idNumbers = new ArrayList<>();
        idNumbers2 = new ArrayList<>();

        if(mNfcAdapter == null){
            Toast.makeText(this, "This device does not support nfc", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(!mNfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC is turned off", Toast.LENGTH_LONG).show();
            finish();
        }
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        fName = (String) bundle.get("fName");
        lName = (String) bundle.get("lName");
        bookingId = (String) bundle.get("orderNumber");
        location = (String) bundle.get("Location");
        String name = fName + " " + lName;
        TextView nameTextView = (TextView) findViewById(R.id.cusName);
        nameTextView.setText(name + " : " + bookingId);


        handleIntent(getIntent());
    }

    public void onOkButtonPress(View view){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String date = String.valueOf(android.text.format.DateFormat.format("yyy-MM-dd", new java.util.Date()));
        final Customer customer = new Customer(fName, lName, bookingId, idNumbers);
        final DatabaseReference myRef = database.getReference().child("Green Jersey Customers").child(date).child(bookingId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Customer cus = dataSnapshot.getValue(Customer.class);
                if (cus != null) {
                    for(String s: cus.getBikes()){
                        if(!customer.getBikes().contains(s)){
                            customer.addBike(s);
                        }
                    }
                }
                myRef.setValue(customer);

                updateBikes(database, cus);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



    }


    public void updateBikes(final FirebaseDatabase database, final Customer customer){
        Log.d("idlist", this.idNumbers2.toString());
        for(final String id: this.idNumbers2) {
            Log.d("id", id);
                final DatabaseReference ref = database.getReference().child("Green Jersey Bikes").child(id);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Bike b = dataSnapshot.getValue(Bike.class);
                        if (customer != null) {
                            if (!customer.getBikes().contains(id)) {
                                b.setInUse(true);
                                b.setLocation(location);
                                b.setCurrentOrder(bookingId);
                                b.setNumUses(b.getNumUses() + 1);
                                ref.setValue(b);
                            }
                            else{
                                if(!b.isInUse()){
                                    b.setInUse(true);
                                    b.setLocation(location);
                                    b.setCurrentOrder(bookingId);
                                    b.setNumUses(b.getNumUses() + 1);
                                    ref.setValue(b);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "One or more of the bikes assigned have already been assigned to this customer", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        else{
                            b.setInUse(true);
                            b.setLocation(location);
                            b.setCurrentOrder(bookingId);
                            b.setNumUses(b.getNumUses() + 1);
                            ref.setValue(b);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
        this.idNumbers2.clear();
    }

    private void handleIntent(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] idByteArray = tag.getId();
            long idNumber = 0;
            for (int i = 0; i < idByteArray.length; i++)
            {
                idNumber += ((long) idByteArray[i] & 0xffL) << (8 * i);
            }
            final long toRemove = idNumber; // so we can remove it from an inner class if need be
            if(!idNumbers2.contains(idNumber)) {
                String idNum = String.valueOf(idNumber);
                idNumbers.add(idNum);
                idNumbers2.add(idNum); //something about neediung this because other one is final or some such utter bulllshit
                LinearLayout layout = (LinearLayout) this.findViewById(R.id.buttonLayout);
                layout.setOrientation(LinearLayout.VERTICAL);
                Button button = new Button(this);
                button.setText(String.valueOf(idNumber));
                button.setId(Math.abs((int) idNumber));
                layout.addView(button);
                //button is now on screen

            }
            else{//tag is already scanned in, need to offer to remove it
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Bike already scanned, would you like to remove it?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeBike(toRemove); //cant get the layout from here, so have to call a method in containing class
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        }
    }

    //removes tags from tag list and buttons from ui
    private void removeBike(long toRemove) {
        int buttonId = Math.abs((int) toRemove);
        Log.d("button id bot: ", String.valueOf(buttonId));
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.buttonLayout);
        Button butt =(Button) layout.findViewById(buttonId);
        layout.removeView(butt);
        idNumbers.remove(toRemove);

    }





    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);
        adapter.enableForegroundDispatch(activity, pendingIntent, null, null);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter){
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onResume(){
        super.onResume();
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause(){
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }


}
