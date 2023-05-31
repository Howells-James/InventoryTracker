package nz.co.greenjersey.inventorytracker.MenuItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import nz.co.greenjersey.inventorytracker.Objects.Bike;
import nz.co.greenjersey.inventorytracker.Objects.BikeDescription;
import nz.co.greenjersey.inventorytracker.Objects.MaintenanceDesc;
import nz.co.greenjersey.inventorytracker.R;

public class AddMaintenanceNote extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    boolean isScanned;
    public ArrayList<MaintenanceDesc> mainDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_maintenance_note);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if(mNfcAdapter == null){
            Toast.makeText(this, "This device does not support nfc", Toast.LENGTH_LONG).show();
            finish();
        }
        if(!mNfcAdapter.isEnabled()){
            Toast.makeText(this, "NFC is turned off", Toast.LENGTH_LONG).show();
            finish();
        }
        mainDesc = new ArrayList<>();
        isScanned = false;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("MaintenanceDesc");
        //grab list of types of bike we have stroed in database already
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dss : dataSnapshot.getChildren()){
                    MaintenanceDesc m = dss.getValue(MaintenanceDesc.class);
                    mainDesc.add(m);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent){
        String action = intent.getAction();

        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            if (!isScanned) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] idByteArray = tag.getId();
                long idNumber = 0;
                for (int i = 0; i < idByteArray.length; i++) {
                    idNumber += ((long) idByteArray[i] & 0xffL) << (8 * i);
                }
                isScanned = true;
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference ref = database.getReference().child("Green Jersey Bikes").child(String.valueOf(idNumber));
                final long finalIdNumber = idNumber;
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Bike bike = dataSnapshot.getValue(Bike.class);
                        if (bike.getMainDescs() == null) {
                            bike.init();
                        }
                        Log.d("bike", bike.toString());
                        displayBikeInfo(bike, database, finalIdNumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }
    }

    public void displayBikeInfo(final Bike bike, final FirebaseDatabase fdb, final Long idNumber){
        final LinearLayout layout = (LinearLayout) findViewById(R.id.addBikeLayout);
        TextView id = new TextView(this);
        TextView info = new TextView(this);
        TextView serialNo = new TextView(this);
        id.setText("Tag number: " + String.valueOf(bike.getTagNumber()));
        info.setText(bike.getBrand() + " " + bike.getDesc() + " " + bike.getColor());
        serialNo.setText("Serial No: " + bike.getSerialNumber());

        layout.addView(id);
        layout.addView(info);
        layout.addView(serialNo);

        TextView tv = new TextView(this);
        tv.setText("Please Select a note type: ");
        layout.addView(tv);

        final RadioGroup rg = new RadioGroup(this);
        for(MaintenanceDesc m: mainDesc){
            RadioButton r = new RadioButton(this);
            r.setText(m.getDesc());
            rg.addView(r);
        }
        final RadioButton customRB = new RadioButton(this);
        customRB.setText("Custom note:");
        rg.addView(customRB);
        rg.check(customRB.getId());

        layout.addView(rg);
        final EditText custDesc = new EditText(this);
        custDesc.setHint("Custom note ");
        custDesc.setText("");

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(rg.getCheckedRadioButtonId() != customRB.getId()){
                    custDesc.setText("");
                    custDesc.clearFocus();
                    layout.removeView(custDesc);
                    View v = getCurrentFocus();
                   // hideKeyboardFrom(getApplicationContext(), v);
                }
                if(rg.getCheckedRadioButtonId() == customRB.getId()){
                    layout.addView(custDesc);
                }
            }
        });

     /*   rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("test", custDesc.getText().toString());
                if(custDesc.getText().toString().length() > 0){
                    custDesc.setText("");
                }
            }
        }); */
        layout.addView(custDesc);

        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setVisibility(View.VISIBLE);
        okButton.setText("Add Note");


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference ref = fdb.getReference().child("Green Jersey Bikes").child(String.valueOf(idNumber));
                if(custDesc.getText().toString().equals("")) {
                    int id = rg.getCheckedRadioButtonId();
                    RadioButton r = (RadioButton) findViewById(id);
                    String date = String.valueOf(android.text.format.DateFormat.format("yyy-MM-dd", new java.util.Date()));
                    bike.addMainDesc(date + " : " + r.getText().toString());
                    ref.setValue(bike);
                }
                else{
                    String date = String.valueOf(android.text.format.DateFormat.format("yyy-MM-dd", new java.util.Date()));
                    bike.addMainDesc(date + " : " + custDesc.getText().toString());
                    ref.setValue(bike);
                }
                finish();
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
