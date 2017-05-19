package com.example.deniz.bp_serverclientcommunication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;





public class MainActivity extends AppCompatActivity {

    // variables for taken the income of the frontend
    private EditText et;
    private Button bt,btPost;
    public TextView tv;
    private OkHttpAdress example;
    private OkHttpSendData examplePost;
    GeoPoint answer;
    MapView map;
    /** Called when the activity is first created. */


    String responseString = "";

    // final string because of using server-Address always the same.
    final StringBuilder scriptUrlString = new StringBuilder();//"http://api.openstreetmap.org/api/0.6/trackpoints?bbox=0,51.5,0.25,51.75&page=0";

    /**
     *
     * Method is always created automatically by Developing environment, setting up an interactive Gui connected to backend
     *
     * Function of method on Create(Bundle): - connecting attributes to buttons on our surface
     *                                       - add ONE ButtonClickListener for sending message to Server -> calling method sendToServer
     * [Checks always first for an Internet connection before sending to Server]
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        scriptUrlString.append("http://maps.googleapis.com/maps/api/geocode/json?address=");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.editText);

        bt = (Button) findViewById(R.id.button);
        btPost = (Button) findViewById(R.id.postButton);

        tv = (TextView) findViewById(R.id.textView2);
        map = (MapView) findViewById(R.id.map);
        /**
         * Button to Receive Data From Server
         */
        bt.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scriptUrlString.append(et.getText().toString());
                    scriptUrlString.append("&sensor=true");
                    String param = scriptUrlString.toString();
                    example = new OkHttpAdress(tv,param);
                    try {
                        answer = example.execute().get();
                    }catch(ExecutionException e){
                        e.printStackTrace();
                    }catch (InterruptedException i){
                        i.printStackTrace();
                    }
                    if(answer == null){
                        tv.setText("ERROR-MESSAGE: Can't receive Server, please check internet-connection ");
                    }else {

                        tv.setText("Korrekt");


                        Context ctx = getApplicationContext();
                        //important! set your user agent to prevent getting banned from the osm servers
                        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


                        map.setTileSource(TileSourceFactory.MAPNIK);
                        map.setBuiltInZoomControls(true);
                        map.setMultiTouchControls(true);
                        IMapController mapController = map.getController();
                        mapController.setZoom(15);
                        mapController.setCenter(answer);

                    }

                }
            });

             }
        });


        /**
         * Button to Send-Data From Server
         */

        btPost.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            try {
                                OkHttpSendData example = new OkHttpSendData();
                                tv.setText(example.execute().get().toString());
                            } catch(InterruptedException i){
                                i.printStackTrace();
                            } catch(ExecutionException ex){
                                ex.printStackTrace();
                            }

                            System.out.println("fertig");

                    }
                });

            }
        });


    }



    public boolean internetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();



        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }



}
