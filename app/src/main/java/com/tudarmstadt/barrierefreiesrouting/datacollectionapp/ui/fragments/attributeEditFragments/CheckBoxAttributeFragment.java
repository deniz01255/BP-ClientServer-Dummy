package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckBoxAttributeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckBoxAttributeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckBoxAttributeFragment extends Fragment{

    private static final String LABEL_PARAM = "labelParam";

    private String mLabelParam;

    private OnFragmentInteractionListener mListener;

    public CheckBoxAttributeFragment() {
        // Required empty public constructor
    }

    public static CheckBoxAttributeFragment newInstance(String labelName) {
        CheckBoxAttributeFragment fragment = new CheckBoxAttributeFragment();
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

        View v = inflater.inflate(R.layout.attribute_fragment_edit_bool, container, false);

        TextView label = (TextView) v.findViewById(R.id.checkbox_attribute_label);

        label.setText(mLabelParam);

        return v;
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
