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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import nz.co.greenjersey.inventorytracker.Objects.Bike;
import nz.co.greenjersey.inventorytracker.Objects.BikeDescription;
import nz.co.greenjersey.inventorytracker.R;



public class AddBike extends AppCompatActivity {
    ArrayList<BikeDescription> bikeTypes = new ArrayList<>();
    NfcAdapter mNfcAdapter;
    Long idNumber = 0l;
    boolean isScanned;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        isScanned = false;
        Intent intent = getIntent();
        this.location = intent.getExtras().getString("location");
        String idNum;

        if(mNfcAdapter == null){
            Toast.makeText(this, "This device does not support nfc", Toast.LENGTH_LONG).show();
            finish();
        }
        if(!mNfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC is turned off", Toast.LENGTH_LONG).show();
            finish();
        }
        // connect to database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("Bike Descriptions");
        //grab list of types of bike we have stroed in database already
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for(DataSnapshot dss : dataSnapshot.getChildren()){
                   BikeDescription bikeDescription = dss.getValue(BikeDescription.class);
                   bikeTypes.add(bikeDescription);
               }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    }



    private void handleIntent(Intent intent){
        String action = intent.getAction();
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] idByteArray = tag.getId();
            for (int i = 0; i < idByteArray.length; i++)
            {
                idNumber += ((long) idByteArray[i] & 0xffL) << (8 * i);
            }
            final long toRemove = idNumber; // so we can remove it from an inner class if need be
            if(!isScanned) {
                LinearLayout layout = (LinearLayout) this.findViewById(R.id.addBikeLayout);
                layout.setOrientation(LinearLayout.VERTICAL);
                Button button = new Button(this);
                button.setText(String.valueOf(idNumber));
                layout.addView(button);
                final RadioGroup rg = buildRadioList(layout);
                final EditText serialNumber = new EditText(this);
                serialNumber.setHint("Enter Bike Serial Number");
                layout.addView(serialNumber);
                final EditText gjStickNo = new EditText(this);
                gjStickNo.setHint("Enter sticker number");
                layout.addView(gjStickNo);

                Button okButton = new Button(this);
                okButton.setText("Add Bike");
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //String idnumString = String.valueOf(idNumber);
                        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                        Date date = new Date();
                        String serialNum = serialNumber.getText().toString();
                        String gjStick = gjStickNo.getText().toString();
                        int id = rg.getCheckedRadioButtonId();
                        String color = "";
                        String brand = "";
                        String desc = "";
                        if(id != -1){
                            RadioButton rb = (RadioButton) findViewById(id);
                            ArrayList<String> all = parseString(rb.getText().toString());
                            brand = all.get(0).trim();
                            desc = all.get(1).trim();
                            color = all.get(2).trim();
                        }


                       if(idNumber != null && date != null && serialNum.length() > 4 && brand != "" && color != "" && desc != "") {
                            Bike bike = new Bike(idNumber, date, serialNum,  brand, color, desc, location, false, 1, "", gjStick, new ArrayList<String>());
                           DatabaseReference myNewRef = database.getReference().child("Green Jersey Bikes").child(String.valueOf(idNumber));
                           myNewRef.setValue(bike);
                           finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Please Ensure you have entered all data correctly", Toast.LENGTH_SHORT).show();
                       }

                    }
                });
                layout.addView(okButton);
            }
            else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("You Have already scanned a bike, would you like to remove it");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                removeBike(); //cant get the layout from here, so have to call a method in containing class
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
            isScanned = true;
        }
    }



    public ArrayList<String> parseString(String toP){
        Scanner sc = new Scanner(toP);
        sc.useDelimiter("  ");
        ArrayList<String> info = new ArrayList<>();
        while(sc.hasNext()){
            info.add(sc.next());
        }
        return info;
    }

    private RadioGroup buildRadioList(LinearLayout layout){
        TextView display = new TextView(this);
        display.setText("Please select a bike description");
        display.setId((int) 4); //magic number
        layout.addView(display);
        ScrollView descriptionMenu = new ScrollView(this);
        RadioGroup radioGroup = new RadioGroup(this);
        int i = 150;
        for(BikeDescription b : bikeTypes){
            RadioButton but = new RadioButton(this);
            but.setId(i);
            but.setText(b.getBrand() + " " + b.getDesc() + " " + b.getColor());
            radioGroup.addView(but);
            i++;
        }
        descriptionMenu.addView(radioGroup);
        layout.addView(descriptionMenu);
        return radioGroup;
    }

    //removes tags and button from ui
    private void removeBike() {
        LinearLayout layout = (LinearLayout) this.findViewById(R.id.addBikeLayout);
        layout.removeAllViews();
        TextView textView = new TextView(this);
        textView.setText("Please scan a bike to add to the database");
        layout.addView(textView);
        isScanned = false;
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
