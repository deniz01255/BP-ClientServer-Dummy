package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.google.android.gms.maps.model.Polyline;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
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

    public int radius;

    public String highwayTypes;

}
