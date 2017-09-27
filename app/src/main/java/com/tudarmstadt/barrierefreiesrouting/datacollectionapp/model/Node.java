package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.util.GeoPoint;

/**
 * Used for parsing the response from overpass api.
 * <p>
 * Used by OsmParser
 */
public class Node {

    public GeoPoint geoPoint;
    public long id;

}
