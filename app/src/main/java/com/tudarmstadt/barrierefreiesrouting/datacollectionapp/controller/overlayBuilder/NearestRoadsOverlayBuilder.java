package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;

import org.osmdroid.util.GeoPoint;
import java.util.LinkedList;
import okhttp3.Response;

/**
 * Builds a new NearestRoadsOverlay.
 *
 * Use DefaultNearestRoadsDirector if you just want to create an roads overlay or create a own
 * implementation that uses this Builder to create own Overlay types.
 */

public class NearestRoadsOverlayBuilder implements INearestRoadsOverlayBuilder {

    protected Response response = null;
    protected LinkedList<Road> roads = null;
    private NearestRoadsOverlay roadsOverlay;

    public NearestRoadsOverlayBuilder() {
        roadsOverlay = new NearestRoadsOverlay();
    }

    @Override
    public NearestRoadsOverlay build() {
        roadsOverlay.nearestRoads = new LinkedList<>();
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
