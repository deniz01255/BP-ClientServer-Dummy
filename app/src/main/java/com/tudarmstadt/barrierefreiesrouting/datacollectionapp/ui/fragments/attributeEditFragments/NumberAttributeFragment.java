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
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import org.apache.commons.lang3.NotImplementedException;

public class NumberAttributeFragment extends Fragment  {

    private static final String ATTRIBUTE_KEY_PARAM = "attributeKeyParam";
    private static IObstacleViewModelProvider obstacleViewModelProvider;

    private String mAttributeKeyString;

    private OnFragmentInteractionListener mListener;

    public NumberAttributeFragment() {
        // Required empty public constructor
    }

    public static NumberAttributeFragment newInstance(String labelName) {
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

        TextInputLayout textEditLabel = (TextInputLayout) v.findViewById(R.id.input_label);

        textEditLabel.setHint(mAttributeKeyString);

        final EditText textEditInput = (EditText) v.findViewById(R.id.input_text);

        if(ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).typeParameterClass == Integer.TYPE){
            textEditInput.setText(Integer.toString((Integer) ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).value));

        }else if(ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).typeParameterClass == Double.TYPE){
            textEditInput.setText(Double.toString( (Double) ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).value));

        }else{
            throw new NotImplementedException("Type not implemented");
        }


        textEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateEditText(s);
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
                    ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).setValueFromString(textEditInput.getText().toString());
                }
            }
        });

        textEditLabel.setHint(mAttributeKeyString);

        return v;
    }

    // TODO: handle verification
    private void validateEditText(CharSequence s){

        String temp = s.toString();

        if (!temp.matches("[0-9]+") ) {
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
