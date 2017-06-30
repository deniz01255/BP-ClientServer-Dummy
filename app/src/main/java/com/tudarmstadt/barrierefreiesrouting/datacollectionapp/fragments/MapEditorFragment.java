package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
import org.osmdroid.bonuspack.overlays.BasicInfoWindow;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import bp.common.model.Obstacle;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.PostObstacleToServerTask;

import bp.common.model.Stairs;

public class MapEditorFragment extends Fragment implements MapEventsReceiver {

    public Road[] mRoads;
    public Road road;
    public MyLocationNewOverlay mLocationOverlay;
    public MapView map;
    private MapEventsOverlay evOverlay;
    private Polyline polylineRoadOverlay;


    private IMapController mapController;
    private ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
    public ItemizedOverlayWithFocus<OverlayItem> mOverlay;

    private OnFragmentInteractionListener mListener;

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

        map.getController().setZoom(18);
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

        Guideline editorTopLine = (Guideline) getActivity().findViewById(R.id.horizontalEditGuideline);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) editorTopLine.getLayoutParams();

        lp.guidePercent = 0.7f;
        editorTopLine.setLayoutParams(lp);

        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {

        final Stairs newObstacle = new Stairs("Chabos wissen wo die Treppe steht", p.getLongitude(), p.getLatitude(), 10, 10, false) ;
        //if (DownloadObstaclesTask.PostNewObstacle(getActivity(), this, newObstacle))
          //  return true;
        PostObstacleToServerTask.PostStairs(getActivity(), this, newObstacle);
       //OnRoadChecker onRoadChecker = new OnRoadChecker();
        //polylineRoadOverlay = onRoadChecker.pointOnRoadCheck(getActivity(),newObstacle);

        //map.getOverlays().add(polylineRoadOverlay);
        //refresh();
        GeoPoint startPoint = new GeoPoint( 51.16569,10.451525);
        GeoPoint endPoint = new GeoPoint(50.8100321,  12.3850986);

       // RoadManager roadManager = new MapQuestRoadManager("P9eWLsqG8k7C30Gcl2jzeAqHByyl5bZz");

        // start and end points
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        // GeoPoint endPoint = new GeoPoint(48.4, -1.9);
        waypoints.add(endPoint);
        // roadManager.addRequestOption("routeType=bicycle");
        //retreive the road between those points
        //Road road = roadManager.getRoad(waypoints);
      //  if (road.mStatus != Road.STATUS_OK){
       //     Log.d("Road Status", ""+road.mStatus);
       // }

       // Log.d("Road Status", ""+road.mNodes.size());


        //new UpdateRoadTask().execute(waypoints);
        return true;



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

    /**
     * Async task to get the road in a separate thread.
     */
    /**  private class UpdateRoadTask extends AsyncTask<Object, Void, Road> {

        protected Road doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            ArrayList<GeoPoint> waypoints = (ArrayList<GeoPoint>)params[0];
            RoadManager roadManager = new OSRMRoadManager();


            return roadManager.getRoad(waypoints);
        }
        @Override
        protected void onPostExecute(Road result) {
            road = result;
            // showing distance and duration of the road
            Toast.makeText(getActivity(), "distance="+road.mLength, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "durée="+road.mDuration, Toast.LENGTH_LONG).show();

            if(road.mStatus != Road.STATUS_OK)
                Toast.makeText(getActivity(), "Error when loading the road - status="+road.mStatus, Toast.LENGTH_SHORT).show();
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road,getActivity());

            map.getOverlays().add(roadOverlay);
            map.invalidate();
            //updateUIWithRoad(result);
        }
    }**/


}


