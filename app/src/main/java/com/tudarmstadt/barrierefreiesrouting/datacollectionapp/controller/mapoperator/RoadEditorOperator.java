package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.ClickObstacleListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener.DragObstacleListener;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;
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
    List<Road> RoadList = new ArrayList<>();

    // Longpress auf die Map
    @Override
    public boolean longPressHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {
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

        streetLine.setPoints(list);
        streetLine.setGeodesic(true);
        streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapEditorFragment.map));

        return streetLine;
    }
}
