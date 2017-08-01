package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.ui.fragments.MapEditorFragment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import bp.common.model.Obstacle;
import bp.common.model.annotations.EditableAttribute;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleViewModel {

    public Map<String, ObstacleAttribute<?>> attributesMap = new HashMap<>();

    private Obstacle mObstacleData;

    public ObstacleViewModel(Map<String, ObstacleAttribute<?>> attributes, Obstacle obstacle){
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

    }

    public Obstacle getmObstacleData() {
        return mObstacleData;
    }
}