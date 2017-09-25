package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostStreetToServerTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.stepperFragments.AddObstacleStepperAdapter;

import java.util.ArrayList;

import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Stairs;
import bp.common.model.ways.Node;
import bp.common.model.ways.Way;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PLaceRoadActivity extends AppCompatActivity implements StepperLayout.StepperListener, MapEditorFragment.OnFragmentInteractionListener,
        TextAttributeFragment.OnFragmentInteractionListener, CheckBoxAttributeFragment.OnFragmentInteractionListener, NumberAttributeFragment.OnFragmentInteractionListener {

    public BrowseMapActivity browseMapActivity;
    ArrayList<Node> nodex = new ArrayList<>();
    private StepperLayout mStepperLayout;
    private int selectedBarrier;
    private int i;
    private String roadString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_obstacle);
        Intent intent = getIntent();
        nodex.clear();
        String xx = intent.getExtras().getString("key");
        ArrayList<Character> lat = new ArrayList<>();
        ArrayList<Character> lo = new ArrayList<>();
        ArrayList<Node> nodes = new ArrayList<>();


        String[] tokens = xx.split(",");
        for (String s : tokens) {
            String[] tokenss = s.split(";");

            String lati = tokenss[0];
            String longi = tokenss[1];

            Node n = new Node((Double.parseDouble(tokenss[0])), (Double.parseDouble(tokenss[1])));
            nodes.add(n);
        }


        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new AddObstacleStepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);


        ObstacleDataSingleton.getInstance().setObstacle(new Stairs());

        nodex = nodes;

        Way w = new Way("Neue Straße", nodex);
        PostStreetToServerTask.PostStreet(w);

    }


    @Override
    public void onCompleted(View view) {
        Way w = new Way("Neue Straße", nodex);
        PostStreetToServerTask.PostStreet(w);


        Toast.makeText(this, R.string.Way_saved, Toast.LENGTH_SHORT).show();
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
