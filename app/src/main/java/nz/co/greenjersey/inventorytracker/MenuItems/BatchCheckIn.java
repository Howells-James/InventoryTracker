package nz.co.greenjersey.inventorytracker.MenuItems;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nz.co.greenjersey.inventorytracker.R;

public class BatchCheckIn extends AppCompatActivity {
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_check_in);

    }
}
