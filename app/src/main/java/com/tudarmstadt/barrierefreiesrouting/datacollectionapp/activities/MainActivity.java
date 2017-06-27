package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.ObstacleDetailsFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

import bp.common.model.Construction;
import bp.common.model.FastTrafficLight;
import bp.common.model.Obstacle;
import bp.common.model.Ramp;
import bp.common.model.Stairs;
import bp.common.model.TightPassage;
import bp.common.model.Unevenness;


public class MainActivity extends AppCompatActivity
        implements
        AdapterView.OnItemSelectedListener, ObstacleDetailsFragment.OnFragmentInteractionListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener
        , IObstacleProvider, IMapFragmentProvider{
    // variables for taken the income of the frontend
    private EditText et;
    private Button dispCurrentPosBUTTON, addBarrierBUTTON;
    public TextView tv;

    private ItemizedOverlayWithFocus<OverlayItem> barriersOverlay;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

    private long selectedBarrier;
    private String barrier;
    private MapEditorFragment mapEditorFragment;
    private ObstacleDetailsFragment obstacleDetailsFragment = null;

    public MapEditorFragment getMapEditorFragment(){ return mapEditorFragment;}

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

        Guideline editorTopLine = (Guideline) findViewById(R.id.horizontalEditGuideline);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) editorTopLine.getLayoutParams();

        lp.guidePercent = 0.5f;
        editorTopLine.setLayoutParams(lp);

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
                return new Unevenness();
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