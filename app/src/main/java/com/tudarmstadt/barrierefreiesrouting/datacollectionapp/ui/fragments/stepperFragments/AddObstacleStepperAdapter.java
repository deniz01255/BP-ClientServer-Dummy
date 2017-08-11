package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

/**
 * Created by vincent on 8/11/17.
 */

public class AddObstacleStepperAdapter extends AbstractFragmentStepAdapter {


    public AddObstacleStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(@IntRange(from = 0L) int position) {



        final SelectObstacleTypeFragment step = new SelectObstacleTypeFragment();
        Bundle b = new Bundle();
        b.putInt("First", position);
        step.setArguments(b);


        return step;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
