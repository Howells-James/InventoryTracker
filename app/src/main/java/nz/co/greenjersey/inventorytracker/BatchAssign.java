package nz.co.greenjersey.inventorytracker;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;


public class BatchAssign extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_assign);
        new Test().execute("s","s","s");

    }


    class Test extends AsyncTask<String, String, String> {

        protected String doInBackground(String... urls){
            try{
                String s = getDataFromUrl("test");
            }catch(Exception e){
                return null;
            }
            return "d";
        }
    }
    //fucking finally
    String apiUrl = "https://api.rezdy.com/v1/bookings/?apiKey=?createdSince2018-01-28";
    String error = ""; // string field
    private String getDataFromUrl(String demoIdUrl) {
        demoIdUrl = apiUrl;

        String result = null;
        int resCode;
        InputStream in;
        try {
            URL url = new URL(demoIdUrl);
            URLConnection urlConn = url.openConnection();

            HttpsURLConnection httpsConn = (HttpsURLConnection) urlConn;
            httpsConn.setAllowUserInteraction(false);
            httpsConn.setInstanceFollowRedirects(true);
            httpsConn.setRequestMethod("GET");
            httpsConn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpsConn.setRequestProperty("Accpet", "application/json");
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("test", result);
        return result;
    }


}
