package nz.co.greenjersey.inventorytracker.MenuItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import nz.co.greenjersey.inventorytracker.Objects.Bike;
import nz.co.greenjersey.inventorytracker.R;

public class BatchCheckIn extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;
    ArrayList<Long> idNumbers;
    ArrayList<Bike> bikes;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_check_in);
        Intent intent = getIntent();
        this.location = intent.getExtras().getString("location");
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        idNumbers = new ArrayList<>();
        bikes = new ArrayList<>();
        if(mNfcAdapter == null){
            Toast.makeText(this, "This device does not support nfc", Toast.LENGTH_LONG).show();
            finish();
        }
        if(!mNfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC is turned off", Toast.LENGTH_LONG).show();
            finish();
        }



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
            if(!idNumbers.contains(String.valueOf(idNumber))) {
                String idNum = String.valueOf(idNumber);
                idNumbers.add(idNumber);
                LinearLayout layout = (LinearLayout) this.findViewById(R.id.buttonLayout);
                layout.setOrientation(LinearLayout.VERTICAL);
                Button button = new Button(this);
                button.setText(String.valueOf(idNumber));
                button.setId(Math.abs((int) idNumber));
                layout.addView(button);
                //button is now on screen

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference().child("Green Jersey Bikes");
                myRef.child(String.valueOf(idNumber)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null) {
                            Bike b = dataSnapshot.getValue(Bike.class);
                            bikes.add(b);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

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
        idNumbers.remove(String.valueOf(toRemove));
        for(Iterator<Bike> it = bikes.iterator(); it.hasNext();){
            if(it.next().getTagNumber().equals(String.valueOf(toRemove))){
                it.remove();
            }
        }

    }


    public void onOkButtonPress(View view){

        //we dont need to deal with bikes that are already back
        for(Iterator<Bike> it = bikes.iterator(); it.hasNext();){
            if(!it.next().isInUse()){
                it.remove();
                Toast.makeText(getApplicationContext(), "One or more of the bikes checked in were already checked in, you do not need to do anything about this", Toast.LENGTH_LONG).show();
            }
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        for(Long id : idNumbers){
            DatabaseReference ref = database.getReference().child("Green Jersey Bikes").child(String.valueOf(id)).child("inUse");
            ref.setValue(false);

            DatabaseReference ref3 = database.getReference().child("Green Jersey Bikes").child(String.valueOf(id)).child("currentOrder");
            ref3.setValue("");
            DatabaseReference ref4 = database.getReference().child("Green Jersey Bikes").child(String.valueOf(id)).child("location");
            ref4.setValue(location);

        }



        finish();

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
