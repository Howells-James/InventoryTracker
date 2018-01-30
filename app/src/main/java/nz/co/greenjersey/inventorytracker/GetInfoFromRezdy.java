package nz.co.greenjersey.inventorytracker;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.R.id;

public class GetInfoFromRezdy extends AsyncTask<String, Void, String>{
    //Holds the reference to which ui were are going to update, pass in the correct ui from each of the menu options
    Context context;

    public GetInfoFromRezdy(Context context){
        //when this class is called it needs to be passed a reference to which class called it, so it knows where to display the ui elements
        this.context = context;
    }

    ArrayList<Booking> bookings = new ArrayList<Booking>();

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
    public void doAfter(String result){
        try {
            //clear the list everytime TODO figure out a cleaner way of doing this
            bookings.clear();
            //feed the string into a json object
            JSONObject reader = new JSONObject(result);
            //get the bookings array out of the json object
            JSONArray bookingsArray = reader.getJSONArray("bookings");
            //iterate through the bookings array
            for(int i = 0; i < bookingsArray.length(); i++){
                //get the order object out of the array we are iterating through
                JSONObject  orderObject = bookingsArray.getJSONObject(i);
                //get the order id from the order object
                String orderId = orderObject.getString("orderNumber");
                //get the customer object out of the json array, the customer object is contained in the order object
                JSONObject customerObject = orderObject.getJSONObject("customer");
                //get the fields we want out of the customer object
                String custFName = customerObject.getString("firstName");
                String custLName = customerObject.getString("lastName");
                //create a new booking
                Booking b = new Booking(custFName, custLName,orderId);
                bookings.add(b);
            }



            /*
            This part creates the ui elements that will hold the customer information
            */
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //set up a layout
                    ScrollView sv = (ScrollView) ((Activity)context).findViewById(R.id.scrollView);
                    LinearLayout layout = (LinearLayout) ((Activity)context).findViewById(R.id.buttonLayout);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    for(Booking book: bookings){
                        Button button = new Button(context);
                        button.setText(book.fName + " " + book.lName + "\nBooking id : " + book.bookingId);

                        layout.addView(button);
                    }
                }
            });

        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Rezdy servers will return tomorrows bookings if given a range of start of today until end of today
     * Solution is to give rezdy yesterdays date
     * @return yesterdays date
     */
    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }


    private String getDataFromUrl() {
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
        Log.d("result test", result);
        return result;
    }
}