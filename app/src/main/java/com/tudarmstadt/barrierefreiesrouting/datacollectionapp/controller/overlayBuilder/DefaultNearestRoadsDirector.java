package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vincent on 03.08.2017.
 */

public class DefaultNearestRoadsDirector {
    INearestRoadsOverlayBuilder builder;


    DefaultNearestRoadsDirector(final INearestRoadsOverlayBuilder builder){
        this.builder = builder;
    }

    public NearestRoadsOverlay construct(GeoPoint p){
        return builder.setApiUrlEndpoint("http://overpass-api.de/api/interpreter")
                .setCenter(p)
                .setRadius(100)
                .setTypes("^(.*)$")
                .build();
    }

}
