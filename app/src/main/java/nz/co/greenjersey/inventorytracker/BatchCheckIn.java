package nz.co.greenjersey.inventorytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BatchCheckIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_check_in);
        //calls a class that handles communication with the rezdy api, this class also handles building the buttons and sending them back here to the ui thread
    }
}
