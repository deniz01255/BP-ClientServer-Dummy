package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.CustomPolyline;

import org.osmdroid.util.GeoPoint;

/**
 * Created by vincent on 8/16/17.
 */

public class ObstaclePositionSelectedOnPolylineEvent {


    private GeoPoint point;
    private CustomPolyline polyline;

    public ObstaclePositionSelectedOnPolylineEvent(GeoPoint point, CustomPolyline polyline) {

        this.polyline = polyline;
        this.point = point;
    }


    public GeoPoint getPoint() {
        return point;
    }

    public CustomPolyline getPolyline() {
        return polyline;
    }
}
