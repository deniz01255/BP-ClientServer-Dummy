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
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.CloseToRoad.CloseToRoadChecker;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.PostObstacleToServerTask;

import bp.common.model.Stairs;

public class MapEditorFragment extends Fragment implements MapEventsReceiver {

    public MapEditorFragment mapEditorF;
    public Activity activMAP;
    public GraphHopperRoadManager graphHopperRoadManager;

    public MyLocationNewOverlay mLocationOverlay;
    public MapView map;
    private MapEventsOverlay evOverlay;
    private RoadManager roadManager;

    public ArrayList<GeoPoint> waypoints;

    public Road road;

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



        // RoadManager roadManager = new MapQuestRoadManager("P9eWLsqG8k7C30Gcl2jzeAqHByyl5bZz");

        GeoPoint startPoint = new GeoPoint( 49.8705556,8.6494444);
        GeoPoint endPoint = new GeoPoint(49.873163174 , 8.653830718);

        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(endPoint);
        CloseToRoadChecker.DownloadStairs(getActivity(),p,this);

        new UpdateRoadTask().execute(waypoints,this,getActivity());

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
    private class UpdateRoadTask extends AsyncTask<Object, Void, Road> {


        protected Road doInBackground(Object... params) {
            @SuppressWarnings("unchecked")
            MapEditorFragment SmapEditorF = (MapEditorFragment)params[1];
            mapEditorF = SmapEditorF;
            Activity SactivMAP = (Activity)params[2];
            activMAP = SactivMAP;
            waypoints = (ArrayList<GeoPoint>)params[0];
           //roadManager = new MapQuestRoadManager("P9eWLsqG8k7C30Gcl2jzeAqHByyl5bZz");
            //roadManager.addRequestOption("routeType=pedestrian");
            graphHopperRoadManager = new GraphHopperRoadManager("3eaff35e-11cf-437f-b17b-570ae07759fc",true);

            graphHopperRoadManager.addRequestOption("vehicle=");

            return graphHopperRoadManager.getRoad(waypoints);
        }
        @Override
        protected void onPostExecute(Road result) {
            road = result;
            // showing distance and duration of the road
            Toast.makeText(getActivity(), "distance="+road.mLength, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(), "durée="+road.mDuration, Toast.LENGTH_LONG).show();

            if(road.mStatus != Road.STATUS_OK) {
                Toast.makeText(getActivity(), "Error when loading the road - status=" + road.mStatus, Toast.LENGTH_SHORT).show();
            }


            for (int i=0; i<road.mNodes.size(); i++){
                final Stairs newObstacle = new Stairs("Chabos wissen wo die Treppe steht", road.mNodes.get(i).mLocation.getLongitude(), road.mNodes.get(i).mLocation.getLatitude(), 10, 10, false) ;

                PostObstacleToServerTask.PostStairs(getActivity(), mapEditorF, newObstacle);
            }

            for (int i=0; i<road.mRouteHigh.size(); i++){
                final Stairs newObstacle = new Stairs("Chabos wissen wo die Treppe steht", road.mRouteHigh.get(i).getLongitude(), road.mRouteHigh.get(i).getLatitude(), 10, 10, false) ;

                PostObstacleToServerTask.PostStairs(getActivity(), mapEditorF, newObstacle);



            }




            activMAP.runOnUiThread(new Runnable() {

                @Override
                public void run()
                {


                    Road road = graphHopperRoadManager.getRoad(waypoints);

                    Polyline roadOverlay = GraphHopperRoadManager.buildRoadOverlay(road,0x800000FF,15.0f);
                    map.getOverlays().add(roadOverlay);
                    map.invalidate();
                }
            });
            //updateUIWithRoad(result);
        }
    }


}
