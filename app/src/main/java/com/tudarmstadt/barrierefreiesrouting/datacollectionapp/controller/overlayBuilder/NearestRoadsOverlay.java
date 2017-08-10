package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;

import org.osmdroid.util.GeoPoint;

import java.util.LinkedList;

/**
 * Created by Vincent on 03.08.2017.
 */

public class NearestRoadsOverlay {

    /**
     * Contains the nearest roads of the given point
     */
    public LinkedList<Road> nearestRoads;

    /**
     * The User provides this point. It is the center point of the circle of nearest roads.
     */
    public GeoPoint center;

    /**
     * Radius of the search query for the nearest roads.
     */
    public int radius;

    /**
     * Filters the types of roads to create overlays for.
     */
    public String highwayTypes;

}
