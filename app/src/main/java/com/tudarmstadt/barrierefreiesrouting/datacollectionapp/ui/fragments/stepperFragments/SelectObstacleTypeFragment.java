package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import bp.common.model.obstacles.Construction;
import bp.common.model.obstacles.Elevator;
import bp.common.model.obstacles.FastTrafficLight;
import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Ramp;
import bp.common.model.obstacles.Stairs;
import bp.common.model.obstacles.TightPassage;
import bp.common.model.obstacles.Unevenness;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleToViewConverter.convertObstacleToViewModel;

/**
 * Created by vincent on 8/11/17.
 */

public class SelectObstacleTypeFragment extends Fragment implements Step {

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.stepfragment_select_obstacle, container, false);

            Spinner spinner = (Spinner) view.findViewById(R.id.spinner_obstacle_selection);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.BARRIER_TYPES, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {

                    Obstacle newObstacle = getObstacleFrom(position);
                    ObstacleDataSingleton.getInstance().setmObstacle(newObstacle);

                    ObstacleViewModel obstacleViewModel = convertObstacleToViewModel(newObstacle, getActivity());
                    ObstacleDataSingleton.getInstance().setmObstacleViewModel(obstacleViewModel);

                    ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection = false;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ObstacleViewModel obstacleViewModel = convertObstacleToViewModel(ObstacleDataSingleton.getInstance().getmObstacle(), getActivity());
            ObstacleDataSingleton.getInstance().setmObstacleViewModel(obstacleViewModel);

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

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }

    private Obstacle getObstacleFrom(int pos) {

        Obstacle result;
        switch (String.valueOf(pos)) {
            case "0":
                result = new Stairs();
                break;
            case "1":
                result = new Ramp();
                break;
            case "2":
                result = new Unevenness();
                break;
            case "3":
                result = new Construction();
                break;
            case "4":
                result = new FastTrafficLight();
                break;
            case "5":
                result = new Elevator();
                break;
            case "6":
                result = new TightPassage();
                break;
            default:
                result = new Stairs();
                break;

        }
        result.setLatitude(ObstacleDataSingleton.getInstance().currentPositionOfSetObstacle.getLatitude());
        result.setLongitude(ObstacleDataSingleton.getInstance().currentPositionOfSetObstacle.getLongitude());

        return result;
    }

}
