package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextAttributeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextAttributeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextAttributeFragment extends Fragment {

    private static final String LABEL_PARAM = "labelParam";

    private String mLabelParam;

    private OnFragmentInteractionListener mListener;

    public TextAttributeFragment() {
        // Required empty public constructor
    }


    public static TextAttributeFragment newInstance(String labelName) {
        TextAttributeFragment fragment = new TextAttributeFragment();
        Bundle args = new Bundle();
        args.putString(LABEL_PARAM, labelName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLabelParam = getArguments().getString(LABEL_PARAM);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_text_attribute, container, false);

        TextView label = (TextView) v.findViewById(R.id.text_attribute_label);

        label.setText(mLabelParam);



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
