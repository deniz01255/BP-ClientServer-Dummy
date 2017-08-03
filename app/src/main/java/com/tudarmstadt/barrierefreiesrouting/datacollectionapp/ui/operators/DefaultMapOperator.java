package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.OverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.DefaultNearestRoadsDirector;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.OsmParser;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.Road;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IMapOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
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

public class DefaultMapOperator implements IMapOperator {

    protected NearestRoadsOverlay roadsOverlay;
    protected MapEditorFragment mapEditorFragment;
    OverpassAPI overpassAPI = new OverpassAPI();
    private Context context;
    private ArrayList<Polyline> currentRoadOverlays = new ArrayList<>();

    public DefaultMapOperator(Context context) {

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

        this.mapEditorFragment = mapEditorFragment;
        DefaultNearestRoadsDirector roadsDirector = new DefaultNearestRoadsDirector(new NearestRoadsOverlayBuilder(context));
        roadsOverlay = roadsDirector.construct(p);

        GetHighwaysFromOverpassAPITask task = new GetHighwaysFromOverpassAPITask(context);
        AsyncTask<Object, Object, Response> execute = task.execute(roadsOverlay.center, roadsOverlay.radius);

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

                for (Road r : roadsOverlay.nearestRoads) {

                    Polyline polyline = new Polyline();
                    polyline.setPoints(r.getRoadPoints());
                    polyline.setColor(Color.RED);
                    polyline.setWidth(20);
                    polyline.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapEditorFragment.map));
                    polyline.setTitle("Polyline tapped!");
                    polyline.setOnClickListener(new Polyline.OnClickListener() {

                        @Override
                        public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {

                            return false;
                        }
                    });

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

            System.out.print(response.body());
            return;
        } else {
            System.out.print("....");

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

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), OverpassAPI.getNearestHighwaysPayload(p, radius));

            Request request = new Request.Builder()
                    .url(OverpassAPI.baseURL + OverpassAPI.stairsResource)
                    .method("POST", body)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
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
