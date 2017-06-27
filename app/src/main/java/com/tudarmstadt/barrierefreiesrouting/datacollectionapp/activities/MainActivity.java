package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.ObstacleToAttributeViewConverter;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.ObstacleDetailsFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

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


public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, ObstacleDetailsFragment.OnFragmentInteractionListener, MapEditorFragment.OnFragmentInteractionListener {

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
    private MapEditorFragment mapEditorFragment = null;

    String responseString = "";

    final StringBuilder scriptUrlString = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.map_fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            mapEditorFragment = new MapEditorFragment();

            mapEditorFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment_container, mapEditorFragment).commit();
        }

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



        ObstacleToAttributeViewConverter.convert(new Obstacle(), mapEditorFragment.getContext());

        BpServerHandler.getObstaclesFromServer(this, mapEditorFragment);
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

                            int height = Integer.parseInt(stairHeightStairs.getText().toString());
                            int number = Integer.parseInt(stairAmmountStairs.getText().toString());

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

    public void onResume() {
        super.onResume();

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        if (mapEditorFragment != null && mapEditorFragment.mLocationOverlay != null) {
            mapEditorFragment.mLocationOverlay.enableMyLocation();
            mapEditorFragment.mLocationOverlay.enableFollowLocation();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mapEditorFragment != null && mapEditorFragment.mLocationOverlay != null) {
            mapEditorFragment.mLocationOverlay.disableMyLocation();
            mapEditorFragment.mLocationOverlay.disableFollowLocation();
        }
    }

    public void setChosenBarrier(long barrier){
        selectedBarrier = barrier;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        setChosenBarrier(id);
        FragmentManager fragmentManager = getFragmentManager();



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}