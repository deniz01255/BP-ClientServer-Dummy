package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network;


import android.os.AsyncTask;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import bp.common.model.Obstacle;
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

public  class BpServerHandler extends AsyncTask<Object, Void, GeoPoint> {


    private Exception exception;
    OkHttpClient client = new OkHttpClient();
    private String respo = "";
    String addr;
    GeoPoint locationPoint = null;

    BpServerHandler(String address) {
        this.addr = address;
    }

    @Override
    protected GeoPoint doInBackground(Object... params) {
        try {
            Request request = new Request.Builder()
                    .url(addr)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                respo = response.body().string();

                JSONObject json;

                try {

                    String lat, lon;
                    json = new JSONObject(respo);
                    JSONObject geoMetryObject = new JSONObject();
                    JSONObject locations = new JSONObject();
                    JSONArray jarr = json.getJSONArray("results");
                    int i;
                    for (i = 0; i < jarr.length(); i++) {
                        json = jarr.getJSONObject(i);
                        geoMetryObject = json.getJSONObject("geometry");
                        locations = geoMetryObject.getJSONObject("location");
                        lat = locations.getString("lat");
                        lon = locations.getString("lng");

                        locationPoint = new GeoPoint(Double.parseDouble(lat),
                                Double.parseDouble(lon));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return locationPoint;


                // return respo = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            this.exception = e;

            return null;
        }
        return null;
    }

    protected void onPostExecute(GeoPoint feed) {
        super.onPostExecute(feed);
    }

    public List<Obstacle> getObstaclesFromServer() {
       return null;
    }


}
