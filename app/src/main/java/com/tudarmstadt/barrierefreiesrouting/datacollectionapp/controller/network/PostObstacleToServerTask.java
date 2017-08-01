package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network;


import android.app.Activity;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RoutingServerAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;

import bp.common.model.Stairs;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by deniz on 12.05.17.
 */
public class PostObstacleToServerTask {

    public PostObstacleToServerTask() {

    }

    public static void PostStairs(final Activity activity, final MapEditorFragment mapEditorFragment, final Stairs obstacle) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(obstacle);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonString);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url( RoutingServerAPI.baseURL + RoutingServerAPI.stairsResource)
                .post(body)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, R.string.error_server_not_found,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(activity, activity.getString(R.string.action_barrier_added),
                                        Toast.LENGTH_SHORT).show();
                                 OverlayItem overlayItem = new OverlayItem(obstacle.getName(), activity.getString(R.string.default_description), new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()));
                                 overlayItem.setMarker(activity.getResources().getDrawable(R.mipmap.ramppic));
                                mapEditorFragment.mOverlay.addItem(overlayItem);
                                mapEditorFragment.refresh();

                            }
                        });

                    }
                });
    }

}
