package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Created by vincent on 8/16/17.
 */

public class RoadsHelperOverlayChangedEvent {


    private ArrayList<Polyline> roads;

    public RoadsHelperOverlayChangedEvent(ArrayList<Polyline> roads){

        this.roads = roads;
    }

    public ArrayList<Polyline> getRoads() {
        return roads;
    }
}
