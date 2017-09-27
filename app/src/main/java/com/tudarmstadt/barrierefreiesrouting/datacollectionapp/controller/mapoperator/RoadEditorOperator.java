package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoadsHelperOverlayChangedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.DragObstacleListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.PlaceStartOfRoadOnPolyline;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RamplerOverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.DefaultNearestRoadsDirector;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.OsmParser;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.CustomPolyline;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ParcedOverpassRoad;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import bp.common.model.ways.Node;
import bp.common.model.ways.Way;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vincent on 8/16/17.
 */
public class RoadEditorOperator implements IUserInteractionWithMap {

    public NearestRoadsOverlay roadsOverlay;
    public List<GeoPoint> roadEndPoints = new ArrayList<>();
    public List<ParcedOverpassRoad> RoadList = new ArrayList<>();
    public List<List<Polyline>> colorCapture = new ArrayList<>();
    public List<Marker> RoadMarker = new ArrayList<>();
    public List<Polyline> currentRoadCapture = new ArrayList<>();
    public GetHighwaysFromOverpassAPITask task;
    public GetHighwaysFromCustomServerTask task2;


    // Longpress auf die Map
    @Override
    public boolean longPressHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {
        ParcedOverpassRoad newStreet = new ParcedOverpassRoad();
        if (RoadList.size() != 0) {
            newStreet.id = RoadList.get(RoadList.size() - 1).id + 1;
        } else {
            newStreet.id = 0;
        }


/** https://routing.vincinator.de/api/barriers/ways/radius?lat1=49.873090&long1=8.659275&radius=100**/

        newStreet.name = "Street: " + newStreet.id;

        RoadList.add(newStreet);

        DefaultNearestRoadsDirector roadsDirector = new DefaultNearestRoadsDirector(new NearestRoadsOverlayBuilder());
        roadsOverlay = roadsDirector.construct(p);
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        task2 = new GetHighwaysFromCustomServerTask(context);
        task2.execute(roadsOverlay.center, roadsOverlay.radius);

        task = new GetHighwaysFromOverpassAPITask(context);
        task.execute(roadsOverlay.center, roadsOverlay.radius);

        int i = 0;

        if (RoadList.size() != 0) {
            for (ParcedOverpassRoad road : RoadList) {
                for (Polyline polyline : road.polylines) {
                    polyline.setColor(Color.BLACK);
                    mapEditorFragment.map.getOverlayManager().add(polyline);
                }
            }
            //mapEditorFragment.map.invalidate();
            currentRoadCapture.clear();
        }
        
        return true;
    }


