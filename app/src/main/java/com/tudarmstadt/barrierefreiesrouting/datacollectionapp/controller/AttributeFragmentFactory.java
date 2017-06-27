package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.DefaultEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.attributeEditFragments.TextAttributeFragment;

import bp.common.model.IObstacle;


/**
 * Created by Vincent on 27.06.2017.
 */

public class AttributeFragmentFactory {
    private static final int CONTENT_VIEW_ID = 10101010;


    public static void insertAttributeFragements(Fragment fragment, ObstacleViewModel obstacleViewModel){


        FragmentManager fragMan = fragment.getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        TextAttributeFragment myFrag = new TextAttributeFragment();

        fragTransaction.add(R.id.editor_attribute_list_container, myFrag);
        fragTransaction.commit();




    }


}
