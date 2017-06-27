package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckBoxAttributeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckBoxAttributeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckBoxAttributeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CheckBoxAttributeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CheckBoxAttributeFragment newInstance(String param1, String param2) {
        CheckBoxAttributeFragment fragment = new CheckBoxAttributeFragment();
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

        View v = inflater.inflate(R.layout.fragment_check_box_attribute, container, false);





        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
