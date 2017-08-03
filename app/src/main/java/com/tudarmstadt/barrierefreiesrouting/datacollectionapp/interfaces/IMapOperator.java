package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;

/**
 * Created by Vincent on 31.07.2017.
 */

public interface IMapOperator extends IOperator {

    boolean longPressHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment);

    boolean singleTapConfirmedHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment);

}
