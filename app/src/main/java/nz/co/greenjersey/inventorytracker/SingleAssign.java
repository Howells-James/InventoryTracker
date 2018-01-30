package nz.co.greenjersey.inventorytracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleAssign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_assign);
        new GetInfoFromRezdy(this).execute();
    }
}
