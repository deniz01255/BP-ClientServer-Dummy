package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Created by vincent on 8/16/17.
 */

public class ObstaclePositionSelectedOnPolylineEvent {


    private GeoPoint point;

    public ObstaclePositionSelectedOnPolylineEvent(GeoPoint point) {


        this.point = point;
    }


    public GeoPoint getPoint() {
        return point;
    }
}
