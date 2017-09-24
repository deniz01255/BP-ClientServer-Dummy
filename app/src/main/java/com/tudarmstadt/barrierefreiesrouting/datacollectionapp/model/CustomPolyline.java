package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.views.overlay.Polyline;

/**
 * Created by Bi on 17.08.2017.
 * this class extends from Polyline class and one extra attribute road
 */

public class CustomPolyline extends Polyline {
    private Road road = null;

    /**
     * @return the road element corresponding to this Polyline Object
     */
    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }
}
