package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.utils.ObstacleTranslator;

import bp.common.model.ObstacleTypes;
import bp.common.model.obstacles.Obstacle;

/**
 * Created by vincent on 8/11/17.
 */

public class ObstacleDetailsViewerFragment extends Fragment {

    private Obstacle obstacle;


    public static ObstacleDetailsViewerFragment newInstance(Obstacle obstacle) {
        ObstacleDetailsViewerFragment fragment = new ObstacleDetailsViewerFragment();
        Bundle args = new Bundle();
        fragment.obstacle = obstacle;

        fragment.setArguments(args);

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.viewer_fragment_obstacle_details, container, false);
        TextView title = (TextView) v.findViewById(R.id.overview_obstacle_title);
        ObstacleTypes typeCode = obstacle.getTypeCode();
        title.setText(getString(R.string.collected)+ " " + ObstacleTranslator.getTranslationFor(getContext(), typeCode) + " "+  getString(R.string.data));




        return v;
    }
}
