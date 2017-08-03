package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder.NearestRoadsOverlay;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vincent on 03.08.2017.
 */

public interface INearestRoadsOverlayBuilder {

    NearestRoadsOverlay build();

    INearestRoadsOverlayBuilder setRadius(final int radius);

    INearestRoadsOverlayBuilder setCenter(final GeoPoint center);

    /**
     * Example Expression for selecting all types: "^(.*)$"
     */
    INearestRoadsOverlayBuilder setTypes(final String typeExpression);


}
