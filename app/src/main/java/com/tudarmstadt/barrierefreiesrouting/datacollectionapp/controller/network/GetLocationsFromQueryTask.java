package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.apiContracts.MainNomatimAPI;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.BrowseMapActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vincent on 8/10/17.
 */

public class GetLocationsFromQueryTask {

    private OkHttpClient client = new OkHttpClient();

    public GetLocationsFromQueryTask() {
    }

    public static void GetLocationSuggestions(final BrowseMapActivity activity, MapEditorFragment mapEditorFragment, final String query) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MainNomatimAPI.baseURL + MainNomatimAPI.getLocationSuggestions(query))
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        return;

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        return;

                    }
                });
    }
}
