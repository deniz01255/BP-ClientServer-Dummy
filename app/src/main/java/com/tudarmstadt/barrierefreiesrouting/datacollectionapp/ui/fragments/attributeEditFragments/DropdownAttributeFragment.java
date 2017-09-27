package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vincent on 22.09.17.
 */

public class DropdownAttributeFragment extends Fragment implements AdapterView.OnItemSelectedListener {


    private static final String ATTRIBUTE_KEY_STRING_PARAM = "keyStringParam";
    private static final String VALID_OPTIONS_STRING_PARAM = "validOptionsParam";
    private static final String NAME_STRING_PARAM = "nameStringParam";

    private String mAttributeKeyString;
    private String mValidOptionsString;
    private String mAttributeLabelName;

    private TextAttributeFragment.OnFragmentInteractionListener mListener;
    List<String> items;
    public static DropdownAttributeFragment newInstance(String attributeKeyString, String validOptionsString, String nameString) {
        DropdownAttributeFragment fragment = new DropdownAttributeFragment();
        Bundle args = new Bundle();
        args.putString(ATTRIBUTE_KEY_STRING_PARAM, attributeKeyString);
        args.putString(VALID_OPTIONS_STRING_PARAM, validOptionsString);
        args.putString(NAME_STRING_PARAM, nameString);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAttributeKeyString = getArguments().getString(ATTRIBUTE_KEY_STRING_PARAM);
            mValidOptionsString = getArguments().getString(VALID_OPTIONS_STRING_PARAM);
            mAttributeLabelName = getArguments().getString(NAME_STRING_PARAM);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.attribute_fragment_edit_dropdown, container, false);

        // create items from csv string
        items = Arrays.asList(mValidOptionsString.split("\\s*,\\s*"));

        // TODO: get the items from the obstacle attribute annotation "valid options"
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner_attribute_selection);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        TextView label = (TextView) v.findViewById(R.id.dropDownAttributeLabel);

        label.setText(mAttributeLabelName);


        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TextAttributeFragment.OnFragmentInteractionListener) {
            mListener = (TextAttributeFragment.OnFragmentInteractionListener) context;
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap.get(mAttributeKeyString).setValueFromString(items.get(i));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
