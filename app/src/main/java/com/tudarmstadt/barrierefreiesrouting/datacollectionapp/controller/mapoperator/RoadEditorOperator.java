package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;


import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.PathOverlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;


import java.lang.reflect.Array;
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
        streetLine.setTitle("Start for ");
        streetLine.setWidth(10f);
        //here, we create a polygon, note that you need 5 points in order to make a closed polygon (rectangle)
        roadEndPoints.add(new GeoPoint(p.getLatitude(), p.getLongitude()));
        roadEndPoints.add(new GeoPoint(p.getLatitude(), p.getLongitude()+0.0002));
        roadEndPoints.add(new GeoPoint(p.getLatitude()+0.0002, p.getLongitude()));
        streetLine.setPoints(roadEndPoints);
        streetLine.setGeodesic(true);
        streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapEditorFragment.map));
        Marker startMarker = new Marker(mapEditorFragment.map);
        startMarker.setPosition(roadEndPoints.get(0));
        startMarker.setTitle("Start point for creating new Road");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        RoadMarker.add(startMarker);
        mapEditorFragment.map.getOverlays().add(startMarker);
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
        mapEditorFragment.map.getOverlayManager().add(streetLine);
        mapEditorFragment.map.invalidate();



        return true;
    }


    // Single Tap auf die Map
    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {
        if(roadEndPoints.size() > 0) {
            List<GeoPoint> roadEndPointsCrob = new ArrayList<>();
            Polyline streetLine = new Polyline(context);
            streetLine.setTitle("added Line");
            streetLine.setWidth(10f);

            roadEndPoints.add(p);

            roadEndPointsCrob.add(roadEndPoints.get(roadEndPoints.size() - 2));
            roadEndPointsCrob.add(p);

            streetLine.setPoints(roadEndPointsCrob);
            streetLine.setGeodesic(true);
            streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, mapEditorFragment.map));

            Marker end = new Marker(mapEditorFragment.map);
            end.setPosition(p);
            end.setTitle("endPunkt");
            end.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            RoadMarker.add(end);

            mapEditorFragment.map.getOverlays().add(end);
            mapEditorFragment.map.getOverlayManager().add(streetLine);
            mapEditorFragment.map.invalidate();


            return true;
        }
        else{
           return false;
        }
    }
}
