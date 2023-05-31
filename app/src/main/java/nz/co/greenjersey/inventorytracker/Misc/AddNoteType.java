package nz.co.greenjersey.inventorytracker.Misc;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nz.co.greenjersey.inventorytracker.Objects.BikeDescription;
import nz.co.greenjersey.inventorytracker.Objects.MaintenanceDesc;
import nz.co.greenjersey.inventorytracker.R;

public class AddNoteType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note_type);


    }
    public void onClick(View v){

        EditText text = (EditText) findViewById(R.id.noteText);
        String note = text.getText().toString();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MaintenanceDesc");
        MaintenanceDesc m = new MaintenanceDesc(note);

        myRef.push().setValue(m);

        finish();
    }
}
