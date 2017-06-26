package com.tudarmstadt.barrierefreiesrouting.datacollectionapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.util.constants.MapViewConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bp.common.model.Obstacle;
import bp.common.model.Stairs;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements LocationListener, MapViewConstants, AdapterView.OnItemSelectedListener {

    // variables for taken the income of the frontend
    private EditText et;
    private Button dispCurrentPosBUTTON, addBarrierBUTTON;
    public TextView tv;
    private BpServerHandler example;
    private GeoPoint locationPoint;
    private GeoPoint answer;
    private static MapView map;
    private LocationListener listener;
    private LocationManager locationManager;
    private String mprovider;
    private ArrayList<OverlayItem> mItems;
    private Overlay mMYLocationOverlay;
    private ItemizedOverlayWithFocus<OverlayItem> barriersOverlay;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

    private long selectedBarrier;
    private String barrier;


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
        addBarrierBUTTON = (Button) findViewById(R.id.button3);
       // dispCurrentPosBUTTON = (Button) findViewById(R.id.button);
        map = (MapView) findViewById(R.id.map);
        GeoPoint startPoint = new GeoPoint(49.8728, 8.6512);
        IMapController mapController = map.getController();
        mapController.setCenter(startPoint);

        map.getController().setZoom(18);

        Spinner dropDownMenu = (Spinner) findViewById(R.id.spinner2);
        ColorDrawable backgroundColor = new ColorDrawable(0xAAAA6666);
        dropDownMenu.setBackground(backgroundColor);
       // dropDownMenu.setPopupBackgroundResource(R.color.popUPColorlightRed);
        dropDownMenu.setOnItemSelectedListener(this);
            // Create an ArrayAdapter using the string array and a default dropDownMenu layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the dropDownMenu
        dropDownMenu.setAdapter(adapter);

        final MainActivity mainActivity = this;

        addBarrierBUTTON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeMYDialog();
            }
        });

        barriersOverlay = new ItemizedOverlayWithFocus<>(this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });


        barriersOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(barriersOverlay);

        getObstaclesFromServer();
        map.invalidate();
    }


    private void makeMYDialog() {

        switch (String.valueOf(selectedBarrier)) {

            case "0" : // stairspic

                View viewStairs = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_dialog_stairs, null);
                final EditText stairAmmountStairs = (EditText) viewStairs.findViewById(R.id.editTextStairsAmount);
                final EditText stairHeightStairs = (EditText) viewStairs.findViewById(R.id.editTextStairsAmountH);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("Barrier stair configuration").setView(viewStairs).setPositiveButton("Create Barrier", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            /**locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                             Criteria criteria = new Criteria();
                             mprovider = locationManager.getBestProvider(criteria, false);
                             if (mprovider != null && !mprovider.equals("")) {
                             if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                             return;
                             }
                             Location location = locationManager.getLastKnownLocation(mprovider);
                             **/

                            int height = Integer.parseInt(stairHeightStairs.getText().toString());
                            int number = Integer.parseInt(stairAmmountStairs.getText().toString());
                            //default test darmstadt49.8705556
                            GeoPoint coordinaten = new GeoPoint(49.8705556,8.6494444);
                            Stairs stairs = new Stairs();

                            stairs.setLongitude(coordinaten.getLongitude());
                            stairs.setLatitude(coordinaten.getLatitude());

                            stairs.setNumberOfStairs(number);
                            stairs.setHandleAvailable(true);

                            pushObstacleToServer(stairs);
                            createOverlayItemOnMap(stairs);
                            
                            }
                        }
                );
                AlertDialog alertStairs = builder.create();
                alertStairs.show();


                break;
            case "1" : // Ramp


                View viewRamp = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_dialog_ramp, null);
                final EditText degree = (EditText) viewRamp.findViewById(R.id.editTextRamp);


                AlertDialog.Builder builderRamp = new AlertDialog.Builder(MainActivity.this);

                builderRamp.setMessage("Barrier Ramp configuration").setView(viewRamp).setPositiveButton("Create Barrier", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int deg = Integer.parseInt(degree.getText().toString());
                                //default test darmstadt49.8705556
                                GeoPoint coordinaten = new GeoPoint(49.8705556,8.6494444);
                               // Ramp ramp = new Ramp("RAMP", coordinaten.getLongitude(), coordinaten.getLatitude(), deg,true);

                                //Gson gson = new Gson();
                                //Log.v("Object stairs in JSON", gson.toJson(ramp));
                                //createOverlayItemOnMap(ramp, "RAMPE",coordinaten);



                            }
                        }
                );
                AlertDialog alertRamp = builderRamp.create();
                alertRamp.show();

                break;
            case "2" : // Uneveness

                break;
            case "3" : // Construction

                break;
            case "4" : // Fast_Traffic_Light

                break;
            case "5" : // Elevator

                break;
            case "6" : // Tight_Passage

                break;
        }
    }

    private void pushObstacleToServer(final Obstacle obstacle) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonString = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            jsonString = mapper.writeValueAsString(obstacle);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        RequestBody body = RequestBody.create(JSON, jsonString);

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url("https://routing.vincinator.de/routing/barriers")
                .post(body)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Fehler",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(MainActivity.this, "Barriere hinzugefügt",
                                        Toast.LENGTH_LONG).show();

                                createOverlayItemOnMap(obstacle);
                               }
                        });

                    }
                });



    }

    private void createOverlayItemOnMap(Obstacle obs) {

        OverlayItem overlayItem = new OverlayItem(obs.getName(), "Chabo", new GeoPoint(obs.getLongitude(), obs.getLatitude()));
        overlayItem.setMarker(getResources().getDrawable(R.mipmap.ramppic));
        barriersOverlay.addItem(overlayItem);
        map.invalidate();

    }

    private void runMyPosition() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();

        mprovider = locationManager.getBestProvider(criteria, false);

        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, MainActivity.this);
            //locationManager.requestLocationUpdates(mprovider, 15000, 1, this);


            if (location != null) {
                onLocationChanged(location);
            } else {
            }


        }
    }

    private void printMYLOG(String barrier) {
        Log.d("Selection of Barrier", barrier);
    }

    @Override
    public void onLocationChanged(Location location) {

        locationPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(15);
        mapController.setCenter(locationPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        //InfoWindow infoWindow = new MyInfoWindow(R.layout.bonuspack_bubble, map);
        //startMarker.setInfoWindow(infoWindow);
        map.getOverlays().add(startMarker);
        // startMarker.setTitle("Barrier-Type: stairspic\n Ammount of stairspic = "+ stairs.getNumberOfStairs()+"\n Heigth of stairspic: "+stairs.getHeightOfStairs());
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

    public static MapView getMap() {
        return map;
    }


    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }


    public void openMapView(View view) {
        //Intent intent = new Intent(this, DisplayCard.class);
        //startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        Log.d("MyApp", String.valueOf(id));
        setChosenBarrier(id);
            //Open dioalog window with input
            //   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

             // ID ist absteigend von 0 - n von den Elementen
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setChosenBarrier(long barrier){
        selectedBarrier = barrier;
    }

    public void getObstaclesFromServer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://routing.vincinator.de/routing/barriers")
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        ObjectMapper mapper = new ObjectMapper();
                        if (!response.isSuccessful())
                            return;
                        final List<bp.common.model.Obstacle> obstacleList = mapper.readValue(res, new TypeReference<List<bp.common.model.Obstacle>>() {
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (bp.common.model.Obstacle obstacle : obstacleList) {
                                    OverlayItem overlayItem = new OverlayItem(obstacle.getName(), "Importierte Barriere", new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()));
                                    overlayItem.setMarker(getResources().getDrawable(R.mipmap.ramppic));
                                    barriersOverlay.addItem(overlayItem);
                                }
                                map.invalidate();
                            }
                        });

                    }
                });
    }

}