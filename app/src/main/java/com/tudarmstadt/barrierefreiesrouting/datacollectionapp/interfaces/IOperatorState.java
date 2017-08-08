package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;

/**
 * Operators are used by the User (or a Service) in order to change the application state.
 *
 * Operators are initialized before they are ready for usage.
 * Before a new Operator can start, the currently active Operator must be disposed.
 */
public interface IOperatorState {

    boolean init();

    boolean dispose();

    boolean longPressHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment);

    boolean singleTapConfirmedHelper(GeoPoint p, MainActivity context, MapEditorFragment mapEditorFragment);

}
