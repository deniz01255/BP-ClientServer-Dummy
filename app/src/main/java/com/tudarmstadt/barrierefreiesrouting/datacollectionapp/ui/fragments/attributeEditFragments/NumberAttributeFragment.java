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
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelProvider;

public class NumberAttributeFragment extends Fragment  {

    private static final String ATTRIBUTE_KEY_PARAM = "attributeKeyParam";
    private static IObstacleViewModelProvider obstacleViewModelProvider;

    private String mAttributeKeyString;

    private OnFragmentInteractionListener mListener;

    public NumberAttributeFragment() {
        // Required empty public constructor
    }

    public static NumberAttributeFragment newInstance(String labelName, IObstacleViewModelProvider obstacleViewModelProvider) {
        NumberAttributeFragment.obstacleViewModelProvider = obstacleViewModelProvider;
        NumberAttributeFragment fragment = new NumberAttributeFragment();
        Bundle args = new Bundle();
        args.putString(ATTRIBUTE_KEY_PARAM, labelName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAttributeKeyString = getArguments().getString(ATTRIBUTE_KEY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.attribute_fragment_edit_number, container, false);

        TextInputLayout label = (TextInputLayout) v.findViewById(R.id.input_label_number);

        TextInputLayout textEditLabel = (TextInputLayout) v.findViewById(R.id.input_label);

        textEditLabel.setHint(mAttributeKeyString);

        final EditText textEditInput = (EditText) v.findViewById(R.id.input_text);

        textEditInput.setText((String) obstacleViewModelProvider.getViewModel().attributesMap.get(mAttributeKeyString).value);

        textEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textEditInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    obstacleViewModelProvider.getViewModel().attributesMap.get(mAttributeKeyString).setValueFromString(textEditInput.getText().toString());
                }
            }
        });

        label.setHint(mAttributeKeyString);

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
