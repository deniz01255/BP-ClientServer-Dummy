package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoadsHelperOverlayChangedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.ClickObstacleListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.DragObstacleListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.PlaceObstacleOnPolygonListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.PlaceStartOfRoadOnPolyline;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RamplerOverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.DefaultNearestRoadsDirector;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.OsmParser;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.CustomPolyline;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;


import org.greenrobot.eventbus.EventBus;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by vincent on 8/16/17.
 */
public class RoadEditorOperator implements IUserInteractionWithMap {

    private NearestRoadsOverlay roadsOverlay;
    List<GeoPoint> roadEndPoints = new ArrayList<>();
    List<Marker>  RoadMarker = new ArrayList<>();
    List<Road> RoadList = new ArrayList<>();
    List<List<Polyline>> colorCapture = new ArrayList<>();
    List<Polyline> currentRoadCapture = new ArrayList<>();

    // Longpress auf die Map
    @Override
    public boolean longPressHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {
        DefaultNearestRoadsDirector roadsDirector = new DefaultNearestRoadsDirector(new NearestRoadsOverlayBuilder());
        roadsOverlay = roadsDirector.construct(p);
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        GetHighwaysFromOverpassAPITask task = new GetHighwaysFromOverpassAPITask(context);
        task.execute(roadsOverlay.center, roadsOverlay.radius);

        if (currentRoadCapture.size() != 0 ){
            colorCapture.add(currentRoadCapture);

        }
        if(colorCapture.size() != 0){
            for (Polyline poly: colorCapture.get(colorCapture.size()-1)) {
                poly.setColor(Color.BLACK);
                mapEditorFragment.map.getOverlayManager().add(poly);
            }
            mapEditorFragment.map.invalidate();
            currentRoadCapture.clear();
        }

        roadEndPoints.clear();
        Road newStreet =  new Road();

        if (RoadList.size() != 0 )
            {
                 newStreet.id =  RoadList.get(RoadList.size()-1).id + 1;
            }
        else{
            newStreet.id = 0;
        }
        newStreet.name = "Street: "+ newStreet.id;



        Polyline streetLine = new Polyline(context);
        //here, we create a polygon, note that you need 5 points in order to make a closed polygon (rectangle)

        roadEndPoints.add(new GeoPoint(p.getLatitude(), p.getLongitude()));
        roadEndPoints.add(new GeoPoint(p.getLatitude(), p.getLongitude()+0.0002));
        roadEndPoints.add(new GeoPoint(p.getLatitude()+0.0002, p.getLongitude()));
        newStreet.setROADList(roadEndPoints);

        streetLine = setUPPoly(streetLine, mapEditorFragment,roadEndPoints);

        Marker startMarker = new Marker(mapEditorFragment.map);
        startMarker.setPosition(roadEndPoints.get(0));
        startMarker.setTitle("Start point for creating new Road");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        RoadMarker.add(startMarker);

        addMapOverlay(startMarker, streetLine, mapEditorFragment);


        RoadList.add(newStreet);
        return true;
    }


    // Single Tap auf die Map
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, Activity context, final MapEditorFragment mapEditorFragment) {
        if(roadEndPoints.size() > 0) {
            List<GeoPoint> roadEndPointsCrob = new ArrayList<>();
            Polyline streetLine = new Polyline(context);


            roadEndPoints.add(p);
            RoadList.get(RoadList.size()-1).setROADList(roadEndPoints);

            roadEndPointsCrob.add(roadEndPoints.get(roadEndPoints.size() - 2));
            roadEndPointsCrob.add(p);
            streetLine = setUPPoly(streetLine, mapEditorFragment,roadEndPointsCrob);

            Marker end = new Marker(mapEditorFragment.map);
            end.setPosition(p);
            end.setTitle("endPunkt");
            end.setDraggable(true);
            end.isDraggable();
            end.setOnMarkerDragListener(new DragObstacleListener(mapEditorFragment,roadEndPoints,this,context));


            end.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            RoadMarker.add(end);


            addMapOverlay(end, streetLine, mapEditorFragment);


            return true;
        }
        else{
           return false;
        }
    }

    public void addMapOverlay(Marker marker, Polyline polyline, MapEditorFragment mapEditorFragment){
        mapEditorFragment.map.getOverlays().add(marker);
        mapEditorFragment.map.getOverlayManager().add(polyline);
        mapEditorFragment.map.invalidate();
    }

    public Polyline setUPPoly(Polyline streetLine, MapEditorFragment mapEditorFragment, List<GeoPoint> list){

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
     * @param response
     */
    protected void processRoads(Response response) {
        if (response != null && response.isSuccessful()) {

            try {
                ArrayList<Polyline> polylines = new ArrayList<>();

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

                    CustomPolyline polyline = new CustomPolyline();
                    polyline.setRoad(r);
                    polyline.setPoints(r.getRoadPoints());
                    polyline.setColor(Color.BLACK);
                    polyline.setWidth(18);
                    // See onClick() method in this class.
                    polyline.setOnClickListener(new PlaceStartOfRoadOnPolyline());
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

        GetHighwaysFromOverpassAPITask(Activity activity ) {

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
            processRoads(result);

        }
    }

}
