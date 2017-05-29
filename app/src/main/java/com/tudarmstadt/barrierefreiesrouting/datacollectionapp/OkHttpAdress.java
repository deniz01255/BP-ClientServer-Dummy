package com.tudarmstadt.barrierefreiesrouting.datacollectionapp;


import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.osmdroid.util.GeoPoint;


/**
 * Created by deniz on 12.05.17.
 */

public class OkHttpAdress extends AsyncTask<Object, Void, GeoPoint> {

    TextView textResponse;
    private Exception exception;
    OkHttpClient client = new OkHttpClient();
    private String respo = "";
    String addr;
    GeoPoint locationPoint = null;


    OkHttpAdress(TextView tv, String address) {

        this.addr = address;
        this.textResponse = tv;

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
        textResponse.setText(respo);
        super.onPostExecute(feed);
    }

    public String getAnswer() {
        return respo;
    }

    public static void main(String[] args) throws IOException {
        /** OkHttpAdress example = new OkHttpAdress();
         String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
         System.out.println(response);**/
    }

    /**  try {
     URL url = new URL(
     "http://maps.googleapis.com/maps/api/geocode/json?address="
     + URIUtil.encodeQuery("Sayaji Hotel, Near balewadi stadium, pune") + "&sensor=true");
     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
     conn.setRequestMethod("GET");
     conn.setRequestProperty("Accept", "application/json");

     if (conn.getResponseCode() != 200) {
     throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
     }
     BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

     String output = "", full = "";
     while ((output = br.readLine()) != null) {
     System.out.println(output);
     full += output;
     }**/


}
