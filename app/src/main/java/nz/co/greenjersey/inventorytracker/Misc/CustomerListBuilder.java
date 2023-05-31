package nz.co.greenjersey.inventorytracker.Misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import nz.co.greenjersey.inventorytracker.AssignmentViews.CustomerBikeAssign;
import nz.co.greenjersey.inventorytracker.AssignmentViews.CustomerUnAssignBikes;
import nz.co.greenjersey.inventorytracker.MenuItems.RemoveBike;
import nz.co.greenjersey.inventorytracker.Objects.Booking;
import nz.co.greenjersey.inventorytracker.R;

public class CustomerListBuilder extends AsyncTask<String, Void, String>{
    //Holds the reference to which ui were are going to update, pass in the correct ui from each of the menu options
    private Context context;
    //Holds the location of the store
    private String location;
    private String callingClass;
    //holds the list of bookings for today
    private ArrayList<Booking> bookings = new ArrayList<>();
    private ProgressBar spinner;

    public CustomerListBuilder(Context context, String location, String callingClass){
        //when this class is called it needs to be passed a reference to which class called it, so it knows where to display the ui elements
        this.context = context;
        this.location = location;
        this.callingClass = callingClass;
    }

    protected String doInBackground(String... result){
        try{
            String orderInformation = getDataFromUrl();
            doAfter(orderInformation); //TODO should be onPostExecute(), but cant figure out how to pass object properly here
            return orderInformation;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private void doAfter(String result){
        try {
            //clear the list everytime TODO figure out a cleaner way of doing this
            bookings.clear();
            JSONObject reader = new JSONObject(result);
            JSONArray bookingsArray = reader.getJSONArray("bookings");
            for(int i = 0; i < bookingsArray.length(); i++){
                JSONObject  orderObject = bookingsArray.getJSONObject(i);
                String orderId = orderObject.getString("orderNumber");
                JSONObject customerObject = orderObject.getJSONObject("customer");
                String custFName = customerObject.getString("firstName");
                String custLName = customerObject.getString("lastName");
                Booking b = new Booking(custFName, custLName,orderId);
                bookings.add(b);
            }
            //create the buttons
            createUiObjects();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Create the buttons and push them to the user interface
     */
    private void createUiObjects(){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //set up a layout
                //TODO implement search functionality
                final LinearLayout layout = (LinearLayout) ((Activity) context).findViewById(R.id.buttonLayout);
                layout.setOrientation(LinearLayout.VERTICAL);
                for (final Booking book : bookings) {
                    if (callingClass.equals("batchAssign")) {
                        Button button = new Button(context);
                        button.setText(book.fName + " " + book.lName + "\nBooking id : " + book.bookingId);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent assignBikes = new Intent(context, CustomerBikeAssign.class);
                                assignBikes.putExtra("fName", book.fName);
                                assignBikes.putExtra("lName", book.lName);
                                assignBikes.putExtra("orderNumber", book.bookingId);
                                assignBikes.putExtra("Location", location);
                                context.startActivity(assignBikes);
                            }
                        });
                        layout.addView(button);
                        //spinner = (ProgressBar) ((Activity) context).findViewById(R.id.progressBar2);
                        //spinner.setVisibility(View.GONE);
                    }
                    else if (callingClass.equals("removeBike")) {
                        Log.d("test", "test");
                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final String date = String.valueOf(android.text.format.DateFormat.format("yyy-MM-dd", new java.util.Date()));
                        final DatabaseReference myRef = database.getReference().child("Green Jersey Customers").child(date);
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(book.bookingId).exists()) {
                                    Button button = new Button(context);
                                    button.setText(book.fName + " " + book.lName + "\nBooking id : " + book.bookingId);
                                    button.setOnClickListener(new View.OnClickListener() {
                                    Intent removeBikes = new Intent(context, CustomerUnAssignBikes.class);
                                        @Override
                                        public void onClick(View v) {
                                            Intent assignBikes = new Intent(context, CustomerUnAssignBikes.class);
                                            assignBikes.putExtra("fName", book.fName);
                                            assignBikes.putExtra("lName", book.lName);
                                            assignBikes.putExtra("orderNumber", book.bookingId);
                                            assignBikes.putExtra("Location", location);
                                            context.startActivity(assignBikes);
                                        }

                                    });
                                    layout.addView(button);
                                    //spinner = (ProgressBar) ((Activity) context).findViewById(R.id.progressBar2);
                                    //spinner.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });
                    }
                }
            }
        });

    }

    /**
     * Rezdy servers will return tomorrows bookings if given a range of start of today until end of today
     * Solution is to give rezdy yesterdays date
     * @return yesterdays date
     */
    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }


    private String getDataFromUrl() {
        //TODO add location to query somehow
        String error = "";
        String date = getDate();
        //TODO exclude shuttle bookings
        String apiUrl = "https://api.rezdy.com/v1/bookings/?apiKey=&minTourStartTime=" + date + "T00:00:00Z&maxTourStartTime=" + date + "T24:00:00Z";
        String result = null;
        int resCode;
        InputStream in;
        try {
            URL url = new URL(apiUrl);
            URLConnection urlConn = url.openConnection();

            HttpsURLConnection httpsConn = (HttpsURLConnection) urlConn;
            httpsConn.setAllowUserInteraction(false);
            httpsConn.setInstanceFollowRedirects(true);
            httpsConn.setRequestMethod("GET");
            //Set flags that rezdy needs
            httpsConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpsConn.setRequestProperty("Accept", "application/json");
            httpsConn.connect();

            resCode = httpsConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpsConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                result = sb.toString();
            } else {
                error += resCode;
                Log.e("GetDataFromUrl Error: ", error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }



}