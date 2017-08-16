package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemLongPressEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.ObstacleOverlayItemSingleTapEvent;

import org.greenrobot.eventbus.EventBus;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by vincent on 8/16/17.
 */

public class SelectObstacleForDetailsView implements ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {


    @Override
    public boolean onItemSingleTapUp(int i, OverlayItem o) {

        EventBus.getDefault().post(new ObstacleOverlayItemSingleTapEvent(o));

        return true;
    }

    @Override
    public boolean onItemLongPress(int i, OverlayItem o) {
        EventBus.getDefault().post(new ObstacleOverlayItemLongPressEvent(o));

        return true;
    }
}
