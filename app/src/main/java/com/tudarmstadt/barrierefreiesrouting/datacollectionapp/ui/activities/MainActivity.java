package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate.StateHandler;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.ObstacleDetailsEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.PlaceObstacleOperatorState;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;

import bp.common.model.Construction;
import bp.common.model.Elevator;
import bp.common.model.FastTrafficLight;
import bp.common.model.Obstacle;
import bp.common.model.Ramp;
import bp.common.model.Stairs;
import bp.common.model.TightPassage;
import bp.common.model.Unevenness;

public class MainActivity extends AppCompatActivity
        implements
        AdapterView.OnItemSelectedListener, ObstacleDetailsEditorFragment.OnFragmentInteractionListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener, CheckBoxAttributeFragment.OnFragmentInteractionListener, NumberAttributeFragment.OnFragmentInteractionListener
        , IObstacleProvider {

    private long selectedBarrier;
    public MapEditorFragment mapEditorFragment;
    public ObstacleDetailsEditorFragment obstacleDetailsEditorFragment;
    public BottomNavigationView navigationToolbar;
    private TextView mTitle;

    private StateHandler stateHandler = new StateHandler(this);

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (findViewById(R.id.map_fragment_container) != null) {
            if (savedInstanceState != null)
                return;
            mapEditorFragment = MapEditorFragment.newInstance(this);
            mapEditorFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment_container, mapEditorFragment).commit();
        }
        if (findViewById(R.id.edit_details_container) != null) {
            if (savedInstanceState != null)
                return;
            obstacleDetailsEditorFragment = ObstacleDetailsEditorFragment.newInstance();
            obstacleDetailsEditorFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.edit_details_container, obstacleDetailsEditorFragment).commit();
        }



        // get the bottom sheet view
        LinearLayout rlBottomLayout = (LinearLayout) findViewById(R.id.bottom_sheet);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(rlBottomLayout);

        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        getObstaclesFromServer();

        // Initialize state maschine with first State.
        stateHandler.setupNextState(new PlaceObstacleOperatorState(this));

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mapEditorFragment.map.getController().setCenter(new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude));

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });


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

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
        selectedBarrier = id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public Obstacle getObstacle() {
        switch (String.valueOf(selectedBarrier)) {
            case "0":
                return new Stairs();
            case "1":
                return new Ramp();
            case "2":
                return new Unevenness();
            case "3":
                return new Construction();
            case "4":
                return new FastTrafficLight();
            case "5":
                return new Elevator("test", 0, 9, "1", "5");
            case "6":
                return new TightPassage();
            default:
                return new Stairs();
        }
    }

    public void getObstaclesFromServer() {
        DownloadObstaclesTask.DownloadStairs(this, mapEditorFragment);
    }

    public StateHandler getStateHandler() {
        return stateHandler;
    }




}