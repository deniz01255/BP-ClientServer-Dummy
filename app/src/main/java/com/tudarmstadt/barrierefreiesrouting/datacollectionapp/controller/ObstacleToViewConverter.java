package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bp.common.model.Obstacle;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleToViewConverter {

    interface Converter {
        View convert(Context ctx);
    }
    private static Map<Class,Converter> converterForClass = new HashMap<>();

    static {
        converterForClass.put(Long.TYPE, new Converter() {
            @Override
            public View convert(Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("Long");
                return editText;
            }
        });
        converterForClass.put(Integer.TYPE, new Converter() {
            @Override
            public View convert(Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("Integer");
                return editText;
            }
        });
        converterForClass.put(Double.TYPE, new Converter() {
            @Override
            public View convert(Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("Double");
                return editText;
            }
        });
        converterForClass.put(String.class, new Converter() {
            @Override
            public View convert(Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("String");
                return editText;
            }
        });
    };

    public static HashMap<Field, View> convert(Obstacle obstacle, Context ctx){


        Field[] fieldsOfObstacle = obstacle.getClass().getFields();
        HashMap<Field, View> mapping = new HashMap<>();

        for (Field f : fieldsOfObstacle ) {
            if(converterForClass.get(f.getType()) != null)
                mapping.put(f, converterForClass.get(f.getType()).convert(ctx));

        }

        return mapping;
    }



}
