package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.BrowseMapActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.ClearAllOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators.PlaceObstacleOperatorState;


/**
 * Created by Vincent on 08.08.2017.
 */

public  class MapEditorState {


    private static MapEditorState INSTANCE;





    private IUserInteractionWithMap activeOperator;
    private ClearAllOperator clearAllOperator;


    public MapEditorState(BrowseMapActivity browseMapActivity, MapEditorFragment mapEditorFragment) {
        clearAllOperator = new ClearAllOperator(mapEditorFragment);
        activeOperator = new PlaceObstacleOperatorState();
    }

    public IUserInteractionWithMap getActiveOperator() {
        return activeOperator;
    }


    public ClearAllOperator getClearAllOperator() {
        return clearAllOperator;
    }


}
