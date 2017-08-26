package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deniz on 26.08.17.
 */

public class PlaceStartOfRoadOnPolyline implements Polyline.OnClickListener {
    List<GeoPoint> roadEndPoints = new ArrayList<>();
    Road newStreet =  new Road();
    List<Marker>  RoadMarker = new ArrayList<>();
    List<Road> RoadList = new ArrayList<>();
    MapEditorFragment mapEditorFragment;
    Context context;
    List<Polyline> currentRoadCapture = new ArrayList<>();

    public PlaceStartOfRoadOnPolyline( Context context){
        /**this.mapEditorFragment = mapEditorFragment;**/
        this.context = context;
    }


    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint geoPoint) {
        Polyline streetLine = new Polyline(context);
        roadEndPoints.add(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
        roadEndPoints.add(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()+0.0002));
        roadEndPoints.add(new GeoPoint(geoPoint.getLatitude()+0.0002, geoPoint.getLongitude()));
        newStreet.setROADList(roadEndPoints);

        streetLine = setUPPoly(streetLine, mapView,roadEndPoints);

        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(roadEndPoints.get(0));
        startMarker.setTitle("Start point for creating new Road");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        RoadMarker.add(startMarker);

        addMapOverlay(startMarker, streetLine, mapView);


        RoadList.add(newStreet);

        Log.d("myTag", "This is my message");
        return true;
    }


    public void addMapOverlay(Marker marker, Polyline polyline, MapView map){
        map.getOverlays().add(marker);
        map.getOverlayManager().add(polyline);
        map.invalidate();
    }

    public Polyline setUPPoly(Polyline streetLine, MapView map, List<GeoPoint> list){

        streetLine.setTitle("Text param");
        streetLine.setWidth(10f);
        streetLine.setColor(Color.BLUE);
        streetLine.setPoints(list);
        streetLine.setGeodesic(true);
        streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));

        currentRoadCapture.add(streetLine);

        return streetLine;
    }


}
