package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.appstate;

import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.ClearAllOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.PlaceObstacleOperatorState;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import bp.common.model.Obstacle;

/**
 * Created by Vincent on 08.08.2017.
 */

public class StateHandler {

    private IOperatorState activeOperator;
    private ClearAllOperator clearAllOperator;

    private MainActivity mainActivity;

    private ArrayList<Polyline> currentRoadOverlays = new ArrayList<>();

    private Obstacle newObstacle;
    private GeoPoint newObstaclePosition;

    private String AppBarTitle = "";

    public StateHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        clearAllOperator = new ClearAllOperator(mainActivity);
    }

    public void setupNextState(IOperatorState nextState) {
        if(mainActivity.mapEditorFragment != null && mainActivity.mapEditorFragment.placeNewObstacleOverlay != null)
            mainActivity.mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        if(activeOperator != null)
            activeOperator.dispose();

        activeOperator = nextState;

        updateNavigationBarState();
        AppBarTitle = nextState.getTopBarTitle();
        mainActivity.toolbar.setTitle(AppBarTitle);

        activeOperator.init();

    }

    public void updateNavigationBarState() {

        if (newObstaclePosition != null) {
            mainActivity.navigationToolbar.getMenu().getItem(1).setEnabled(true);
            mainActivity.navigationToolbar.invalidate();

        } else {
            mainActivity.navigationToolbar.getMenu().getItem(1).setEnabled(false);
            mainActivity.navigationToolbar.invalidate();
        }

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
}
