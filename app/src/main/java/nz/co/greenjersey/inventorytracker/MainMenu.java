package nz.co.greenjersey.inventorytracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    //the location of the store
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent intent = getIntent();
        location = intent.getExtras().getString("location");
        TextView locationText;
        locationText = (TextView)findViewById(R.id.locationTextView);
        locationText.setText("Location: " + location);
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
