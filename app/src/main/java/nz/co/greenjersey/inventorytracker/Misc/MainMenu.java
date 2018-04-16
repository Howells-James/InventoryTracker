package nz.co.greenjersey.inventorytracker.Misc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import nz.co.greenjersey.inventorytracker.MenuItems.AddBike;
import nz.co.greenjersey.inventorytracker.MenuItems.AddMaintenanceNote;
import nz.co.greenjersey.inventorytracker.MenuItems.BatchAssign;
import nz.co.greenjersey.inventorytracker.MenuItems.BatchCheckIn;
import nz.co.greenjersey.inventorytracker.MenuItems.RemoveBike;
import nz.co.greenjersey.inventorytracker.R;

public class MainMenu extends AppCompatActivity {

    //the location of the store
    String location;
    TextView locationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        locationText = (TextView)findViewById(R.id.locationTextView);
        location = "Martinborough"; // set as default
        locationText.setText("Location: " + location);
    }

    /**
     * Creates the menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater  inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_options_menu, menu);
        return true;
    }

    /**
     * Called when an item is selected in the menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case R.id.changeLocation :
                final CharSequence[] locations = {"Martinborough", "Petone"};
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Choose a location");
                alert.setSingleChoiceItems(locations, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        location = locations[which].toString();
                        locationText.setText("Location: " + location);
                        dialog.dismiss();
                    }
                });
                AlertDialog alertD = alert.create();
                alertD.show();
                return true;

            case R.id.addBikeDescription :
                Intent addBikeIntent = new Intent(this, AddBikeDescription.class);
                startActivity(addBikeIntent);
                return true;

            case R.id.roundUp :
                Intent intent = new Intent(this, RoundUp.class);
                startActivity(intent);
                return true;

            case R.id.addNoteType:
                Intent addNoteTypeIntent = new Intent(this, AddNoteType.class);
                startActivity(addNoteTypeIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void batchAssign(View view){
        Intent batchAssignIntent = new Intent(this, BatchAssign.class);
        batchAssignIntent.putExtra("location", location);
        startActivity(batchAssignIntent);
    }

    public void batchCheckIn (View view){
        Intent batchCheckInIntent = new Intent(this, BatchCheckIn.class);
        batchCheckInIntent.putExtra("location", location);
        startActivity(batchCheckInIntent);
    }

    public void addMaintenanceNote(View view) {
        Intent addMaintenanceNoteIntent = new Intent(this, AddMaintenanceNote.class);
        addMaintenanceNoteIntent.putExtra("location", location);
        startActivity(addMaintenanceNoteIntent);
    }

    public void addBike(View view){
        Intent addBikeIntent = new Intent(this, AddBike.class);
        addBikeIntent.putExtra("location", location);
        startActivity(addBikeIntent);
    }

    public void removeBike(View view){
        Intent removeBikeIntent = new Intent(this, RemoveBike.class);
        removeBikeIntent.putExtra("location", location);
        startActivity(removeBikeIntent);
    }
}
