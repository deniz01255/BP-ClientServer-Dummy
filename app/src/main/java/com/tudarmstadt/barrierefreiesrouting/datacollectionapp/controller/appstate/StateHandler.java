package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.BrowseMapActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.ClearAllOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.PlaceObstacleOperatorState;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import bp.common.model.obstacles.Obstacle;


/**
 * Created by Vincent on 08.08.2017.
 */

public  class StateHandler {

    private IOperatorState activeOperator;
    private ClearAllOperator clearAllOperator;

    private MapEditorFragment mapEditorFragment;

    private ArrayList<Polyline> currentRoadOverlays = new ArrayList<>();

    private Obstacle newObstacle;
    private GeoPoint newObstaclePosition;

    private ObstacleViewModel obstacleViewModel;


    private String AppBarTitle = "";

    public StateHandler(BrowseMapActivity browseMapActivity, MapEditorFragment mapEditorFragment) {
        clearAllOperator = new ClearAllOperator(mapEditorFragment);
        this.mapEditorFragment = mapEditorFragment;
        activeOperator = new PlaceObstacleOperatorState(mapEditorFragment, browseMapActivity);
    }

    public void setupNextState(IOperatorState nextState) {
        if (mapEditorFragment != null && mapEditorFragment.placeNewObstacleOverlay != null)
            mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        if (activeOperator != null)
            activeOperator.dispose();

        activeOperator = nextState;

        activeOperator.init();

    }


    public IOperatorState getActiveOperator() {
        return activeOperator;
    }

    public void replaceActiveOperator(IOperatorState activeOperator) {
        this.activeOperator = activeOperator;
    }

    public Obstacle getNewObstacle() {
        return newObstacle;
    }

    public void setNewObstacle(Obstacle newObstacle) {
        this.newObstacle = newObstacle;
    }

    public ArrayList<Polyline> getCurrentRoadOverlays() {
        return currentRoadOverlays;
    }

    public GeoPoint getNewObstaclePosition() {
        return newObstaclePosition;
    }

    public void setNewObstaclePosition(GeoPoint newObstaclePosition) {
        this.newObstaclePosition = newObstaclePosition;
    }

    public ClearAllOperator getClearAllOperator() {
        return clearAllOperator;
    }

    public Overlay getPlaceNewObstacleOverlay() {
        return mapEditorFragment.placeNewObstacleOverlay;
    }

    public ObstacleViewModel getObstacleViewModel() {
        return obstacleViewModel;
    }

    public void setObstacleViewModel(ObstacleViewModel obstacleViewModel) {
        this.obstacleViewModel = obstacleViewModel;
    }
}
