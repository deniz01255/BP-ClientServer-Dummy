package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import org.osmdroid.util.GeoPoint;

/**
 * Operators are used by the User (or a Service) in order to change the application state.
 * <p>
 * Operators are initialized before they are ready for usage.
 * Before a new Operator can start, the currently active Operator must be disposed.
 */
public interface IUserInteractionWithMap {

    boolean longPressHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment);

    boolean singleTapConfirmedHelper(GeoPoint p, Activity context, MapEditorFragment mapEditorFragment);


}
