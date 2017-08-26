package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import android.util.Log;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

/**
 * Created by deniz on 26.08.17.
 */

public class PlaceStartOfRoadOnPolyline implements Polyline.OnClickListener {
    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint geoPoint) {
        Log.d("myTag", "This is my message");
        return true;
    }
}
