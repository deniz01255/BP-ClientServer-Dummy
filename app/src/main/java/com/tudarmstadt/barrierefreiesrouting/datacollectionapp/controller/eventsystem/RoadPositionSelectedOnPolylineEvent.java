package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import org.osmdroid.util.GeoPoint;

/**
 * Created by deniz on 05.09.17.
 */

public class RoadPositionSelectedOnPolylineEvent {
    private GeoPoint point;

    public RoadPositionSelectedOnPolylineEvent(GeoPoint point) {


        this.point = point;
    }


    public GeoPoint getPoint() {
        return point;
    }
}
