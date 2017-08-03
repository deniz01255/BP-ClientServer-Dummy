package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import android.content.Context;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import bp.common.model.Obstacle;
import bp.common.model.annotations.EditableAttribute;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleToViewConverter {

    private static Map<Class, Converter> converterForClass = new HashMap<>();

    static {
        converterForClass.put(Long.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {

                ObstacleAttribute<Long> oa = new ObstacleAttribute<Long>(Long.TYPE);
                oa.value = (Long) value;
                return oa;
            }
        });
        converterForClass.put(Integer.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {
                ObstacleAttribute<Integer> oa = new ObstacleAttribute<Integer>(Integer.TYPE);
                oa.value = (Integer) value;
                return oa;
            }
        });
        converterForClass.put(Double.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {
                ObstacleAttribute<Double> oa = new ObstacleAttribute<Double>(Double.TYPE);
                oa.value = (Double) value;
                return oa;

            }
        });
        converterForClass.put(String.class, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, Context ctx) {
                ObstacleAttribute<String> oa = new ObstacleAttribute<String>(String.class);
                oa.value = (String) value;
                return oa;
            }
        });
    }

    public static Map<String, ObstacleAttribute<?>> convertObstacleToAttributeMap(Obstacle obstacle, Context ctx) {

        HashMap<String, ObstacleAttribute<?>> map = new HashMap<String, ObstacleAttribute<?>>();

        Class<?> current = obstacle.getClass();
        while (current.getSuperclass() != null) {
            Field[] fieldsOfObstacle = current.getDeclaredFields();

            for (Field f : fieldsOfObstacle) {
                if (converterForClass.get(f.getType()) != null)
                    try {
                        f.setAccessible(true);
                        if (f.getAnnotation(EditableAttribute.class) != null)
                            map.put(f.getAnnotation(EditableAttribute.class).value(), converterForClass.get(f.getType()).convert(f.get(obstacle), ctx));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            current = current.getSuperclass();
        }

        return map;
    }

    ;

    interface Converter {
        ObstacleAttribute<?> convert(Object value, Context ctx);
    }

}
