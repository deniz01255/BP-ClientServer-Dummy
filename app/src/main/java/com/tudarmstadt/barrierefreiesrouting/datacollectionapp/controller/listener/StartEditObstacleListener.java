package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.listener;

import android.view.View;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.StartEditObstacleEvent;

import org.greenrobot.eventbus.EventBus;

import bp.common.model.obstacles.Obstacle;

/**
 * Created by vincent on 8/16/17.
 */

public class StartEditObstacleListener implements View.OnClickListener {


    private Obstacle obstacle;

    public StartEditObstacleListener(Obstacle obstacle) {

        this.obstacle = obstacle;
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new StartEditObstacleEvent(obstacle));

    }
}
