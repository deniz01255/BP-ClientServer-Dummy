package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities;

import android.app.Activity;
import android.app.FragmentManager;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
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

import bp.common.model.Construction;
import bp.common.model.Elevator;
import bp.common.model.FastTrafficLight;
import bp.common.model.Obstacle;
import bp.common.model.Ramp;
import bp.common.model.Stairs;
import bp.common.model.TightPassage;
import bp.common.model.Uneveness;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler.PostNewObstacle;


public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, ObstacleDetailsFragment.OnFragmentInteractionListener, MapEditorFragment.OnFragmentInteractionListener
        , IObstacleProvider{
    // variables for taken the income of the frontend
    private EditText et;
    private Button dispCurrentPosBUTTON, addBarrierBUTTON;
    public TextView tv;

    private ItemizedOverlayWithFocus<OverlayItem> barriersOverlay;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

    private long selectedBarrier;
    private String barrier;
    private MapEditorFragment mapEditorFragment = null;
    private ObstacleDetailsFragment obstacleDetailsFragment = null;


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
        if (findViewById(R.id.obstacle_editor_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            obstacleDetailsFragment = new ObstacleDetailsFragment();
            obstacleDetailsFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.obstacle_editor_fragment_container, obstacleDetailsFragment).commit();
        }


        Spinner dropDownMenu = (Spinner) findViewById(R.id.spinner2);
        ColorDrawable backgroundColor = new ColorDrawable(0xAAAA6666);

        dropDownMenu.setBackground(backgroundColor);
        // dropDownMenu.setPopupBackgroundResource(R.color.popUPColorlightRed);
        dropDownMenu.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default dropDownMenu layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.BARRIER_TYPES, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the dropDownMenu
        dropDownMenu.setAdapter(adapter);


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
                             //PostNewObstacle((Activity) MainActivity.class, mapEditorFragment, stairs);
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

    private void createOverlayItemOnMap(Obstacle obs) {

        OverlayItem overlayItem = new OverlayItem(obs.getName(), "Chabo", new GeoPoint(obs.getLongitude(), obs.getLatitude()));
        overlayItem.setMarker(getResources().getDrawable(R.mipmap.ramppic));
        barriersOverlay.addItem(overlayItem);

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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.obstacle_editor_fragment_container, ObstacleDetailsFragment.newInstance()).commit();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Obstacle getObstacle() {

        switch(String.valueOf(selectedBarrier)){
            case "0":
                return new Stairs();
            case "1":
                return new Ramp();
            case "2":
                return new Uneveness();
            case "3":
                return new Construction();
            case "4":
                return new FastTrafficLight();
            case "5":
                return new Stairs();
            case "6":
                return new TightPassage();
            default:
                return new Stairs();

        }
    }
}