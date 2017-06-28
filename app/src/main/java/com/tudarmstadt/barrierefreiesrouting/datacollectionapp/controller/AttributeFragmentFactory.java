package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.DefaultEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments.CheckBoxAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments.NumberAttributeFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments.TextAttributeFragment;

import java.util.Map;

import bp.common.model.IObstacle;


/**
 * Created by Vincent on 27.06.2017.
 */

public class AttributeFragmentFactory {
    private static final int CONTENT_VIEW_ID = 10101010;


    public static void insertAttributeFragments(Fragment fragment, ObstacleViewModel obstacleViewModel){

        Map<String, ObstacleAttribute<?>> obstacle = obstacleViewModel.attributesMap;

        for (Map.Entry<String, ObstacleAttribute<?>> entry : obstacle.entrySet())
        {
            if(entry.getValue().typeParameterClass == Double.TYPE){
                CommitFragment(fragment, NumberAttributeFragment.newInstance(entry.getKey()) , entry.getKey());
            }
            else if(entry.getValue().typeParameterClass == Integer.TYPE){
                CommitFragment(fragment,  NumberAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            }
            else if(entry.getValue().typeParameterClass == String.class){
                CommitFragment(fragment, TextAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            }
            else if(entry.getValue().typeParameterClass == Boolean.TYPE){
                CommitFragment(fragment, CheckBoxAttributeFragment.newInstance(entry.getKey()), entry.getKey());
            }
        }
    }

    private static void CommitFragment(Fragment parentFragment, Fragment newFragment ,String tag) {
        FragmentManager fragMan = parentFragment.getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        fragTransaction.add(R.id.editor_attribute_list_container, newFragment, tag);
        fragTransaction.commit();
    }

}
