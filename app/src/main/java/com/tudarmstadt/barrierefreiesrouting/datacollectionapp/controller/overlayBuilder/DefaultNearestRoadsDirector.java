package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;

import org.osmdroid.util.GeoPoint;

/**
 * Creates an OverlayBuilder with the default parameters.
 * <p>
 * Custom NearestRoadsOverlays can be created analog to this Default calss.
 */
public class DefaultNearestRoadsDirector {

    INearestRoadsOverlayBuilder builder;

    public DefaultNearestRoadsDirector(final INearestRoadsOverlayBuilder builder) {
        this.builder = builder;
    }

    public NearestRoadsOverlay construct(GeoPoint p) {
        return builder
                .setCenter(p)
                .setRadius(50)
                .setTypes("^(.*)$")
                .build();
    }

}
