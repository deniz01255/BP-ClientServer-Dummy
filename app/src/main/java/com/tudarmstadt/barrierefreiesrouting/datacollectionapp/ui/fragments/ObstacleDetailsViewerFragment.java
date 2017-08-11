package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

/**
 * Created by vincent on 8/11/17.
 */

public class ObstacleDetailsViewerFragment extends Fragment {


    public static ObstacleDetailsViewerFragment newInstance() {
        ObstacleDetailsViewerFragment fragment = new ObstacleDetailsViewerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.viewer_fragment_obstacle_details, container, false);


        return v;
    }
}
