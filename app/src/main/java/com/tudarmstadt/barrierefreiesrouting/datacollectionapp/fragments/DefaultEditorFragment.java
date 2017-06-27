package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DefaultEditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DefaultEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultEditorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DefaultEditorFragment() {
        // Required empty public constructor
    }

    public static DefaultEditorFragment newInstance() {
        DefaultEditorFragment fragment = new DefaultEditorFragment();
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

        TextView textView = (TextView) getActivity().findViewById(R.id.default_editor_textview);


        textView.setText("Longpress on the Map to Add a new Barrier");




        return inflater.inflate(R.layout.fragment_default_editor, container, false);
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
