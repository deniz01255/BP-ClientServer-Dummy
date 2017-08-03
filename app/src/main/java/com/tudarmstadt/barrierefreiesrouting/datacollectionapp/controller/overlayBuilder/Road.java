package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Created by Vincent on 03.08.2017.
 */

public class Road {

    public long id;
    /**
     * The name of the road to display in the details view of the road.
     */
    public String name = "has no name";
    private ArrayList<GeoPoint> roadPoints = new ArrayList<GeoPoint>();

    /**
     * All GeoPoints that form the Road.
     */
    public ArrayList<GeoPoint> getRoadPoints() {
        return roadPoints;
    }

    public void setRoadPoints(ArrayList<GeoPoint> roadPoints) {
        this.roadPoints = roadPoints;
    }

}
