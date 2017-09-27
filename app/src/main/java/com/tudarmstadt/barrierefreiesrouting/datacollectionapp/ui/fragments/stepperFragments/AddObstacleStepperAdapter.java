package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

/**
 * Simple Implementation of Material-Stepper.
 * <p>
 * Two Steps are initialized in the createStep Method.
 * <p>
 * Please review the README from https://github.com/stepstone-tech/android-material-stepper
 */
public class AddObstacleStepperAdapter extends AbstractFragmentStepAdapter {


    public AddObstacleStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {


        switch (position) {
            case 0:
                return (Step) getStep(position, new AttributesEditorFragment());
            case 1: {
                return (Step) getStep(position, new OverviewSendFragment());

            }
        }

        return null;
    }

    @NonNull
    private Fragment getStep(@IntRange(from = 0L) int position, Fragment fragment) {
        Bundle b = new Bundle();
        b.putInt("First", position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
