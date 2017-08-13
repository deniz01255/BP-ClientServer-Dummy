package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces.IObstacleViewModelProvider;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model.ObstacleDataSingleton;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.ObstacleDetailsViewerFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.attributeEditFragments.TextAttributeFragment;

import java.util.Map;

/**
 * Created by Vincent on 27.06.2017.
 */
public class AttributeFragmentFactory {

    /**
     * Inserts Attribute Fragments into the given fragment. All Attributes are listed in the
     * obstacleViewModel.
     *
     * Depending on the typeParameterClass of the entry, a Number- Text- or CheckBox-
     * AttributeFragment will ne inserted in the fragment container.
     * @param fragment the fragment container, where to insert the attributes
     * @param obstacleViewModel contains the attributes mapping
     */
    public static void insertAttributeEditFragments(Fragment fragment) {

        Map<String, ObstacleAttribute<?>> obstacleAttributeMap = ObstacleDataSingleton.getInstance().getmObstacleViewModel().attributesMap;

        for (Map.Entry<String, ObstacleAttribute<?>> entry : obstacleAttributeMap.entrySet()) {
            if (entry.getValue().typeParameterClass == Double.TYPE) {
                CommitFragment(fragment, NumberAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            } else if (entry.getValue().typeParameterClass == Integer.TYPE) {
                CommitFragment(fragment, NumberAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            } else if (entry.getValue().typeParameterClass == String.class) {
                CommitFragment(fragment, TextAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            } else if (entry.getValue().typeParameterClass == Boolean.TYPE) {
                CommitFragment(fragment, CheckBoxAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            }
        }
    }


    private static void CommitFragment(Fragment parentFragment, Fragment newFragment, String tag) {

        FragmentManager fragMan = parentFragment.getChildFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        fragTransaction.add(R.id.editor_attribute_list_container, newFragment, tag);
        fragTransaction.commit();
    }

}
