package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network;

import android.app.Activity;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RoutingServerAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import bp.common.model.obstacles.Obstacle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by deniz on 12.05.17.
 */
public class DownloadObstaclesTask {

    private OkHttpClient client = new OkHttpClient();

    public DownloadObstaclesTask() {
    }

    public static void DownloadStairs(final Activity activity, final MapEditorFragment mapEditorFragment) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(RoutingServerAPI.baseURL + RoutingServerAPI.stairsResource)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        ObjectMapper mapper = new ObjectMapper();
                        //mapper.enableDefaultTyping();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                        List<Obstacle> obstacleList = new LinkedList<Obstacle>();
                        if (!response.isSuccessful())
                            return;
                        try {
                            obstacleList = mapper.readValue(res, new TypeReference<List<Obstacle>>() {
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        final List<Obstacle> finalObstacleList = obstacleList;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Obstacle obstacle : finalObstacleList) {
                                    OverlayItem overlayItem = new OverlayItem(obstacle.getName(), activity.getString(R.string.default_description), new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()));
                                    overlayItem.setMarker(activity.getResources().getDrawable(R.mipmap.ramppic));
                                    mapEditorFragment.obstacleOverlay.addItem(overlayItem);
                                }
                                Toast.makeText(activity.getBaseContext(), activity.getString(R.string.action_barrier_loaded),
                                        Toast.LENGTH_SHORT).show();
                                mapEditorFragment.map.invalidate();
                            }
                        });

                    }
                });
    }

}
