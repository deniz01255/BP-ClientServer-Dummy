package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import android.content.Context;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator.RoadEditorOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deniz on 20.08.17.
 */

public class DragObstacleListener implements Marker.OnMarkerDragListener{
    List<GeoPoint> roadPoints = new ArrayList<>();
    MapEditorFragment mapEditorFragment;
    RoadEditorOperator roadEditorOperator;
    Context context;

    public DragObstacleListener(MapEditorFragment mapEditorFragment, List<GeoPoint> roadPoints,RoadEditorOperator roadEditorOperator, Context context){
        this.mapEditorFragment = mapEditorFragment;
        this.roadPoints = roadPoints;
        this.roadEditorOperator = roadEditorOperator;
        this.context = context;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        marker.setPosition(new GeoPoint(marker.getPosition().getLatitude(),marker.getPosition().getLongitude()));


    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        GeoPoint geopoint = marker.getPosition();
        Polyline streetLine = new Polyline(context);
        List<GeoPoint> roadEndPointsCrob = new ArrayList<>();

        roadPoints.add(geopoint);
        roadEndPointsCrob.add(roadPoints.get(roadPoints.size() - 2));
        roadEndPointsCrob.add(geopoint);
        roadPoints.remove(roadPoints.size()-1);

       streetLine = roadEditorOperator.setUPPoly(streetLine, mapEditorFragment,roadEndPointsCrob);

        mapEditorFragment.map.getOverlayManager().add(streetLine);
        mapEditorFragment.map.invalidate();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }
}


