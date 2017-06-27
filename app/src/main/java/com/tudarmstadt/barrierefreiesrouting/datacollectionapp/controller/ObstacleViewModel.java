package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;
import bp.common.model.Stairs;
import bp.common.model.annotations.EditableAttribute;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleViewModel {

    private Map<String, ObstacleAttribute<?>> attributesMap = new HashMap<>();

    private IObstacle mObstacleData;

    public ObstacleViewModel(Map<String, ObstacleAttribute<?>> attributes, IObstacle obstacle){
        attributesMap = attributes;
        mObstacleData = obstacle;
    }

    public void commit(Activity activity, MapEditorFragment mapEditorFragment){

        Field[] obstacleFields = mObstacleData.getClass().getDeclaredFields();

        for (Field field : obstacleFields){

            String tag = field.getAnnotation(EditableAttribute.class).value();

            if(attributesMap.get(tag) != null){

                try {
                    field.set(mObstacleData, attributesMap.get(tag) );
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }



        BpServerHandler.PostNewObstacle(activity, mapEditorFragment, mObstacleData);

    }
}