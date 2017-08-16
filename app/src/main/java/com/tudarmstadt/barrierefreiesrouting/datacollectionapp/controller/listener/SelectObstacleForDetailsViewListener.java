package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemLongPressEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemSingleTapEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleOverlayItem;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by vincent on 8/16/17.
 */

public class SelectObstacleForDetailsViewListener implements ItemizedIconOverlay.OnItemGestureListener<ObstacleOverlayItem> {


    @Override
    public boolean onItemSingleTapUp(int i, ObstacleOverlayItem o) {

        EventBus.getDefault().post(new ObstacleOverlayItemSingleTapEvent(o));

        return true;
    }

    @Override
    public boolean onItemLongPress(int i, ObstacleOverlayItem o) {
        EventBus.getDefault().post(new ObstacleOverlayItemLongPressEvent(o));

        return true;
    }
}
