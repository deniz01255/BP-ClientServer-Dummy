package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by deniz on 20.08.17.
 */

public class DragObstacleListener implements Marker.OnMarkerDragListener{
    @Override
    public void onMarkerDrag(Marker marker) {
        marker.setPosition(new GeoPoint(marker.getPosition().getLatitude()+0.00015,marker.getPosition().getLongitude()));

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        GeoPoint geopoint = marker.getPosition();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }
}


