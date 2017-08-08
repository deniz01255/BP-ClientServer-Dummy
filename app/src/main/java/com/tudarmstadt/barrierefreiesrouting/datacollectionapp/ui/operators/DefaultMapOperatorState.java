package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate.StateHandler;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.MainOverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RamplerOverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.DefaultNearestRoadsDirector;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.OsmParser;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Vincent on 31.07.2017.
 */
public class DefaultMapOperatorState implements IOperatorState {

    protected NearestRoadsOverlay roadsOverlay;
    protected MapEditorFragment mapEditorFragment;
    MainOverpassAPI overpassAPI = new MainOverpassAPI();
    private MainActivity mainActivity;
    private Context context;
    private ArrayList<Polyline> currentRoadOverlays = new ArrayList<>();
    private PlaceBarrierOnOverlayOperatorState placeBarrierOnOverlayOperatorState;

    public DefaultMapOperatorState(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        // Prepare the next state
        placeBarrierOnOverlayOperatorState = new PlaceBarrierOnOverlayOperatorState(mainActivity, mapEditorFragment);
        this.context = context;
    }

    /**
     * This will be called from the StateHandler
     * @return
     */
    @Override
    public boolean init() {

        return true;
    }

    /**
     * This will be called from the StateHandler
     * @return
     */
    @Override
    public boolean dispose() {

        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p, MainActivity mainActivity, MapEditorFragment mapEditorFragment) {

        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        this.mapEditorFragment = mapEditorFragment;
        DefaultNearestRoadsDirector roadsDirector = new DefaultNearestRoadsDirector(new NearestRoadsOverlayBuilder());
        roadsOverlay = roadsDirector.construct(p);

        GetHighwaysFromOverpassAPITask task = new GetHighwaysFromOverpassAPITask(mainActivity);
        task.execute(roadsOverlay.center, roadsOverlay.radius);

        mainActivity.getStateHandler().setupNextState(placeBarrierOnOverlayOperatorState);

        return true;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment) {

        return true;
    }

    protected void processRoads(Response response) {
        if (response != null && response.isSuccessful()) {
            mapEditorFragment.map.getOverlays().removeAll(currentRoadOverlays);
            currentRoadOverlays.clear();
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                OsmParser parser = new OsmParser();
                String ss = response.body().string();
                InputSource source = new InputSource(new StringReader(ss));

                saxParser.parse(source, parser);

                roadsOverlay.nearestRoads = parser.getRoads();

                if (roadsOverlay.nearestRoads.isEmpty() || roadsOverlay.nearestRoads.getFirst().getRoadPoints().isEmpty())
                    return;

                for (Road r : roadsOverlay.nearestRoads) {

                    Polyline polyline = new Polyline();
                    polyline.setPoints(r.getRoadPoints());
                    polyline.setColor(Color.BLACK);
                    polyline.setWidth(18);

                    polyline.setOnClickListener(placeBarrierOnOverlayOperatorState);

                    currentRoadOverlays.add(polyline);

                }
                for (Polyline p : currentRoadOverlays) {
                    mapEditorFragment.map.getOverlays().add(p);

                }
                mapEditorFragment.map.invalidate();

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            View contextView = mapEditorFragment.getActivity().findViewById(R.id.placeSnackBar);
            Snackbar.make(contextView, R.string.server_response_roads_loaded, Snackbar.LENGTH_SHORT)
                    .show();
            return;
        } else {
            return;
        }
    }

    class GetHighwaysFromOverpassAPITask extends AsyncTask<Object, Object, Response> {
        Intent myIntent = new Intent(context, MainActivity.class);
        ProgressDialog progressDialog;
        private MainActivity activity;

        GetHighwaysFromOverpassAPITask(MainActivity activity) {

            this.activity = activity;
            progressDialog = new ProgressDialog(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Lade Straßen in der nähe..");
            this.progressDialog.show();
        }
        @Override
        protected Response doInBackground(Object... params) {

            GeoPoint p = (GeoPoint) params[0];
            int radius = (int) params[1];
            DownloadObstaclesTask task = new DownloadObstaclesTask();
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), RamplerOverpassAPI.getNearestHighwaysPayload(p, radius));

            Request request = new Request.Builder()
                    .url(RamplerOverpassAPI.baseURL + RamplerOverpassAPI.stairsResource)
                    .method("POST", body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (!response.isSuccessful()) {
                //TODO: handle unsuccessful server responses
                System.out.print("This line exists only for break pointability ;P ... Server responded not successfully");
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            processRoads(result);

        }
    }

}
