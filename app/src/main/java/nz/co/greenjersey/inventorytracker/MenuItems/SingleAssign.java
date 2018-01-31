package nz.co.greenjersey.inventorytracker.MenuItems;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nz.co.greenjersey.inventorytracker.Misc.CustomerListBuilder;
import nz.co.greenjersey.inventorytracker.R;

public class SingleAssign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_assign);
        String location = getIntent().getExtras().getString("location");
        new CustomerListBuilder(this, location).execute();
    }
}
