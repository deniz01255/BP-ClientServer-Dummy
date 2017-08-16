package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import bp.common.model.obstacles.Obstacle;
import okhttp3.Response;

/**
 * Created by vincent on 8/16/17.
 */

public class RoutingServerObstaclesDownloadedEvent {



    private final Response response;


    public RoutingServerObstaclesDownloadedEvent(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }


}
