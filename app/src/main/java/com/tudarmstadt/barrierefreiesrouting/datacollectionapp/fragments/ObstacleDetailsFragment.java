package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.activities.IObstacleProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.ObstacleToViewConverter;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments.TextAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


import bp.common.model.IObstacle;
import bp.common.model.Obstacle;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.AttributeFragmentFactory.insertAttributeFragements;
import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.ObstacleToViewConverter.convertObstacleToAttributeMap;

public class ObstacleDetailsFragment extends Fragment  {

    private OnFragmentInteractionListener mListener;
    private LinearLayout myLayout;

    private LinearLayout.LayoutParams defaultParams;

    private HashMap<Field, View> mapping;

    private IObstacle obstacleToEdit;


    public ObstacleDetailsFragment() {
    }

    public static ObstacleDetailsFragment newInstance() {
        ObstacleDetailsFragment fragment = new ObstacleDetailsFragment();
        Bundle args = new Bundle();

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

        View v =  inflater.inflate(R.layout.fragment_obstacle_details, container, false);
        obstacleToEdit = ((IObstacleProvider)getActivity()).getObstacle();
        LinearLayout.LayoutParams defaultLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View pl = v.findViewById(R.id.editor_attribute_list_container);

        //mapping = ObstacleToViewConverter.convert( obstacleToEdit, getContext());


        TextView heading = new TextView(getContext());

        heading.setLayoutParams(defaultLinearLayoutParams);
        heading.setText("Edit " + obstacleToEdit.getTypeName());

        ((LinearLayout)pl).addView(heading);

      /*  for(Map.Entry<Field, View> entry: mapping.entrySet()) {

            View attributeViewElement = entry.getValue();
            attributeViewElement.setLayoutParams(defaultLinearLayoutParams);
            ((LinearLayout)pl).addView(attributeViewElement);
        }*/

        Button commitButton = new Button(getContext());

        commitButton.setLayoutParams(defaultLinearLayoutParams);
        commitButton.setText("Commit");

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              // BpServerHandler.PostNewObstacle(getActivity(), this, obstacleToEdit);
                // return true;

            }
        });

        ((LinearLayout)pl).addView(commitButton);
        ObstacleViewModel obstacleViewModel = new ObstacleViewModel(convertObstacleToAttributeMap(obstacleToEdit, getContext()));
        insertAttributeFragements(this, obstacleViewModel);


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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void doShit(){

    }


}
