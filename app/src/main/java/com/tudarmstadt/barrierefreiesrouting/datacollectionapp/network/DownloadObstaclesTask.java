package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;


/**
 * Created by deniz on 12.05.17.
 */
public class BpServerHandler extends AsyncTask<IObstacle, Void, Void> {


    private MainActivity activity; //activity is defined as a global variable in your AsyncTask

    public BpServerHandler(MainActivity a) {
        activity = a;
    }


    public void getObstaclesFromServer() {

    }

    public boolean PostNewObstacle(IObstacle newObstacle) {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(newObstacle);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return true;
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonString);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://routing.vincinator.de/routing/barriers")
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
                                // OverlayItem overlayItem = new OverlayItem(newObstacle.getName(), "Importierte Barriere", new GeoPoint(newObstacle.getLatitude(), newObstacle.getLongitude()));
                                // overlayItem.setMarker(activity.getResources().getDrawable(R.mipmap.ramppic));
                                //mapEditorFragment.mOverlay.addItem(overlayItem);
                                activity.getMapEditorFragment().refresh();
                            }
                        });

                    }
                });
        return false;
    }


    @Override
    protected Void doInBackground(IObstacle... params) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://routing.vincinator.de/routing/barriers")
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
                        if (!response.isSuccessful())
                            return;
                        final List<IObstacle> obstacleList = mapper.readValue(res, new TypeReference<List<IObstacle>>() {
                        });

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (IObstacle obstacle : obstacleList) {
                                    OverlayItem overlayItem = new OverlayItem(obstacle.getName(), "Importierte Barriere", new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()));
                                    overlayItem.setMarker(activity.getResources().getDrawable(R.mipmap.ramppic));
                                    activity.getMapEditorFragment().addObstacle(overlayItem);
                                }
                                activity.getMapEditorFragment().refresh();

                            }
                        });

                    }
                });
    }


}
