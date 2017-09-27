package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import okhttp3.Response;

/**
 * Created by deniz on 14.09.17.
 */

public class RoutingServerRoadDownloadEvent {

    private final Response response;


    public RoutingServerRoadDownloadEvent(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
