package nz.co.greenjersey.inventorytracker.Misc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import nz.co.greenjersey.inventorytracker.MenuItems.AddMaintenanceNote;
import nz.co.greenjersey.inventorytracker.MenuItems.BatchAssign;
import nz.co.greenjersey.inventorytracker.MenuItems.BatchCheckIn;
import nz.co.greenjersey.inventorytracker.MenuItems.BatchCheckOut;
import nz.co.greenjersey.inventorytracker.MenuItems.SingleAssign;
import nz.co.greenjersey.inventorytracker.MenuItems.SingleCheckIn;
import nz.co.greenjersey.inventorytracker.MenuItems.SingleCheckOut;
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
        String location = "Martinborough"; // set as default
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
        //there's only one item at the moment, but may as well have this in a switch to make it easy in the future
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void batchAssign(View view){
        Intent batchAssignIntent = new Intent(this, BatchAssign.class);
        batchAssignIntent.putExtra("location", location);
        startActivity(batchAssignIntent);
    }

    public void singleAssign (View view){
        Intent singleAssignIntent = new Intent(this, SingleAssign.class);
        singleAssignIntent.putExtra("location", location);
        startActivity(singleAssignIntent);
    }

    public void batchCheckIn (View view){
        Intent batchCheckInIntent = new Intent(this, BatchCheckIn.class);
        batchCheckInIntent.putExtra("location", location);
        startActivity(batchCheckInIntent);
    }

    public void batchCheckOut (View view){
        Intent batchCheckOutIntent = new Intent(this, BatchCheckOut.class);
        batchCheckOutIntent.putExtra("location", location);
        startActivity(batchCheckOutIntent);
    }

    public void singleCheckIn(View view){
        Intent singleCheckInIntent = new Intent(this, SingleCheckIn.class);
        singleCheckInIntent.putExtra("location", location);
        startActivity(singleCheckInIntent);
    }

    public void singleCheckOut(View view){
        Intent singleCheckOutIntent = new Intent(this, SingleCheckOut.class);
        singleCheckOutIntent.putExtra("location", location);
        startActivity(singleCheckOutIntent);
    }

    public void addMaintenanceNote(View view) {
        Intent addMaintenanceNoteIntent = new Intent(this, AddMaintenanceNote.class);
        addMaintenanceNoteIntent.putExtra("location", location);
        startActivity(addMaintenanceNoteIntent);
    }
}
