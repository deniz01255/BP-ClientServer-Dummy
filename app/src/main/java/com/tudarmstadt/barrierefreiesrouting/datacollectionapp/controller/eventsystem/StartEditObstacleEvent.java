package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import bp.common.model.obstacles.Obstacle;

/**
 * Created by vincent on 8/16/17.
 */

public class StartEditObstacleEvent {


    private Obstacle obstacle;

    public StartEditObstacleEvent(Obstacle obstacle){

        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}
