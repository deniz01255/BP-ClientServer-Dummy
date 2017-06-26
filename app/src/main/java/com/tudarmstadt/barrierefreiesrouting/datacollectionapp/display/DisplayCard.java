package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.display;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.OkHttpAdress;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacleViews.ObstacleSelection;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.concurrent.ExecutionException;

public class DisplayCard extends AppCompatActivity implements LocationListener {

    // variables for taken the income of the frontend
    private EditText et;
    private Button bt, btPost;
    public TextView tv;
    private OkHttpAdress example;
    private GeoPoint locationPoint;
    private GeoPoint answer;
    private static MapView map;
    private LocationListener listener;
    private LocationManager locationManager;
    private String mprovider;

    String responseString = "";

    // final string because of using server-Address always the same.
    final StringBuilder scriptUrlString = new StringBuilder();//"http://api.openstreetmap.org/api/0.6/trackpoints?bbox=0,51.5,0.25,51.75&page=0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_card);

        scriptUrlString.append("http://maps.googleapis.com/maps/api/geocode/json?address=");

        bt = (Button) findViewById(R.id.button3);
        map = (MapView) findViewById(R.id.map);

        /**locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         Criteria criteria = new Criteria();

         mprovider = locationManager.getBestProvider(criteria, false);

         if (mprovider != null && !mprovider.equals("")) {
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
         return;
         }
         Location location = locationManager.getLastKnownLocation(mprovider);
         locationManager.requestLocationUpdates(mprovider, 15000, 1, this);

         if (location != null)
         onLocationChanged(location);
         else
         Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
         }**/
        /**
         * Button to Receive Data From Server
         */
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            Context ctx = getApplicationContext();
                            //important! set your user agent to prevent getting banned from the osm servers
                            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

                            map.setTileSource(TileSourceFactory.MAPNIK);
                            map.setBuiltInZoomControls(true);
                            map.setMultiTouchControls(true);
                            IMapController mapController = map.getController();
                            mapController.setZoom(15);
                            mapController.setCenter(answer);
                        }

                });

        }
        });


    /**
         * Button to Send-Data From Server
         */

        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tv.setText(example.execute().get().toString());
                        } catch (InterruptedException i) {
                            i.printStackTrace();
                        } catch (ExecutionException ex) {
                            ex.printStackTrace();
                        }

                        System.out.println("fertig");

                    }
                });

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        tv.setText("Longitude = " + location.getLongitude() + "\n Latitude = " + location.getLatitude());

        locationPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        mapController.setCenter(locationPoint);

        //###########
        Marker startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        //startMarker.setIcon(R.drawable.ic_launcher);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        //InfoWindow infoWindow = new MyInfoWindow(R.layout.bonuspack_bubble, map);
        //startMarker.setInfoWindow(infoWindow);
        map.getOverlays().add(startMarker);
        startMarker.setTitle("Title of the marker");
        //###########


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public boolean internetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static MapView getMap(){return map;}


    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    /** Called when the user clicks the add button **/
    public void openObstacleSelection(View view) {
        Intent intent = new Intent(this, ObstacleSelection.class);
        startActivity(intent);
    }
}
