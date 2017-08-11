package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate.StateHandler;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments.AddObstacleStepperAdapter;

import org.osmdroid.util.GeoPoint;

import bp.common.model.Obstacle;

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
    }


        @Override
        public void onCompleted (View view){
        finish();
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
