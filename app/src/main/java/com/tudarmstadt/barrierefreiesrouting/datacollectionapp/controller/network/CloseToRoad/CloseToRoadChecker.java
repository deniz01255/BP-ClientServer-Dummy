package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.CloseToRoad;

import android.app.Activity;
import android.util.Log;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostObstacleToServerTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.GoogleRoadsAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.json.JSONArray;
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

    public CloseToRoadChecker() {
    }

    public static void CloseToRoadChecker(final Activity activity, final GeoPoint location, final MapEditorFragment mapEditorFragment) {

        /** StringBuilder sb = new StringBuilder("http://router.project-osrm.org/nearest/v1/driving/");
         sb.append(String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude()));
         sb.append("?number=3&bearings=0,20");**/

        StringBuilder sb = new StringBuilder(GoogleRoadsAPI.baseURL + GoogleRoadsAPI.nearesRoutesResource + "?points=");
        sb.append(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
        sb.append("&key=" + GoogleRoadsAPI.apiKey);

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
            JSONObject jObject = new JSONObject(jsonData);
            JSONArray jArray = jObject.getJSONArray("snappedPoints");

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                JSONObject coord = object.getJSONObject("location");

                final Stairs newObstacle = new Stairs(activity.getString(R.string.default_description), (Double) coord.get("longitude"), (Double) coord.get("latitude"), 10, 10, false);
                PostObstacleToServerTask.PostStairs(activity, mapEditorFragment, newObstacle);
            }
        } catch (Exception e) {
            Log.d("closeToRoadChecker", e.toString());

        }
    }

}
