package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bp.common.model.Obstacle;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleToAttributeViewConverter {


    public static List<View> convert(Obstacle obs, Context context) {

        Field[] fields = obs.getClass().getFields();
        List<View> views = new LinkedList<>();

        for (Field f : fields) {
            Class<?> ftype = f.getType();

            views.add(converterForClass.get(ftype).convert(null, context));
        }

        return views;
    }

    interface Converter {
        View convert(Object o, Context ctx);
    }

    private static Map<Class, Converter> converterForClass = new HashMap<>();

    static {
        converterForClass.put(Long.TYPE, new Converter() {
            @Override
            public View convert(Object o, Context ctx) {
                return new EditText(ctx);
            }
        });
        converterForClass.put(String.class, new Converter() {
            @Override
            public View convert(Object o, Context ctx) {
                return new EditText(ctx);
            }
        });
        converterForClass.put(Integer.TYPE, new Converter() {
            @Override
            public View convert(Object o, Context ctx) {
                return new EditText(ctx);
            }
        });
    }


}


