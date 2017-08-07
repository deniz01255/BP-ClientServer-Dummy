package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;

import java.lang.reflect.Field;
import java.util.HashMap;

import bp.common.model.Obstacle;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.AttributeFragmentFactory.insertAttributeFragments;
import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleToViewConverter.convertObstacleToAttributeMap;

public class ObstacleDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout myLayout;

    private LinearLayout.LayoutParams defaultParams;

    private HashMap<Field, View> mapping;

    private Obstacle obstacleToEdit;

    private ObstacleViewModel obstacleViewModel;

    public ObstacleDetailsFragment() {
    }

    public static ObstacleDetailsFragment newInstance(Obstacle obstacle) {
        ObstacleDetailsFragment fragment = new ObstacleDetailsFragment();
        Bundle args = new Bundle();

        fragment.obstacleToEdit = obstacle;
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.obstacle_details_dialog, container, false);

        obstacleToEdit = ((IObstacleProvider) getActivity()).getObstacle();

        LinearLayout.LayoutParams defaultLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams commitButtonLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View pl = v.findViewById(R.id.editor_attribute_list_container);

        TextView heading = new TextView(getActivity());

        heading.setLayoutParams(defaultLinearLayoutParams);
        heading.setText("Edit " + obstacleToEdit.getTypeName());

        // Heading at the Top
        ((LinearLayout) pl).addView(heading);

        obstacleViewModel = new ObstacleViewModel(convertObstacleToAttributeMap(obstacleToEdit, getActivity()), obstacleToEdit);

        // Edit Fragments between heading and Commit Button
        insertAttributeFragments(this, obstacleViewModel);

        Button commitButton = new Button(getActivity());
        commitButton.setLayoutParams(commitButtonLinearLayoutParams);
        commitButton.setText("Commit");

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //obstacleViewModel.commit(getActivity(), ((IMapFragmentProvider) getActivity()).getMapEditorFragment());
                Toast.makeText(getActivity(), "Obstacle saved", Toast.LENGTH_LONG);
            }
        });

        // Commit Button to the bottom
        ((LinearLayout) pl).addView(commitButton);

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
