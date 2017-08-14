package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleAttribute;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelConsumer;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import java.util.Map;

/**
 * Created by vincent on 8/11/17.
 */

public class OverviewSendFragment extends Fragment implements Step, IObstacleViewModelConsumer {

    private static View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.stepfragment_overview_send, container, false);


        } catch (InflateException e) {

        }

        return view;
    }
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

        LinearLayout detailsList = (LinearLayout) view.findViewById(R.id.overview_details_attribute_list);

        detailsList.removeAllViews();

        // Place for each ViewModel Attribute a new label
        for (Map.Entry<String, ObstacleAttribute<?>> entry : ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.entrySet()) {
            
            TextView tt = new TextView(getActivity());
            tt.setText(entry.getValue().name + ": " +  entry.getValue().getString());
            detailsList.addView(tt);

        }
    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }

    @Override
    public void setObstacleViewModel() {

    }



}
