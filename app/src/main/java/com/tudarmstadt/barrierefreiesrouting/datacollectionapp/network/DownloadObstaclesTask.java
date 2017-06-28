package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network;


import android.app.Activity;
import android.os.AsyncTask;

import com.fasterxml.jackson.core.type.TypeReference;
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




}
