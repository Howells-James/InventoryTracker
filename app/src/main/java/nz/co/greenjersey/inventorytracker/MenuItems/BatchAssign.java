package nz.co.greenjersey.inventorytracker.MenuItems;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nz.co.greenjersey.inventorytracker.Misc.CustomerListBuilder;
import nz.co.greenjersey.inventorytracker.R;


public class BatchAssign extends AppCompatActivity {

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_assign);
        Intent intent = getIntent();
        this.location = intent.getExtras().getString("location");
        //calls a class that handles communication with the rezdy api, this class also handles building the buttons and sending them back here to the ui thread
        new CustomerListBuilder(this, this.location).execute();
    }



}
