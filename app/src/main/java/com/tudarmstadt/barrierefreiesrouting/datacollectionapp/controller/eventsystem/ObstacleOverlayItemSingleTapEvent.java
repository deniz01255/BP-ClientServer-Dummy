package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by vincent on 8/16/17.
 */

public class ObstacleOverlayItemSingleTapEvent {
    private OverlayItem overlayItem;


    public ObstacleOverlayItemSingleTapEvent(OverlayItem o) {
        this.overlayItem = o;
    }

    public OverlayItem getOverlayItem() {
        return overlayItem;
    }
}
