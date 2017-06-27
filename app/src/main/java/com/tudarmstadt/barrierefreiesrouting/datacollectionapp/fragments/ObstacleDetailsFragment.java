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

public class ObstacleDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout myLayout;

    private LinearLayout.LayoutParams defaultParams;


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


        View pl = v.findViewById(R.id.EditViewList);
        TextView Paper = new TextView(pl.getContext());
        Paper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Paper.setText("Inserted TestText");
        ((LinearLayout)pl).addView(Paper);


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



}
