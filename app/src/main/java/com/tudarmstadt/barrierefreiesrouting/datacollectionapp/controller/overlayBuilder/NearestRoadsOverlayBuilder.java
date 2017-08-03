package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vincent on 03.08.2017.
 */

public class NearestRoadsOverlayBuilder implements INearestRoadsOverlayBuilder {

    private NearestRoadsOverlay roadsOverlay;

    NearestRoadsOverlayBuilder(){
        roadsOverlay = new NearestRoadsOverlay();
    }

    @Override
    public NearestRoadsOverlay build() {

        http://overpass-api.de/api/interpreter


        return roadsOverlay;
    }

    @Override
    public INearestRoadsOverlayBuilder setRadius(int radius) {
        this.roadsOverlay.radius = radius;
        return this;
    }

    @Override
    public INearestRoadsOverlayBuilder setCenter(GeoPoint center) {
        this.roadsOverlay.center = center;

        return this;
    }

    @Override
    public INearestRoadsOverlayBuilder setTypes(String typeExpression) {
        this.roadsOverlay.highwayTypes = typeExpression;
        return this;
    }


}
