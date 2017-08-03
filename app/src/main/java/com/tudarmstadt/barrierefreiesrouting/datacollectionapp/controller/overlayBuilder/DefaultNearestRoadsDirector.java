package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vincent on 03.08.2017.
 */

public class DefaultNearestRoadsDirector {
    INearestRoadsOverlayBuilder builder;


    public DefaultNearestRoadsDirector(final INearestRoadsOverlayBuilder builder){
        this.builder = builder;
    }

    public NearestRoadsOverlay construct(GeoPoint p){

        System.out.print("");
        return builder
                .setCenter(p)
                .setRadius(100)
                .setTypes("^(.*)$")
                .build();
    }

}
