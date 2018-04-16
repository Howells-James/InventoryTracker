package nz.co.greenjersey.inventorytracker.MenuItems;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import nz.co.greenjersey.inventorytracker.Misc.CustomerListBuilder;
import nz.co.greenjersey.inventorytracker.R;

public class RemoveBike extends AppCompatActivity {
    public String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_bike);
        Intent intent = getIntent();
        this.location = intent.getExtras().getString("location");
        //calls a class that handles communication with the rezdy api, this class also handles building the buttons and sending them back here to the ui thread
        new CustomerListBuilder(this, this.location, "removeBike").execute();
    }

}
