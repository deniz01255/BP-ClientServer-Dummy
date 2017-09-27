package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments.AddObstacleStepperAdapter;

import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Stairs;


/**
 * This activity provides a Stepper which is responsible to step through the
 * data collection process.
 */
public class PlaceObstacleActivity extends AppCompatActivity implements StepperLayout.StepperListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener, CheckBoxAttributeFragment.OnFragmentInteractionListener, NumberAttributeFragment.OnFragmentInteractionListener {

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_obstacle);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new AddObstacleStepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);

        ObstacleDataSingleton.getInstance().setObstacle(new Stairs());

    }

    @Override
    public void onCompleted(View view) {
        Toast.makeText(this, R.string.Obstacle_saved, Toast.LENGTH_SHORT).show();
        this.finish();

    }

    @Override
    public void onError(VerificationError verificationError) {

        ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection = false;
    }

    @Override
    public void onStepSelected(int i) {
        ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection = false;

    }

    @Override
    public void onReturn() {

        ObstacleDataSingleton.getInstance().editorIsSyncedWithSelection = false;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
