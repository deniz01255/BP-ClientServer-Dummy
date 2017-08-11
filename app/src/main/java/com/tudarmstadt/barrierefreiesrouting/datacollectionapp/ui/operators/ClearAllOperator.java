package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.operators;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IOperatorState;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vincent on 08.08.2017.
 */

public class ClearAllOperator {

    private MainActivity mainActivity;

    public ClearAllOperator(MainActivity mainActivity){

        this.mainActivity = mainActivity;
    }

    public void clearAll(){
        // Go back to place obstacle OperatorState
        mainActivity.getStateHandler().replaceActiveOperator(new PlaceObstacleOperatorState(mainActivity));

        // reset the position
        mainActivity.getStateHandler().setNewObstaclePosition(null);
        // reset the obstacle
        mainActivity.getStateHandler().setNewObstacle(null);

        // remove temporary edit obstacle items on the road overlay
        mainActivity.mapEditorFragment.placeNewObstacleOverlay.removeAllItems();

        // remove the roads overlay
        mainActivity.mapEditorFragment.map.getOverlays().removeAll(mainActivity.getStateHandler().getCurrentRoadOverlays());


        mainActivity.mapEditorFragment.map.invalidate();
    }



}
