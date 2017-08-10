package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.DownloadObstaclesTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.network.PostObstacleToServerTask;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.activities.MainActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import java.lang.reflect.Field;
import java.util.HashMap;

import bp.common.model.Obstacle;
import bp.common.model.Stairs;

import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.AttributeFragmentFactory.insertAttributeFragments;
import static com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleToViewConverter.convertObstacleToAttributeMap;

public class ObstacleDetailsFragment extends DialogFragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout myLayout;

    private LinearLayout.LayoutParams defaultParams;

    private HashMap<Field, View> mapping;

    private ObstacleViewModel obstacleViewModel;

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

        View v = inflater.inflate(R.layout.dialog_fragment_obstacle_details, container, false);

        final Obstacle obstacleToEdit = ((IObstacleProvider) getActivity()).getObstacle();

        LinearLayout.LayoutParams commitButtonLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        View pl = v.findViewById(R.id.editor_attribute_list_container);

        getDialog().setTitle("Edit " + obstacleToEdit.getTypeName());

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
                PostObstacleToServerTask.PostStairs((Stairs) obstacleToEdit);

                Toast.makeText(getActivity(), getActivity().getString(R.string.action_barrier_added),
                        Toast.LENGTH_SHORT).show();
                OverlayItem overlayItem = new OverlayItem(obstacleToEdit.getName(), getActivity().getString(R.string.default_description), new GeoPoint(obstacleToEdit.getLatitude(), obstacleToEdit.getLongitude()));
                overlayItem.setMarker(getActivity().getResources().getDrawable(R.mipmap.ramppic));


                View contextView = getActivity().findViewById(R.id.placeSnackBar);
                Snackbar.make(contextView, R.string.server_response_posted_new_obstacle, Snackbar.LENGTH_SHORT)
                        .show();

                getDialog().cancel();
            }
        });

        // Commit Button to the bottom
        ((LinearLayout) pl).addView(commitButton);

        return v;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
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
