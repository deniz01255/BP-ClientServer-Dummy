package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.overlayBuilder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.OverpassAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.RoutingServerAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.INearestRoadsOverlayBuilder;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Vincent on 03.08.2017.
 */

public class NearestRoadsOverlayBuilder  implements INearestRoadsOverlayBuilder {

    private NearestRoadsOverlay roadsOverlay;
    private MainActivity context;

    public NearestRoadsOverlayBuilder(MainActivity context){
        this.context = context;
        roadsOverlay = new NearestRoadsOverlay();
    }

    @Override
    public NearestRoadsOverlay build() {

        //http://overpass-api.de/api/interpreter

        GetHighwaysFromOverpassAPITask task = new GetHighwaysFromOverpassAPITask( context);
        task.execute(this.roadsOverlay.center,this.roadsOverlay.radius);


        return roadsOverlay;
    }

    @Override
    public INearestRoadsOverlayBuilder setRadius(int radius) {
        this.roadsOverlay.radius = radius;
        return this;
    }

    @Override
    public INearestRoadsOverlayBuilder setCenter(GeoPoint center) {
        this.roadsOverlay.center = center;

        return this;
    }

    @Override
    public INearestRoadsOverlayBuilder setTypes(String typeExpression) {
        this.roadsOverlay.highwayTypes = typeExpression;
        return this;
    }


    class GetHighwaysFromOverpassAPITask extends AsyncTask<Object, Object, Response> {
        Intent myIntent = new Intent(context, MainActivity.class);

        private MainActivity activity;

        ProgressDialog progressDialog;

        GetHighwaysFromOverpassAPITask(MainActivity activity){

            this.activity = activity;
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.setMessage("Progress start");
            this.progressDialog.show();
        }

        @Override
        protected Response doInBackground(Object... params) {

            GeoPoint p = (GeoPoint) params[0];
            int radius = (int) params[1];
            DownloadObstaclesTask task = new DownloadObstaclesTask();
            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = RequestBody.create(MediaType.parse("text/plain"), OverpassAPI.getNearestHighwaysPayload(p, radius));

            Request request = new Request.Builder()
                    .url( OverpassAPI.baseURL + OverpassAPI.stairsResource)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

}
