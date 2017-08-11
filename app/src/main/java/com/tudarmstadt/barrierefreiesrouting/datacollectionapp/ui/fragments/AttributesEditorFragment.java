package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostObstacleToServerTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import bp.common.model.Obstacle;
import bp.common.model.Stairs;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.AttributeFragmentFactory.insertAttributeFragments;
import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleToViewConverter.convertObstacleToAttributeMap;

/**
 * This Fragment holds all Attributes that can be edited
 */
public class AttributesEditorFragment extends Fragment {

    private ObstacleViewModel obstacleViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.attributes_edit_fragment, container, false);

        final Obstacle obstacleToEdit = ((IObstacleProvider) getActivity()).getObstacle();

        obstacleViewModel = new ObstacleViewModel(convertObstacleToAttributeMap(obstacleToEdit, getActivity()), obstacleToEdit);

        insertAttributeFragments(this, obstacleViewModel);


        return v;
    }


}
