package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ParcedOverpassRoad;

import org.osmdroid.util.GeoPoint;

import java.util.LinkedList;

/**
 * This Overlay is used to provide an overlay for the user to choose where the obstacle should be placed
 */
public class NearestRoadsOverlay {

    /**
     * Contains the nearest roads of the given point
     */
    public LinkedList<ParcedOverpassRoad> nearestRoads;

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
