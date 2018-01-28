package nz.co.greenjersey.inventorytracker;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * locationSelectedMartinborough will open up the main menu consisting of the actions that can  be taken
     * it also passes a string located in the Intent object, the string has the value "Martinborough"
     * The String can be accessed in the child activity with
     * Intent intent = getIntent();
     * String location = intent.getExtras().getString("location");
      * @param view
     */
    public void locationSelectedMartinborough(View view){
        Intent mboroIntent = new Intent(this, MainMenu.class);
        mboroIntent.putExtra("location", "Martinborough");
        startActivity(mboroIntent);
    }

    /**
     * locationSelectedPetone will open up the main menu consisting of the actions that can  be taken
     * it also passes a string located in the Intent object, the string has the value "Petone"
     * The String can be accessed in the child activity with
     * Intent intent = getIntent();
     * String location = intent.getExtras().getString("location");
     * @param view
     */
    public void locationSelectedPetone(View view){
        Intent petoneIntent = new Intent(this, MainMenu.class);
        petoneIntent.putExtra("location", "Petone");
        startActivity(petoneIntent);
    }

}
