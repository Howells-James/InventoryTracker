package nz.co.greenjersey.inventorytracker.Misc;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.google.firebase.database.ChildEventListener;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.Map;

        import nz.co.greenjersey.inventorytracker.Objects.Bike;
        import nz.co.greenjersey.inventorytracker.Util.ListenList;
        import nz.co.greenjersey.inventorytracker.R;

public class RoundUp extends AppCompatActivity {

    Map<Long, Object> bikes;
    ListenList<Bike> inUse;
    Map<String, ArrayList> CusAndBike;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_up);
        roundUp();

    }

    public void displayData(final int index){
        LinearLayout layout = (LinearLayout) findViewById(R.id.roundUpLayout);
        TextView bikeInfo = new TextView(this);
        bikeInfo.setText("In Use by order: " + inUse.get(index).getCurrentOrder() + " : " + inUse.get(index).getBrand() + " " + inUse.get(index).getDesc() + " " + inUse.get(index).getColor() + " No: " + inUse.get(index).getGjStickerNo());
        layout.addView(bikeInfo);
    }

    public void roundUp() {
        bikes = new HashMap<>();
        inUse = new ListenList<>(this);
        CusAndBike = new HashMap<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("Green Jersey Bikes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Bike b = dataSnapshot.getValue(Bike.class);
                if (b.isInUse()) {
                    inUse.add(b);
                }

            }

            //pointless shit
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });


    }

}
