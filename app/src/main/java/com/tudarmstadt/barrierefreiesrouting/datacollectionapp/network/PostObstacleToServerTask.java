package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;


import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;
import bp.common.model.Stairs;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R.id.map;


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
                .url("https://routing.vincinator.de/routing/barriers/stairs")
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
                                Toast.makeText(activity, "Fehler",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(activity, "Barriere hinzugefügt",
                                        Toast.LENGTH_LONG).show();
                                 OverlayItem overlayItem = new OverlayItem(obstacle.getName(), "Long press is kacke", new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()));
                                 overlayItem.setMarker(activity.getResources().getDrawable(R.mipmap.ramppic));
                                mapEditorFragment.mOverlay.addItem(overlayItem);
                                mapEditorFragment.refresh();

                            }
                        });

                    }
                });
    }

}
