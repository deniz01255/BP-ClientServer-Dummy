package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoutingServerObstaclePostedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoutingServerStreetPostedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RoutingServerAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.Road;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import bp.common.model.obstacles.Obstacle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by deniz on 29.08.17.
 */

public class PostStreetToServerTask {

    public PostStreetToServerTask() {}

    public static void PostStreet(final Road road) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(road);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonString);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RoutingServerAPI.baseURL + RoutingServerAPI.obstacleResource)
                .post(body)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        EventBus.getDefault().post(new RoutingServerStreetPostedEvent(response, road));
                    }
                });
    }
}
