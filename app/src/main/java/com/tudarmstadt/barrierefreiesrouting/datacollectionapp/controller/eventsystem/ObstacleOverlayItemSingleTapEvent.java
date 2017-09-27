package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleOverlayItem;

/**
 * Created by vincent on 8/16/17.
 */

public class ObstacleOverlayItemSingleTapEvent {
    private ObstacleOverlayItem overlayItem;


    public ObstacleOverlayItemSingleTapEvent(ObstacleOverlayItem o) {
        this.overlayItem = o;
    }

    public ObstacleOverlayItem getOverlayItem() {
        return overlayItem;
    }
}
