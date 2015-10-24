package in.silive.travelplanner.Nearby_Places;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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

public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap;
    URL murl;
    HttpURLConnection mconnection;
    BufferedReader mbufferedReader;
    StringBuilder mresponse = new StringBuilder();
    String destination_city;
    String Api_key = "AIzaSyC1fvg262FI2ir1HxEhA9tAFdJ6xaE64vE";
    Bundle info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().isCompassEnabled();
        new map_act().execute();


    }

    public class map_act extends AsyncTask<Void, Void, String> {


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
                lat = latlng.getDouble("lat");
                lng = latlng.getDouble("lng");
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(str));

            }
        } catch (Exception e) {
            Log.d("display", "Parsing not working");
        }

    }


}
