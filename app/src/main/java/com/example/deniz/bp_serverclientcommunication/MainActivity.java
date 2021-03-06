package com.example.deniz.bp_serverclientcommunication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import android.location.Criteria;

import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.concurrent.ExecutionException;




public class MainActivity extends AppCompatActivity implements LocationListener{

    // variables for taken the income of the frontend
    private EditText et;
    private Button bt, btPost;
    public TextView tv;
    private OkHttpAdress example;
    private OkHttpSendData examplePost;
    private GeoPoint locationPoint;
    private GeoPoint answer;
    private MapView map;
    private LocationListener listener;
    private LocationManager locationManager;
    private String mprovider;






    /**
     * Called when the activity is first created.
     */


    String responseString = "";

    // final string because of using server-Address always the same.
    final StringBuilder scriptUrlString = new StringBuilder();//"http://api.openstreetmap.org/api/0.6/trackpoints?bbox=0,51.5,0.25,51.75&page=0";

    /**
     * Method is always created automatically by Developing environment, setting up an interactive Gui connected to backend
     * <p>
     * Function of method on Create(Bundle): - connecting attributes to buttons on our surface
     * - add ONE ButtonClickListener for sending message to Server -> calling method sendToServer
     * [Checks always first for an Internet connection before sending to Server]
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        scriptUrlString.append("http://maps.googleapis.com/maps/api/geocode/json?address=");

        et = (EditText) findViewById(R.id.editText);
        bt = (Button) findViewById(R.id.button);
        btPost = (Button) findViewById(R.id.postButton);
        tv = (TextView) findViewById(R.id.textView2);
        map = (MapView) findViewById(R.id.map);



            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
            }



/*
#####################################################################################################################
 */




/*
####################################################################################################################
 */

        /**
         * Button to Receive Data From Server
         */
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scriptUrlString.append(et.getText().toString());
                        scriptUrlString.append("&sensor=true");
                        String param = scriptUrlString.toString();
                        example = new OkHttpAdress(tv, param);
                        try {
                            Log.d("MyApp","I am here");
                            answer = example.execute().get();

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException i) {
                            i.printStackTrace();
                        }
                        if (answer == null) {
                            tv.setText("ERROR-MESSAGE: Can't receive Server, please check internet-connection ");
                        } else {

                            tv.setText("Korrekt");


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
                            OkHttpSendData example = new OkHttpSendData();
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

        tv.setText("Longitude = "+ location.getLongitude() + "\n Latitude = "+ location.getLatitude());

        locationPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        mapController.setCenter(locationPoint);


        //###########
        Marker startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(location.getLatitude(),location.getLongitude()));
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


    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }



}
