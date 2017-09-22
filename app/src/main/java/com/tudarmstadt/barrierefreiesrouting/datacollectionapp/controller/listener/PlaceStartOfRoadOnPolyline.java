package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstaclePositionSelectedOnPolylineEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoadPositionSelectedOnPolylineEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deniz on 26.08.17.
 */

public class PlaceStartOfRoadOnPolyline implements Polyline.OnClickListener,IUserInteractionWithMap {
   public List<GeoPoint> roadEndPoints = new ArrayList<>();
   public Road newStreet;
    public List<Marker>  RoadMarker = new ArrayList<>();
    public List<Road> RoadList = new ArrayList<>();
    public MapEditorFragment mapEditorFragment;
    public  Context context;
    public  List<Polyline> currentRoadCapture = new ArrayList<>();

    public PlaceStartOfRoadOnPolyline( Context context){
        /**this.mapEditorFragment = mapEditorFragment;**/
        this.context = context;
    }


    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint geoPoint) {
        newStreet =  new Road();
        //List<Overlay> po = mapView.getOverlays();
        /** for(Overlay ov: mapView.getOverlays()){
           if(Polyline.class.isInstance(ov)){
                mapView.getOverlays().remove(ov);
            }
        }**/


        Polyline streetLine = new Polyline(context);
        roadEndPoints.add(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()));
        roadEndPoints.add(new GeoPoint(geoPoint.getLatitude(), geoPoint.getLongitude()+0.0002));
        roadEndPoints.add(new GeoPoint(geoPoint.getLatitude()+0.0002, geoPoint.getLongitude()));
        newStreet.setROADList(roadEndPoints);

        streetLine = setUPPoly(streetLine, mapView,roadEndPoints);


        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(roadEndPoints.get(0));
        startMarker.setTitle("Start point for creating new Road");
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        RoadMarker.add(startMarker);

        Marker endM = new Marker(mapView);
        endM.setPosition(roadEndPoints.get(2));
        endM.setTitle("Start point for creating new Road");
        endM.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        RoadMarker.add(endM);

        addMapOverlay(startMarker, streetLine, mapView);
        addMapOverlay(endM, streetLine, mapView);

        RoadList.add(newStreet);

        Log.d("myTag", "This is my message");
        roadEndPoints.clear();
        EventBus.getDefault().post(new RoadPositionSelectedOnPolylineEvent(geoPoint));

      /**  for (Overlay overlay:mapView.getOverlays()) {
            if(Marker.class.isInstance(overlay)){
                break;
            }else{
                mapView.getOverlays().remove(overlay);
            }
        }**/

        return true;
    }


    public void addMapOverlay(Marker marker, Polyline polyline, MapView map){
        map.getOverlays().add(marker);
        map.getOverlayManager().add(polyline);
        map.invalidate();
    }

    public Polyline setUPPoly(Polyline streetLine, MapView map, List<GeoPoint> list){

        streetLine.setTitle("Text param");
        streetLine.setWidth(10f);
        streetLine.setColor(Color.BLUE);
        streetLine.setPoints(list);
        streetLine.setGeodesic(true);
        streetLine.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));

        currentRoadCapture.add(streetLine);

        return streetLine;
    }


    @Override
    public boolean longPressHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {
        return false;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment) {
        if(roadEndPoints.size() > 0) {

            List<GeoPoint> roadEndPointsCrob = new ArrayList<>();
            Polyline streetLine = new Polyline(context);


            roadEndPoints.add(p);
            RoadList.get(RoadList.size()-1).setROADList(roadEndPoints);

            roadEndPointsCrob.add(roadEndPoints.get(roadEndPoints.size() - 2));
            roadEndPointsCrob.add(p);
            streetLine = setUPPoly(streetLine, mapEditorFragment.map,roadEndPointsCrob);

            Marker end = new Marker(mapEditorFragment.map);
            end.setPosition(p);
            end.setTitle("endPunkt");
            end.setDraggable(true);
            end.isDraggable();
           // end.setOnMarkerDragListener(new DragObstacleListener(mapEditorFragment,roadEndPoints,this,context));


            end.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            RoadMarker.add(end);


            addMapOverlay(end, streetLine, mapEditorFragment.map);


            return true;
        }
        else{
            return false;
        }
    }
}
