package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import bp.common.model.obstacles.Obstacle;
import okhttp3.Response;

/**
 * Created by vincent on 8/16/17.
 */

public class RoutingServerObstaclePostedEvent {


    private final Response response;
    private final Obstacle obstacle;


    public RoutingServerObstaclePostedEvent(Response response, Obstacle obstacle) {
        this.response = response;
        this.obstacle = obstacle;
    }

    public Response getResponse() {
        return response;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}
