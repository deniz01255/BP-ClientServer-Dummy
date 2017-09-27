package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemLongPressEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemSingleTapEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleOverlayItem;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.views.overlay.ItemizedIconOverlay;

/**
 * Listener that emits events if an Obstacle Overlay Item was clicked
 * This Listener only makes sense if it is used for ObstacleOverlayItems
 */
public class SelectObstacleForDetailsViewListener implements ItemizedIconOverlay.OnItemGestureListener<ObstacleOverlayItem> {


    @Override
    public boolean onItemSingleTapUp(int i, ObstacleOverlayItem o) {
        // set the selected obstacle for edit mode
        EventBus.getDefault().post(new ObstacleOverlayItemSingleTapEvent(o));

        return true;
    }

    @Override
    public boolean onItemLongPress(int i, ObstacleOverlayItem o) {
        EventBus.getDefault().post(new ObstacleOverlayItemLongPressEvent(o));
        return true;
    }
}
