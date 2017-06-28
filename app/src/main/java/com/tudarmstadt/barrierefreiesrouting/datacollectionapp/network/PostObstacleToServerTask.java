package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network;


import android.os.AsyncTask;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.MainActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.util.List;

import bp.common.model.IObstacle;
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
public class PostObstacleToServerTask extends AsyncTask<IObstacle, Void, Void> {


    private MainActivity activity; //activity is defined as a global variable in your AsyncTask

    public PostObstacleToServerTask(MainActivity a) {
        activity = a;
    }




    @Override
    protected Void doInBackground(IObstacle... params) {
        ObjectMapper mapper = new ObjectMapper();
        IObstacle newObstacle = params[0];
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(newObstacle);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
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
        return null;
    }

}
