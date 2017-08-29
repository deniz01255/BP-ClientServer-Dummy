package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;

import bp.common.model.obstacles.Obstacle;
import okhttp3.Response;

/**
 * Created by deniz on 29.08.17.
 */

public class RoutingServerStreetPostedEvent {

    private final Response response;
    private final Road road;


    public RoutingServerStreetPostedEvent(Response response, Road road) {
        this.response = response;
        this.road = road;
    }

    public Response getResponse() {
        return response;
    }

    public Road getObstacle() {
        return road;
    }
}



