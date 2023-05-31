package nz.co.greenjersey.inventorytracker.Misc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nz.co.greenjersey.inventorytracker.Objects.BikeDescription;
import nz.co.greenjersey.inventorytracker.R;

public class AddBikeDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bike_description);


    }

    public void onButtonPress(View view){

        EditText textBrand = (EditText) findViewById(R.id.editTextBrand);
        String brand = textBrand.getText().toString();
        EditText textDesc = (EditText) findViewById(R.id.editTextDesc);
        String desc = textDesc.getText().toString();
        EditText textCol = (EditText) findViewById(R.id.editTextCol);
        String col =  textCol.getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Bike Descriptions");
        BikeDescription b = new BikeDescription(desc + "  ", col + "  ", brand+ "  ");

        myRef.push().setValue(b);


        finish();
    }

}
