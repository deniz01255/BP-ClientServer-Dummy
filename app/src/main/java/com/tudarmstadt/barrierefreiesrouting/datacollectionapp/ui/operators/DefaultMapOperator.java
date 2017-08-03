package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.OverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.DefaultNearestRoadsDirector;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IMapOperator;

import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Created by Vincent on 31.07.2017.
 */

public class DefaultMapOperator implements IMapOperator {

    OverpassAPI overpassAPI = new OverpassAPI();
    private Context context;

    public DefaultMapOperator(Context context){

        this.context = context;
    }


    @Override
    public boolean init() {
        return true;
    }

    @Override
    public boolean dispose() {
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment) {


        DefaultNearestRoadsDirector roadsDirector = new DefaultNearestRoadsDirector(new NearestRoadsOverlayBuilder( context));

        NearestRoadsOverlay nearestRoadsOverlay = roadsDirector.construct(p);

        return true;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment) {


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


            return graphHopperRoadManager.getRoad(waypoints);
        }
        @Override
        protected void onPostExecute(Road result) {
            road = result;
            if(road.mStatus != Road.STATUS_OK) {
                Toast.makeText(activity, "Error when loading the road - status=" + road.mStatus, Toast.LENGTH_SHORT).show();
                return;
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
        }
    }

}
