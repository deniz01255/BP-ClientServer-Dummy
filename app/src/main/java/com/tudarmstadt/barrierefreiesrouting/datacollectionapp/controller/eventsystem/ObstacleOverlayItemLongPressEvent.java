package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by vincent on 8/16/17.
 */

public class ObstacleOverlayItemLongPressEvent {
    private OverlayItem overlayItem;

    public ObstacleOverlayItemLongPressEvent(OverlayItem overlayItem) {
        this.overlayItem = overlayItem;
    }

    public OverlayItem getOverlayItem() {
        return overlayItem;
    }
}
