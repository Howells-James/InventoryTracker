package nz.co.greenjersey.inventorytracker.AssignmentViews;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import androidx.appcompat.app.AppCompatActivity;;
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
import java.util.Iterator;

import nz.co.greenjersey.inventorytracker.Objects.Customer;
import nz.co.greenjersey.inventorytracker.R;

public class CustomerUnAssignBikes extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    String fName;
    String lName;
    String bookingId;
    String location;
    private ArrayList<String> idNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_un_assign_bikes);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        idNumbers = new ArrayList<>();

        if(mNfcAdapter == null){
            Toast.makeText(this, "This device does not support nfc", Toast.LENGTH_LONG).show();
            finish();
        }
        if(!mNfcAdapter.isEnabled()){
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
        nameTextView.setText("Removing Bikes from: " + name + " : " + bookingId);
        handleIntent(getIntent());
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
            if(!idNumbers.contains(idNumber)) {
                String idNum = String.valueOf(idNumber);
                idNumbers.add(idNum);
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

    public void onOkButtonPress(View view){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final String date = String.valueOf(android.text.format.DateFormat.format("yyy-MM-dd", new java.util.Date()));
        final DatabaseReference myRef = database.getReference().child("Green Jersey Customers").child(date).child(bookingId);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("bikes").exists()) {
                    Customer cus = dataSnapshot.getValue(Customer.class);
                    if (cus != null) {
                        if (cus.getBikes() == null) {
                            finish();
                        }
                    } else {
                        finish();
                    }
                    for (String scannedBike : idNumbers) {
                        Iterator<String> iterator = cus.getBikes().iterator();
                        while (iterator.hasNext()) {
                            if (scannedBike.equals(iterator.next())) {
                                iterator.remove();
                                DatabaseReference ref2 = database.getReference().child("Green Jersey Bikes").child(scannedBike).child("inUse");
                                ref2.setValue(false);
                                DatabaseReference ref3 = database.getReference().child("Green Jersey Bikes").child(scannedBike).child("currentOrder");
                                ref3.setValue("");
                            }
                        }
                    }

                    myRef.setValue(cus);


                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "The customer you selected has no bikes assigned to them", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

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
