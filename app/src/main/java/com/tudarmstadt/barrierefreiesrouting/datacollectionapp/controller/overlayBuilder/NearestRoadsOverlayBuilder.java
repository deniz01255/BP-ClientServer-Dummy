package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;

import org.osmdroid.util.GeoPoint;

import java.util.LinkedList;

import okhttp3.Response;

/**
 * Created by Vincent on 03.08.2017.
 */

public class NearestRoadsOverlayBuilder implements INearestRoadsOverlayBuilder {

    protected Response response = null;
    protected LinkedList<Road> roads = null;
    private NearestRoadsOverlay roadsOverlay;
    private MainActivity context;

    public NearestRoadsOverlayBuilder(MainActivity context) {
        this.context = context;
        roadsOverlay = new NearestRoadsOverlay();
    }

    @Override
    public NearestRoadsOverlay build() {

        //http://overpass-api.de/api/interpreter

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
