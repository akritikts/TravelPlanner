package in.silive.travelplanner;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Detailed_view extends AppCompatActivity {
    Cursor cursor;
    DataBaseHelper db=new DataBaseHelper(this);
    Bundle info;
    Integer pos;
    TextView date_txtview;
    TextView source;
    TextView destination;
    TextView train;
    TextView hotel;
    TextView fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        cursor=db.getDetailedJourney();
        cursor.moveToFirst();
        date_txtview = (TextView) findViewById(R.id.date_txtview);
        source = (TextView) findViewById(R.id.source);
        destination = (TextView) findViewById(R.id.destination);
        train = (TextView) findViewById(R.id.train);
        hotel = (TextView) findViewById(R.id.hotel);
        fav = (TextView) findViewById(R.id.fav);
        display_details();
    }

    public void display_details() {
        Log.d("display","detailed");
        Intent i=getIntent();
        pos = Integer.parseInt(i.getStringExtra("position"));
        cursor.moveToPosition(pos);
        date_txtview.setText(cursor.getString(3));
        source.setText(cursor.getString(1));
        destination.setText(cursor.getString(2));
        train.setText(cursor.getString(4));
        hotel.setText(cursor.getString(8));
        fav.setText(cursor.getString(9));
        Log.d("display", "detailed workd");


    }

}
