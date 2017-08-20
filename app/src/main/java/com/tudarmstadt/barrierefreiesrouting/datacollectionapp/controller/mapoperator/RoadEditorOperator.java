package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.ClickObstacleListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincent on 8/16/17.
 */
public class RoadEditorOperator implements IUserInteractionWithMap {


    List<GeoPoint> roadEndPoints = new ArrayList<>();
    List<Marker>  RoadMarker = new ArrayList<>();

    // Longpress auf die Map
    @Override
    public boolean longPressHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {





        Polyline streetLine = new Polyline(context);
        //here, we create a polygon, note that you need 5 points in order to make a closed polygon (rectangle)

        roadEndPoints.add(new GeoPoint(p.getLatitude(), p.getLongitude()));
        roadEndPoints.add(new GeoPoint(p.getLatitude(), p.getLongitude()+0.0002));
        roadEndPoints.add(new GeoPoint(p.getLatitude()+0.0002, p.getLongitude()));
        streetLine = setUPPoly(streetLine, mapEditorFragment,roadEndPoints);

        Marker startMarker = new Marker(mapEditorFragment.map);
        startMarker.setPosition(roadEndPoints.get(0));
        startMarker.setTitle("Start point for creating new Road");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        RoadMarker.add(startMarker);

        //Note, the info window will not show if you set the onclick listener
        //line can also attach click listeners to the line
        /*
        line.setOnClickListener(new Polyline.OnClickListener() {
            @Override
            public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
                Toast.makeText(context, "Hello world!", Toast.LENGTH_LONG).show();
                return false;
            }
        });*/
        addMapOverlay(startMarker, streetLine, mapEditorFragment);



        return true;
    }


    // Single Tap auf die Map
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, Activity context, final MapEditorFragment mapEditorFragment) {
        if(roadEndPoints.size() > 0) {
            List<GeoPoint> roadEndPointsCrob = new ArrayList<>();
            Polyline streetLine = new Polyline(context);

            roadEndPoints.add(p);
            roadEndPointsCrob.add(roadEndPoints.get(roadEndPoints.size() - 2));
            roadEndPointsCrob.add(p);
            streetLine = setUPPoly(streetLine, mapEditorFragment,roadEndPointsCrob);

            Marker end = new Marker(mapEditorFragment.map);
            end.setPosition(p);
            end.setTitle("endPunkt");
            end.setDraggable(true);
            end.isDraggable();
            end.setOnMarkerClickListener(new ClickObstacleListener());

            /**end.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                //LatLng temp = null;
                GeoPoint geopoint = null;
                @Override
                public void onMarkerDragStart(Marker marker) {
                    // TODO Auto-generated method stub
                    geopoint = marker.getPosition();
                    //temp=marker.getPosition();
                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    // TODO Auto-generated method stub
                    marker.setPosition(geopoint);
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    // TODO Auto-generated method stub
                    //LatLng temp = marker.getPosition();
                   GeoPoint geopoint = marker.getPosition();
                }
            });
**/

            /*end.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    GeoPoint geopoint = marker.getPosition();
                }

                @Override
                public void onMarkerDrag(Marker marker) {
                    marker.setPosition(new GeoPoint(marker.getPosition().getLatitude()+0.00015,marker.getPosition().getLongitude()));
                    mapEditorFragment.map.invalidate();

                }
            });*/
            end.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            RoadMarker.add(end);


            addMapOverlay(end, streetLine, mapEditorFragment);


            return true;
        }
        else{
           return false;
        }
    }

    private void addMapOverlay(Marker marker, Polyline polyline, MapEditorFragment mapEditorFragment){
        mapEditorFragment.map.getOverlays().add(marker);
        mapEditorFragment.map.getOverlayManager().add(polyline);
        mapEditorFragment.map.invalidate();
    }

    private Polyline setUPPoly(Polyline streetLine, MapEditorFragment mapEditorFragment, List<GeoPoint> list){

        streetLine.setTitle("Text param");
        streetLine.setWidth(10f);

        streetLine.setPoints(list);
        streetLine.setGeodesic(true);
        streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapEditorFragment.map));

        return streetLine;
    }
}
