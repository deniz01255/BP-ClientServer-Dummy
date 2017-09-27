package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import okhttp3.Response;

/**
 * Created by vincent on 28.09.17.
 */

public class BlacklistedRoadsTaskDownloadedEvent {

    private final Response response;


    public BlacklistedRoadsTaskDownloadedEvent(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
