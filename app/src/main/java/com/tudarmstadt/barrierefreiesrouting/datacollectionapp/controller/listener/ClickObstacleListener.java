package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by deniz on 20.08.17.
 */

public class ClickObstacleListener implements Marker.OnMarkerClickListener {
    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        marker.setTitle("Gewechselt");
        return true;
    }
}
