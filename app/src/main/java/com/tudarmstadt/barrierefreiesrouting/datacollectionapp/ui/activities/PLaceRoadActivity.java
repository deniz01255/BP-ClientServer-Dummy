package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
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
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PLaceRoadActivity extends AppCompatActivity implements StepperLayout.StepperListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener, CheckBoxAttributeFragment.OnFragmentInteractionListener, NumberAttributeFragment.OnFragmentInteractionListener {

    private StepperLayout mStepperLayout;
    private int selectedBarrier;

    public BrowseMapActivity browseMapActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_road);
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


    private Obstacle getObstacleFromSelection(long selectedItemId) {
        return null;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
