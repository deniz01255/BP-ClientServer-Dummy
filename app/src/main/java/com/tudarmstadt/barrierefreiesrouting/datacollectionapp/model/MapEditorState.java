package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

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
import java.util.Collection;

import bp.common.model.obstacles.Obstacle;


/**
 * Created by Vincent on 08.08.2017.
 */

public  class MapEditorState {


    private static MapEditorState INSTANCE;





    private IOperatorState activeOperator;
    private ClearAllOperator clearAllOperator;


    public MapEditorState(BrowseMapActivity browseMapActivity, MapEditorFragment mapEditorFragment) {
        clearAllOperator = new ClearAllOperator(mapEditorFragment);
        activeOperator = new PlaceObstacleOperatorState();
    }

    public IOperatorState getActiveOperator() {
        return activeOperator;
    }


    public ClearAllOperator getClearAllOperator() {
        return clearAllOperator;
    }


}
