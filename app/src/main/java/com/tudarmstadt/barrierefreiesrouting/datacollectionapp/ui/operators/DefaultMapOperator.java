package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.widget.Toast;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IMapOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.CloseToRoad.CloseToRoadChecker;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostObstacleToServerTask;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import bp.common.model.Stairs;

/**
 * Created by Vincent on 31.07.2017.
 */

public class DefaultMapOperator implements IMapOperator {


    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean dispose() {
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p, Activity activity, MapEditorFragment mapEditorFragment) {

        // TODO: refactor this code
        // RoadManager roadManager = new MapQuestRoadManager("P9eWLsqG8k7C30Gcl2jzeAqHByyl5bZz");

        // TODO: highlight the correct Road!!
        GeoPoint startPoint = new GeoPoint( 49.87683721424917,8.653078681454645);
        GeoPoint endPoint = new GeoPoint(49.87547201057107, 8.653020858764648);

        ArrayList<GeoPoint> roadPoints = new ArrayList<GeoPoint>();
        roadPoints.add(startPoint);
        roadPoints.add(endPoint);
        CloseToRoadChecker.CloseToRoadChecker(activity,p,mapEditorFragment);

        new UpdateRoadTask().execute(roadPoints,mapEditorFragment,activity);
        return true;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, Activity activity, MapEditorFragment mapEditorFragment) {


        return true;
    }

    /**
     * Async task to get the road in a separate thread.
     */
    public class UpdateRoadTask extends AsyncTask<Object, Void, Road> {
        public ArrayList<GeoPoint> waypoints;
        public GraphHopperRoadManager graphHopperRoadManager;
        public Road road;
        private Activity activity;
        public MapEditorFragment mapEditorFragment;

        protected Road doInBackground(Object... params) {
            waypoints = (ArrayList<GeoPoint>)params[0];
            activity = (Activity)params[2];
            mapEditorFragment = (MapEditorFragment)params[1];

            //roadManager = new MapQuestRoadManager("P9eWLsqG8k7C30Gcl2jzeAqHByyl5bZz");
            //roadManager.addRequestOption("routeType=pedestrian");
            graphHopperRoadManager = new GraphHopperRoadManager("3eaff35e-11cf-437f-b17b-570ae07759fc",true);

            graphHopperRoadManager.addRequestOption("vehicle=foot");

            return graphHopperRoadManager.getRoad(waypoints);
        }
        @Override
        protected void onPostExecute(Road result) {
            road = result;

            if(road.mStatus != Road.STATUS_OK) {
                Toast.makeText(activity, "Error when loading the road - status=" + road.mStatus, Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i=0; i<road.mNodes.size(); i++){
                final Stairs newObstacle = new Stairs(activity.getString(R.string.default_description), road.mNodes.get(i).mLocation.getLongitude(), road.mNodes.get(i).mLocation.getLatitude(), 10, 10, false) ;
                PostObstacleToServerTask.PostStairs(activity, mapEditorFragment, newObstacle);
            }

            for (int i=0; i<road.mRouteHigh.size(); i++){
                final Stairs newObstacle = new Stairs(activity.getString(R.string.default_description), road.mRouteHigh.get(i).getLongitude(), road.mRouteHigh.get(i).getLatitude(), 10, 10, false) ;
                PostObstacleToServerTask.PostStairs(activity, mapEditorFragment, newObstacle);
            }

            activity.runOnUiThread(new Runnable() {

                @Override
                public void run()
                {
                    Road road = graphHopperRoadManager.getRoad(waypoints);

                    Polyline roadOverlay = GraphHopperRoadManager.buildRoadOverlay(road,0x800000FF,15.0f);
                    mapEditorFragment.map.getOverlays().add(roadOverlay);
                    mapEditorFragment.map.invalidate();
                }
            });
            //updateUIWithRoad(result);
        }
    }

}
