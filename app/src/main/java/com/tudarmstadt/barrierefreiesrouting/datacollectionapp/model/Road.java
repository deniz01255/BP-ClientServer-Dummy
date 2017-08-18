package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

/**
 * Used for parsing the response from overpass api.
 *
 * Used by OsmParser
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

}
