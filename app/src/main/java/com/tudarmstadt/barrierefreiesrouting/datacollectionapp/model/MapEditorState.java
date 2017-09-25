package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator.PlaceNearestRoadsOnMapOperator;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IUserInteractionWithMap;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.BrowseMapActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;


/**
 *
 */

public class MapEditorState {

    /**
     * The active Map operator that is used to handle long press and single tap events on the map.
     */
    private IUserInteractionWithMap activeOperator;


    public MapEditorState(BrowseMapActivity browseMapActivity, MapEditorFragment mapEditorFragment) {
        activeOperator = new PlaceNearestRoadsOnMapOperator();
    }

    public IUserInteractionWithMap getActiveOperator() {
        return activeOperator;
    }

    public void setActiveOperator(IUserInteractionWithMap activeOperator) {
        this.activeOperator = activeOperator;
    }


}
