package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewOverlay;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.CompleteRoadEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemSingleTapEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstaclePositionSelectedOnPolylineEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoadPositionSelectedOnPolylineEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoadsHelperOverlayChangedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoutingServerObstaclePostedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoutingServerObstaclesDownloadedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.StartEditObstacleEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleOverlayItem;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.ObstacleDetailsViewerFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator.PlaceNearestRoadsOnMapOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator.RoadEditorOperator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bp.common.model.obstacles.Construction;
import bp.common.model.obstacles.Elevator;
import bp.common.model.obstacles.FastTrafficLight;
import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Ramp;
import bp.common.model.obstacles.Stairs;
import bp.common.model.obstacles.TightPassage;
import bp.common.model.obstacles.Unevenness;
import bp.common.model.ways.Node;
import bp.common.model.ways.Way;
import okhttp3.Response;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * The starting point of the app.
 *
 * This Activity displays the map fragment, the bottom sheet and the searchView.
 *
 *
 * Events send via the EventBus that require an update on the map, are handled in the
 * onMessageEvent() methods with the respective Event class as Parameter.
 */
public class BrowseMapActivity extends AppCompatActivity
        implements
        AdapterView.OnItemSelectedListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener, CheckBoxAttributeFragment.OnFragmentInteractionListener, NumberAttributeFragment.OnFragmentInteractionListener
        , IObstacleProvider {

    private long selectedBarrier;
    public FloatingActionButton floatingActionButton;
    public FloatingActionButton floatingActionButtonRoad;
    public MapEditorFragment mapEditorFragment;
    private ArrayList<Polyline> currentPolylineArrayList = new ArrayList<>();
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browser_map);

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




        // get the bottom sheet view
        LinearLayout rlBottomLayout = (LinearLayout) findViewById(R.id.bottom_sheet);

        bottomSheetBehavior = BottomSheetBehavior.from(rlBottomLayout);

        bottomSheetBehavior.setHideable(false);

        BottomSheetBehavior.from(rlBottomLayout)
                .setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        floatingActionButtonRoad = (FloatingActionButton) findViewById(R.id.action_send_Street);

        floatingActionButtonRoad.hide();

        floatingActionButtonRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<GeoPoint> geop = new ArrayList<GeoPoint>();
                ArrayList<Node> nodeList = new ArrayList<Node>();
                List<Overlay> xx = mapEditorFragment.map.getOverlays();
                for(int i = xx.size()-1; i > 0; i--){
                    if (Polyline.class.isInstance(xx.get(i))|| Marker.class.isInstance(xx.get(i))) {
                        if(Marker.class.isInstance(xx.get(i))){
                            xx.get(i).isEnabled();
                            geop.add(((Marker) xx.get(i)).getPosition());
                        }
                    } else{
                        break;
                    }
                }
                for (GeoPoint gp: geop) {
                    nodeList.add(new Node(gp.getLatitude(),gp.getLongitude()));
                }

                Intent i = new Intent(BrowseMapActivity.this, PLaceRoadActivity.class);
                StringBuilder sb = new StringBuilder();
                for (Node nd: nodeList) {
                    sb.append(nd.getLatitude());
                    sb.append(nd.getLongitude());
                    sb.append("|");
                }
                i.putExtra("key", sb.toString());
                startActivity(i);
               // Intent intent = new Intent(BrowseMapActivity.this, PLaceRoadActivity.class);
               // startActivity(intent);



            }
        });




        floatingActionButton = (FloatingActionButton) findViewById(R.id.action_place_obstacle);

        floatingActionButton.hide();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BrowseMapActivity.this, PlaceObstacleActivity.class);
                startActivity(intent);

            }
        });


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mapEditorFragment.map.getController().setCenter(new GeoPoint(place.getLatLng().latitude, place.getLatLng().longitude));
            }

            @Override
            public void onError(Status status) {
            }
        });

        Button placeObstacleModeButton = (Button) findViewById(R.id.bottom_sheet_button_place_obstacle_mode);
        placeObstacleModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapEditorFragment.getStateHandler().setActiveOperator(new PlaceNearestRoadsOnMapOperator());
            }
        });

        Button roadEditorModeButton = (Button) findViewById(R.id.bottom_sheet_button_road_edit_mode);
        roadEditorModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                floatingActionButton.hide();
                mapEditorFragment.placeNewObstacleOverlay.removeAllItems();
                for (Polyline p : currentPolylineArrayList) {
                    mapEditorFragment.map.getOverlays().remove(p);
                }

                ObstacleDataSingleton.getInstance().obstacleDataCollectionCompleted = false;
                mapEditorFragment.getStateHandler().setActiveOperator(new RoadEditorOperator());
                mapEditorFragment.map.invalidate();

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getObstaclesFromServer();

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Context context = getApplicationContext();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));


        // Check if the last obstacle data collection was completed

        if (ObstacleDataSingleton.getInstance().obstacleDataCollectionCompleted) {
            floatingActionButton.hide();
            mapEditorFragment.placeNewObstacleOverlay.removeAllItems();
            for (Polyline p : currentPolylineArrayList) {
                mapEditorFragment.map.getOverlays().remove(p);
            }

            ObstacleDataSingleton.getInstance().obstacleDataCollectionCompleted = false;
        }

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
        DownloadObstaclesTask.downloadObstacles();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(RoutingServerObstaclesDownloadedEvent event) {

        try {
            Response response = event.getResponse();
            String res = response.body().string();

            final ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            if (!response.isSuccessful())
                return;

            final List<Obstacle> obstacleList = mapper.readValue(res, new TypeReference<List<Obstacle>>() {
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Obstacle obstacle : obstacleList) {
                        ObstacleOverlayItem overlayItem = new ObstacleOverlayItem(obstacle.getName(), getString(R.string.default_description), new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()), obstacle);
                        overlayItem.setMarker(getResources().getDrawable(R.mipmap.ramppic));
                        mapEditorFragment.obstacleOverlay.addItem(overlayItem);
                    }
                    Toast.makeText(getBaseContext(), getString(R.string.action_barrier_loaded),
                            Toast.LENGTH_SHORT).show();
                    mapEditorFragment.map.invalidate();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEvent(RoutingServerObstaclePostedEvent event) {

        final Obstacle obstacle = event.getObstacle();
        Response response = event.getResponse();

        if (!response.isSuccessful())
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ObstacleOverlayItem overlayItem = new ObstacleOverlayItem(obstacle.getName(), getString(R.string.default_description), new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()), obstacle);
                overlayItem.setMarker(getResources().getDrawable(R.mipmap.ramppic));
                mapEditorFragment.obstacleOverlay.addItem(overlayItem);

                Toast.makeText(getBaseContext(), getString(R.string.action_barrier_loaded),
                        Toast.LENGTH_SHORT).show();

                mapEditorFragment.map.invalidate();
            }
        });


    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(RoadPositionSelectedOnPolylineEvent event) {
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();
        GeoPoint point = event.getPoint();
        if (point != null) {
            OverlayItem overlayItem = new OverlayItem("", "", point);
            mapEditorFragment.placeNewObstacleOverlay.addItem(overlayItem);
            mapEditorFragment.map.invalidate();
            floatingActionButtonRoad.show();
        } else {

            floatingActionButtonRoad.hide();
        }
        ObstacleDataSingleton.getInstance().currentPositionOfSetObstacle = point;


    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(ObstaclePositionSelectedOnPolylineEvent event) {
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();
        GeoPoint point = event.getPoint();
        if (point != null) {
            OverlayItem overlayItem = new OverlayItem("", "", point);
            mapEditorFragment.placeNewObstacleOverlay.addItem(overlayItem);
            mapEditorFragment.map.invalidate();
            floatingActionButton.show();
        } else {

            floatingActionButton.hide();
        }
        ObstacleDataSingleton.getInstance().currentPositionOfSetObstacle = point;


    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(RoadsHelperOverlayChangedEvent event) {
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        for (Polyline p : currentPolylineArrayList) {
            mapEditorFragment.map.getOverlays().remove(p);
        }

        currentPolylineArrayList = event.getRoads();

        for (Polyline p : currentPolylineArrayList) {
            mapEditorFragment.map.getOverlays().add(p);
        }

        mapEditorFragment.map.getOverlays().remove(mapEditorFragment.placeNewObstacleOverlay);
        mapEditorFragment.map.getOverlays().add(mapEditorFragment.placeNewObstacleOverlay);

        mapEditorFragment.map.invalidate();

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ObstacleOverlayItemSingleTapEvent event) {

        ObstacleOverlayItem overlayItem = event.getOverlayItem();

        LinearLayout rlBottomLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
        BottomSheetBehavior.from(rlBottomLayout)
                .setState(BottomSheetBehavior.STATE_EXPANDED);

        ObstacleDetailsViewerFragment obstacleDetailsFragment = ObstacleDetailsViewerFragment.newInstance(event.getOverlayItem().getObstacle());
        obstacleDetailsFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.obstacle_bottom_sheet_details_container, obstacleDetailsFragment).commit();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StartEditObstacleEvent event) {
        ObstacleDataSingleton.getInstance().setObstacle(event.getObstacle());
        ObstacleDataSingleton.getInstance().currentPositionOfSetObstacle = new GeoPoint(event.getObstacle().latitude, event.getObstacle().longitude);
        Intent intent = new Intent(BrowseMapActivity.this, PlaceObstacleActivity.class);
        startActivity(intent);
    }

}