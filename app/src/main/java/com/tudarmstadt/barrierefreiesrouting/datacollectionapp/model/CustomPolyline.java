package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.views.overlay.Polyline;

/**
 * This class extends from Polyline class and one extra attribute road
 */

public class CustomPolyline extends Polyline {
    private ParcedOverpassRoad road = null;

    /**
     * @return the road element corresponding to this Polyline Object
     */
    public ParcedOverpassRoad getRoad() {
        return road;
    }

    public void setRoad(ParcedOverpassRoad road) {
        this.road = road;
    }
}
