package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem.RoutingServerObstaclesDownloadedEvent;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RoutingServerAPI;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Task to download all obstacles from the routing web api.
 */
public class DownloadObstaclesTask {

    public static void downloadObstacles() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RoutingServerAPI.baseURL + RoutingServerAPI.obstacleResource)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        EventBus.getDefault().post(new RoutingServerObstaclesDownloadedEvent(response));
                    }
                });
    }

}
