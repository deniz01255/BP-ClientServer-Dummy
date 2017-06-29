package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network;


import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IAsyncDownloadObstaclesResponse;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;
import bp.common.model.Stairs;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;


/**
 * Created by deniz on 12.05.17.
 */
public class DownloadObstaclesTask{


    private OkHttpClient client = new OkHttpClient();
    public DownloadObstaclesTask() {
    }

    public static void DownloadStairs(final Activity activity, final MapEditorFragment mapEditorFragment){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://routing.vincinator.de/routing/barriers/stairs")
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

                        List<Stairs> obstacleList = new LinkedList<Stairs>();
                        if (!response.isSuccessful())
                            return;
                        try{
                            obstacleList = mapper.readValue(res, new TypeReference<List<Stairs>>() {});
                        }catch(Exception e){
                            e.printStackTrace();
                        }


                        final List<Stairs> finalObstacleList = obstacleList;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Stairs obstacle : finalObstacleList) {
                                    OverlayItem overlayItem = new OverlayItem(obstacle.getName(), "Importierte Barriere", new GeoPoint(obstacle.getLatitude(), obstacle.getLongitude()));
                                    overlayItem.setMarker(activity.getResources().getDrawable(R.mipmap.ramppic));
                                    mapEditorFragment.mOverlay.addItem(overlayItem);

                                }
                                Toast.makeText(activity.getBaseContext(), "Barriers loaded",
                                        Toast.LENGTH_LONG).show();
                                mapEditorFragment.map.invalidate();
                            }
                        });

                    }
                });
    }


}
