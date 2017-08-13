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
import android.widget.Toast;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;

/**
 * Created by vincent on 8/11/17.
 */

public class SelectObstacleTypeFragment extends Fragment implements Step, AdapterView.OnItemSelectedListener{

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();



    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
