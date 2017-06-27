package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.content.Context;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleToViewConverter {

    interface Converter {
        View convert(IObstacle obstacle, Context ctx);
    }
    private static Map<Class,Converter> converterForClass = new HashMap<>();

    static {
        converterForClass.put(Long.TYPE, new Converter() {
            @Override
            public View convert(IObstacle obstacle, Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("Long");
                return editText;
            }
        });
        converterForClass.put(Integer.TYPE, new Converter() {
            @Override
            public View convert(IObstacle obstacle, Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("Integer");
                return editText;
            }
        });
        converterForClass.put(Double.TYPE, new Converter() {
            @Override
            public View convert(IObstacle obstacle, Context ctx) {

                TextView label = new TextView(ctx);
                label.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                label.setGravity(Gravity.LEFT);
                label.setText("Double Input");

                EditText editText = new EditText(ctx);
                editText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setGravity(Gravity.RIGHT);
                editText.setText("......");

                LinearLayout rowLayout = new LinearLayout(ctx);

                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.addView(label);
                rowLayout.addView(editText);

                return rowLayout;
            }
        });
        converterForClass.put(String.class, new Converter() {
            @Override
            public View convert(IObstacle obstacle, Context ctx) {

                EditText editText = new EditText(ctx);
                editText.setText("String");
                return editText;
            }
        });
    };

    public static HashMap<Field, View> convert(IObstacle obstacle, Context ctx){


        Field[] fieldsOfObstacle = obstacle.getClass().getDeclaredFields();
        HashMap<Field, View> mapping = new HashMap<>();

        for (Field f : fieldsOfObstacle ) {
            if(converterForClass.get(f.getType()) != null)
                mapping.put(f, converterForClass.get(f.getType()).convert(obstacle, ctx));

        }

        return mapping;
    }



}
