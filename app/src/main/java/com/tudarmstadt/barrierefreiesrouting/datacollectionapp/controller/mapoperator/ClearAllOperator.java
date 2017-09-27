package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.mapoperator;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

/**
 * TODO: Implement clear All Method
 * Can not be used right now.
 */
public class ClearAllOperator {

    private MapEditorFragment mapEditorFragment;

    public ClearAllOperator(MapEditorFragment mapEditorFragment) {

        this.mapEditorFragment = mapEditorFragment;
    }

    public void clearAll() {
        // Go back to place obstacle OperatorState
        //mapEditorFragment.getStateHandler().replaceActiveOperator(new PlaceNearestRoadsOnMapOperator(mapEditorFragment));

        // remove temporary edit obstacle items on the road overlay
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();


        mapEditorFragment.map.invalidate();
    }


}
