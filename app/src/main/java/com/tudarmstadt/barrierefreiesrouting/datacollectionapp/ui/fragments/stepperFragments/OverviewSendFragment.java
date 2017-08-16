package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleAttribute;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostObstacleToServerTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.utils.ObstacleTranslator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelConsumer;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.BrowseMapActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import java.util.Map;

import bp.common.model.ObstacleTypes;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleToViewConverter.convertAttributeMapToObstacle;

/**
 * Created by vincent on 8/11/17.
 */

public class OverviewSendFragment extends Fragment implements BlockingStep, IObstacleViewModelConsumer {

    private View view;


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

        TextView title = (TextView) view.findViewById(R.id.overview_obstacle_title);


        ObstacleTypes typeCode = ObstacleDataSingleton.getInstance().getmObstacle().getTypeCode();


        title.setText(getString(R.string.collected)+ " " + ObstacleTranslator.getTranslationFor(getContext(), typeCode) + " "+  getString(R.string.data));

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

    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();

    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback) {

        ObstacleDataSingleton.getInstance().setmObstacle(convertAttributeMapToObstacle(ObstacleDataSingleton.getInstance().getmObstacleViewModel()));

        PostObstacleToServerTask.PostObstacle(ObstacleDataSingleton.getInstance().getmObstacle());

        // TODO: place this in the success of the server message (?) and update the BrowseMapActivity manually
        ObstacleDataSingleton.getInstance().obstacleDataCollectionCompleted = true;

        callback.complete();
    }

    @Override
    @UiThread
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
