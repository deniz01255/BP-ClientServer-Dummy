package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.CloseToRoad;

import android.app.Activity;
import android.util.Log;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostObstacleToServerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;

import bp.common.model.Stairs;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by deniz on 05.07.17.
 */

public class CloseToRoadChecker {

    private OkHttpClient client = new OkHttpClient();
    public CloseToRoadChecker(){}

    public static void DownloadStairs(final Activity activity , final GeoPoint location, final MapEditorFragment mapEditorFragment){

       /** StringBuilder sb = new StringBuilder("http://router.project-osrm.org/nearest/v1/driving/");
        sb.append(String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude()));
        sb.append("?number=3&bearings=0,20");**/


        StringBuilder sb = new StringBuilder("https://roads.googleapis.com/v1/nearestRoads?points=");
        sb.append(String.valueOf(location.getLatitude())+","+String.valueOf(location.getLongitude()));
        sb.append("&key=AIzaSyAZfrKvAgPmHvi4t70-UYMBOMGOQxV7m7I");



        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(sb.toString())
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonData = responses.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            JSONArray Jarray = Jobject.getJSONArray("snappedPoints");

            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject object     = Jarray.getJSONObject(i);
                JSONObject coord  = object.getJSONObject("location");


                final Stairs newObstacle = new Stairs("ANGEBOOOT BP FERTIG", (Double) coord.get("longitude"), (Double) coord.get("latitude"), 10, 10, false) ;
                PostObstacleToServerTask.PostStairs(activity, mapEditorFragment, newObstacle);
            }
        }catch (IOException e){}
         catch (JSONException j){
             Log.d("ERROR SERVER OSRM",j.toString());
        }
    }





}
