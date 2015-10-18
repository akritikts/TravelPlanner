package in.silive.travelplanner.Nearby_Places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import in.silive.travelplanner.R;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    URL murl;
    HttpURLConnection mconnection;
    BufferedReader mbufferedReader;
    StringBuilder mresponse = new StringBuilder();
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> mdisplay;
    String destination_city;
    String Api_key = "AIzaSyDT7HTfZS8fCk9HGqSRrtebfACIchDSOa4";
    LocationManager locationManager;
    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    Bundle info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

    }

    public class display_hotels extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void[] params) {

            try {
                Intent intent = getIntent();
                info = intent.getBundleExtra("info");
                destination_city = info.getString("dest_station");
                murl = new URL("https://maps.googleapis.com/maps/api/place/textsearch/json?query=Tourist+spots+in+" + destination_city + "&key=" + Api_key);
                mconnection = (HttpURLConnection) murl.openConnection();
                Log.d("Hotels_Search", "url : " + murl);
                Log.d("display", "connection");
                mconnection.setRequestMethod("GET");
                mconnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");

                mconnection.connect();
                mbufferedReader = new BufferedReader(new InputStreamReader(mconnection.getInputStream()));

                String line = "";
                while ((line = mbufferedReader.readLine()) != null) {
                    mresponse.append(line + "\n");


                }
            } catch (Exception e) {
                Log.d("display", "NOconnection");
                e.printStackTrace();
            }
            if (mresponse.toString() != null)
                return mresponse.toString();
            else
                return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s)

        {
            // super.onPostExecute(s);

            parsing_locs(s);
            mdisplay.notifyDataSetChanged();


        }
    }

    public void parsing_locs(String s) {
        try {
            double lat, lng;
            String str;
            JSONObject l_places = new JSONObject(s);
            JSONArray places = l_places.getJSONArray("results");
            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.getJSONObject(i);
                str = place.getString("name");
                JSONObject latlng = places.getJSONObject(i);
                lat = location.getLatitude();
                lng = location.getLongitude();
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(str));

            }
        } catch (Exception e) {
            Log.d("display", "Parsing not working");
        }

    }


}
