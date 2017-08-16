package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import okhttp3.Response;

/**
 * Created by vincent on 8/16/17.
 */

public class RoutingServerResponseEvent {


    private Response response;


    public RoutingServerResponseEvent( Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
