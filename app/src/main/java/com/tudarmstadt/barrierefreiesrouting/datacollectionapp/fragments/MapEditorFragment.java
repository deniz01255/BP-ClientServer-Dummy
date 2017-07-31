package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import bp.common.model.Obstacle;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IMapOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.CloseToRoad.CloseToRoadChecker;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.PostObstacleToServerTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.operators.DefaultMapOperator;

import bp.common.model.Stairs;

public class MapEditorFragment extends Fragment implements MapEventsReceiver {


    public MyLocationNewOverlay mLocationOverlay;
    public MapView map;
    private MapEventsOverlay evOverlay;
    private RoadManager roadManager;

    private IMapController mapController;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    public ItemizedOverlayWithFocus<OverlayItem> mOverlay;

    private OnFragmentInteractionListener mListener;
    private IMapOperator activeMapOperator;

    // Leerer Constructor wird benötigt
    public MapEditorFragment() {
    }

    public static MapEditorFragment newInstance(Obstacle obs) {
        MapEditorFragment fragment = new MapEditorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activeMapOperator = new DefaultMapOperator();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_editor, container, false);
        MainActivity activity = (MainActivity)getActivity() ;

        Context context = activity.getApplicationContext();

        map = (MapView) v.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        map.setClickable(true);

        this.evOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(evOverlay);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context),map);
        this.mLocationOverlay.enableMyLocation();
        mLocationOverlay.setDrawAccuracyEnabled(false);
        map.getOverlays().add(this.mLocationOverlay);

        // Auf der Startposition wird die Map zentriert - 49.8728, 8.6512 => Luisenplatz ;)
        GeoPoint startPoint = new GeoPoint(49.8728, 8.6512);
        mapController = map.getController();
        mapController.setCenter(startPoint);

        map.getController().setZoom(20);
        mLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                map.getController().animateTo(mLocationOverlay.getMyLocation());

            }
        });

        mOverlay = new ItemizedOverlayWithFocus<>(activity, items,
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

        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);


        v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        System.out.println("..");

        return activeMapOperator.singleTapConfirmedHelper(p, getActivity(), this);

    }


    @Override
    public boolean longPressHelper(GeoPoint p) {
        System.out.println("..");

        return activeMapOperator.longPressHelper(p,getActivity(),this);
    }



    public void addObstacle(OverlayItem overlayItem) {
        mOverlay.addItem(overlayItem);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void refresh(){
        map.invalidate();

    }



}
