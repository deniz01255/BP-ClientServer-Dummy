package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextAttributeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextAttributeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextAttributeFragment extends Fragment {

    private static final String ATTRIBUTE_KEY_STRING_PARAM = "keyStringParam";

    private String mAttributeKeyString;

    private OnFragmentInteractionListener mListener;

    public static TextAttributeFragment newInstance(String attributeKeyString) {
        TextAttributeFragment fragment = new TextAttributeFragment();
        Bundle args = new Bundle();
        args.putString(ATTRIBUTE_KEY_STRING_PARAM, attributeKeyString);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAttributeKeyString = getArguments().getString(ATTRIBUTE_KEY_STRING_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.attribute_fragment_edit_text, container, false);

        TextInputLayout textEditLabel = (TextInputLayout) v.findViewById(R.id.input_label);

        textEditLabel.setHint(mAttributeKeyString);

        final EditText textEditInput = (EditText) v.findViewById(R.id.input_text);

        textEditInput.setText((String) ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).value);

        textEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateEditText(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).setValueFromString(textEditInput.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textEditInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).setValueFromString(textEditInput.getText().toString());
                }
            }
        });


        return v;
    }

    private void validateEditText(CharSequence s) {


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
