package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;

import bp.common.model.obstacles.Obstacle;
import bp.common.model.ways.Way;
import okhttp3.Response;

/**
 * Created by deniz on 29.08.17.
 */

public class RoutingServerStreetPostedEvent {

    private final Response response;
    private final Way way;


    public RoutingServerStreetPostedEvent(Response response, Way way) {
        this.response = response;
        this.way = way;
    }

    public Response getResponse() {
        return response;
    }

    public Way getObstacle() {
        return way;
    }
}



