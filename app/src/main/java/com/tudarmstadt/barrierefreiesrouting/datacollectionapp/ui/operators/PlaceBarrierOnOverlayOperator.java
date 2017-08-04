package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;


import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.Polyline.OnClickListener;

/**
 *
 * This class does not implement an Operator Interface - Not beautiful..
 * This is just the onClick Listener for the overlay.
 * In fact, one can argue that we need both Operators at the same time..
 * Naming this class PlaceBarrierOnOverlayOperator leads to a less cluttered file structure..
 *
 *
 */

public class PlaceBarrierOnOverlayOperator implements OnClickListener {

    private OverlayItem overlayItem;
    private ItemizedOverlayWithFocus setNewObstaclePositionOverlay;


    public PlaceBarrierOnOverlayOperator(ItemizedOverlayWithFocus setNewObstaclePositionOverlay, OverlayItem overlayItem){

        this.overlayItem = overlayItem;
        this.setNewObstaclePositionOverlay = setNewObstaclePositionOverlay;
    }


    @Override
    public boolean onClick(Polyline polyline, MapView mapView, GeoPoint eventPos) {
        setNewObstaclePositionOverlay.removeAllItems();
        OverlayItem newOverlayItem = new OverlayItem(overlayItem.getTitle(), overlayItem.getTitle(), eventPos);
        overlayItem.setMarker(mapView.getContext().getResources().getDrawable(R.mipmap.ramppic));


        setNewObstaclePositionOverlay.addItem(newOverlayItem);
        overlayItem = newOverlayItem;
        // Workaround: display the tempOverlay on top
        mapView.getOverlays().remove(setNewObstaclePositionOverlay);
        mapView.getOverlays().add(setNewObstaclePositionOverlay);


        mapView.invalidate();

        return true;
    }

}
