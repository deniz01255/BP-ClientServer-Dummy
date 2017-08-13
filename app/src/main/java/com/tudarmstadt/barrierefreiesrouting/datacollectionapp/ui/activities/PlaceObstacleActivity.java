package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments.AddObstacleStepperAdapter;

import bp.common.model.Obstacle;
import bp.common.model.Stairs;


/**
 * Created by vincent on 8/11/17.
 */

public class PlaceObstacleActivity extends AppCompatActivity implements StepperLayout.StepperListener, AdapterView.OnItemSelectedListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener, CheckBoxAttributeFragment.OnFragmentInteractionListener, NumberAttributeFragment.OnFragmentInteractionListener
        , IObstacleProvider {

        private StepperLayout mStepperLayout;


    @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_obstacle);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new AddObstacleStepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);

        ObstacleDataSingleton.getInstance().setmObstacle(new Stairs());

    }


        @Override
        public void onCompleted (View view){
            Toast.makeText(this, R.string.Obstacle_saved, Toast.LENGTH_SHORT).show();
            this.finish();

    }

        @Override
        public void onError (VerificationError verificationError){

    }

        @Override
        public void onStepSelected ( int i){

    }

        @Override
        public void onReturn () {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public Obstacle getObstacle() {
        return null;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
