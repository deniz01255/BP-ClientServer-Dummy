package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.BrowseMapActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

/**
 * Created by Vincent on 08.08.2017.
 */

public class ClearAllOperator {

    private MapEditorFragment mapEditorFragment;

    public ClearAllOperator(MapEditorFragment mapEditorFragment){

        this.mapEditorFragment = mapEditorFragment;
    }

    public void clearAll(){
        // Go back to place obstacle OperatorState
        mapEditorFragment.getStateHandler().replaceActiveOperator(new PlaceObstacleOperatorState(mapEditorFragment));

        // reset the position
        mapEditorFragment.getStateHandler().setNewObstaclePosition(null);
        // reset the obstacle
        mapEditorFragment.getStateHandler().setNewObstacle(null);

        // remove temporary edit obstacle items on the road overlay
        mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        // remove the roads overlay
        mapEditorFragment.map.getOverlays().removeAll(mapEditorFragment.getStateHandler().getCurrentRoadOverlays());


        mapEditorFragment.map.invalidate();
    }



}