    // Single Tap auf die Map
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint, Activity context, final MapEditorFragment mapEditorFragment) {
        if (RoadList.size() == 0) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setTitle("No existing road!");
            builder1.setMessage("Please verify that you first do a LONGPRESS to create a road \n\n" +
                    "Single tap only works if a road exists, adding further nodes.");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();


            return false;
        }
        List<GeoPoint> gp = new ArrayList<GeoPoint>();
        List<Overlay> xx = mapEditorFragment.map.getOverlays();
        ParcedOverpassRoad road = RoadList.get(RoadList.size() - 1);
        road.polylines.add((Polyline) xx.get(xx.size() - 1));
        road.polylines.add((Polyline) xx.get(xx.size() - 3));

        Marker x = (Marker) xx.get(xx.size() - 4);
        gp.add(x.getPosition());
        x = (Marker) xx.get(xx.size() - 2);
        gp.add(x.getPosition());
        road.setROADList(gp);


        if (road.getRoadPoints().size() > 0) {

            List<GeoPoint> roadEndPointsCrob = new ArrayList<>();
            Polyline streetLine = new Polyline(context);


            road.setRoadPoints(geoPoint);


            roadEndPointsCrob.add(road.getRoadPoints().get(road.getRoadPoints().size() - 2));
            roadEndPointsCrob.add(geoPoint);
            streetLine = setUPPoly(streetLine, mapEditorFragment, roadEndPointsCrob);

            Marker end = new Marker(mapEditorFragment.map);
            end.setPosition(geoPoint);
            end.setTitle("endPunkt");
            end.setDraggable(true);
            end.isDraggable();
            end.setOnMarkerDragListener(new DragObstacleListener(road, mapEditorFragment, roadEndPoints, this, context));

            List<Overlay> xxxx = mapEditorFragment.map.getOverlays();
            Overlay ov = mapEditorFragment.map.getOverlays().get(mapEditorFragment.map.getOverlays().size()-2);
            Marker mark = (Marker)ov;
            mark.setDraggable(false);

            end.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            RoadMarker.add(end);


            addMapOverlay(end, streetLine, mapEditorFragment);

            mapEditorFragment.map.invalidate();
            return true;
        } else {
            mapEditorFragment.map.invalidate();
            return false;
        }

    }

    public void addMapOverlay(Marker marker, Polyline polyline, MapEditorFragment mapEditorFragment) {
        mapEditorFragment.map.getOverlays().add(marker);
        mapEditorFragment.map.getOverlayManager().add(polyline);
        // mapEditorFragment.map.invalidate();
    }

    public Polyline setUPPoly(Polyline streetLine, MapEditorFragment mapEditorFragment, List<GeoPoint> list) {

        streetLine.setTitle("Text param");
        streetLine.setWidth(10f);
        streetLine.setColor(Color.BLUE);
        streetLine.setPoints(list);
        streetLine.setGeodesic(true);
        streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapEditorFragment.map));

        currentRoadCapture.add(streetLine);

        return streetLine;
    }


    /**
     * render the roads found near a chosen point as Polyline
     * and give this an Eventlistener so when touched a barrier will be added to the map
     *
     * @param response
     */
    protected void processRoads(Response response, Context context) {
        ArrayList<PlaceStartOfRoadOnPolyline> list = new ArrayList<>();
        if (response != null && response.isSuccessful()) {

            try {
                ArrayList<Polyline> polylines = new ArrayList<>();

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                OsmParser parser = new OsmParser();
                String ss = response.body().string();
                InputSource source = new InputSource(new StringReader(ss));

                final ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


                final List<Way> wayList = mapper.readValue(ss, new TypeReference<List<Way>>() {
                });


                for (Way w : wayList) {
                    List<GeoPoint> node = new ArrayList<>();
                    ParcedOverpassRoad r = new ParcedOverpassRoad();

                    for (Node n : w.getNodes()) {
                        GeoPoint g = new GeoPoint(n.getLatitude(), n.getLongitude());
                        node.add(g);

                    }
                    r.setROADList(node);
                    /**
                     CustomPolyline polyline = new CustomPolyline();
                     polyline.setPoints(node);
                     polyline.setColor(Color.GREEN);
                     polyline.setWidth(18);
                     // See onClick() method in this class.
                     polyline.setOnClickListener(new PlaceStartOfRoadOnPolyline(context));
                     polylines.add(polyline);
                     **/

                    roadsOverlay.nearestRoads.add(r);
                }

                EventBus.getDefault().post(new RoadsHelperOverlayChangedEvent(polylines));

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /* render the roads found near a chosen point as Polyline
    * and give this an Eventlistener so when touched a barrier will be added to the map
    * @param response
    */
    protected void processWays(Response response, Context context) {
        if (response != null && response.isSuccessful()) {

            try {
                ArrayList<Polyline> polylines = new ArrayList<>();

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();

                OsmParser parser = new OsmParser();
                String ss = response.body().string();
                InputSource source = new InputSource(new StringReader(ss));

                saxParser.parse(source, parser);

                List<ParcedOverpassRoad> give = roadsOverlay.nearestRoads;

                roadsOverlay.nearestRoads = parser.getRoads();
                for (ParcedOverpassRoad r : give) {
                    roadsOverlay.nearestRoads.add(r);
                }

                if (roadsOverlay.nearestRoads.isEmpty() || roadsOverlay.nearestRoads.getFirst().getRoadPoints().isEmpty())
                    return;

                ArrayList<PlaceStartOfRoadOnPolyline> pl = new ArrayList<>();

                for (ParcedOverpassRoad r : roadsOverlay.nearestRoads) {

                    CustomPolyline polyline = new CustomPolyline();
                    polyline.setRoad(r);
                    polyline.setPoints(r.getRoadPoints());
                    polyline.setColor(Color.BLACK);
                    polyline.setWidth(18);
                    // See onClick() method in this class.
                    PlaceStartOfRoadOnPolyline pla = new PlaceStartOfRoadOnPolyline(context, pl);
                    pl.add(pla);
                    polyline.setOnClickListener(pla);
                    polylines.add(polyline);
                }

                EventBus.getDefault().post(new RoadsHelperOverlayChangedEvent(polylines));

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * A Asynctask Class to get the osm data from a certain area while longpressed
     * This is used to find the nearest roads to a chosen point on the map
     */
    private class GetHighwaysFromOverpassAPITask extends AsyncTask<Object, Object, Response> {
        ProgressDialog progressDialog;

        GetHighwaysFromOverpassAPITask(Activity activity) {

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
            }


            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            processWays(result, progressDialog.getContext());


        }
    }


    /**
     * A Asynctask Class to get the osm data from a certain area while longpressed
     * This is used to find the nearest roads to a chosen point on the map
     */
    public class GetHighwaysFromCustomServerTask extends AsyncTask<Object, Object, Response> {
        ProgressDialog progressDialog;

        GetHighwaysFromCustomServerTask(Activity activity) {

            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Lade Custom Straßen in der nähe..");
            this.progressDialog.show();
        }

        @Override
        protected Response doInBackground(Object... params) {

            GeoPoint p = (GeoPoint) params[0];
            int radius = (int) params[1];
            // DownloadObstaclesTask task = new DownloadObstaclesTask();
            OkHttpClient client = new OkHttpClient();

            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), RamplerOverpassAPI.getNearestHighwaysPayload(p, radius));

            Request request = new Request.Builder()
                    .url("https://routing.vincinator.de/api/barriers/ways/radius?lat1=" + p.getLatitude() + "&long1=" + p.getLongitude() + "&radius=" + radius)
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
            }


            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            processRoads(result, progressDialog.getContext());


        }
    }

}
