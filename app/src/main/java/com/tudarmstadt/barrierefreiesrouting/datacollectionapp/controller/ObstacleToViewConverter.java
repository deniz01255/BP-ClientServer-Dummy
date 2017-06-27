package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.app.Fragment;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bp.common.model.IObstacle;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleToViewConverter {

    interface Converter {
        ObstacleAttribute<?> convert(Object value, Context ctx);
    }
    private static Map<Class, Converter> converterForClass = new HashMap<>();

    static {
        converterForClass.put(Long.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {

                ObstacleAttribute<Long> oa = new ObstacleAttribute<Long>();
                oa.value = (Long) value;
                return oa;
            }
        });
        converterForClass.put(Integer.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {
                ObstacleAttribute<Integer> oa = new ObstacleAttribute<Integer>();
                oa.value = (Integer) value;
                return oa;
            }
        });
        converterForClass.put(Double.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {
                ObstacleAttribute<Double> oa = new ObstacleAttribute<Double>();
                oa.value = (Double) value;
                return oa;

            }
        });
        converterForClass.put(String.class, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {
                ObstacleAttribute<String> oa = new ObstacleAttribute<String>();
                oa.value = (String) value;
                return oa;
            }
        });
    };

    public static HashMap<Field, ObstacleAttribute<?>> convertObstacleToAttributeMap(IObstacle obstacle, Context ctx){


        Field[] fieldsOfObstacle = obstacle.getClass().getDeclaredFields();
        HashMap<Field, ObstacleAttribute<?>>  map = new HashMap<Field, ObstacleAttribute<?>>();

        for (Field f : fieldsOfObstacle ) {
            if(converterForClass.get(f.getType()) != null)
                try {
                    map.put(f, converterForClass.get(f.getType()).convert(f.get(obstacle), ctx));


                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }

        return map;
    }



}
