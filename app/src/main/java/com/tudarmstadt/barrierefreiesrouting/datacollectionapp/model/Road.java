package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.util.GeoPoint;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
     * list of node instead of GeoPoint
     */
    private ArrayList<Node> roadNodes = new ArrayList<Node>();

    public ArrayList<Node> getRoadNodes() {
        return roadNodes;
    }

    /**
     * All GeoPoints that form the Road.
     */
    public void setROADList(List<GeoPoint> list){
        roadPoints = (ArrayList)list;
    }

    public void setRoadPoints(GeoPoint point){roadPoints.add(point);}

    public ArrayList<GeoPoint> getRoadPoints() {
        return roadPoints;
    }




}
