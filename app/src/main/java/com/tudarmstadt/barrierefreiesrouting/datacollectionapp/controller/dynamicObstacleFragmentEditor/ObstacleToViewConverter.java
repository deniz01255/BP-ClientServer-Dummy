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

    /**
     * Used to access the corresponding converter for the given class.
     */
    private static Map<Class, Converter> converterForClass = new HashMap<>();

    static {
        converterForClass.put(Long.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, String name) {
                ObstacleAttribute<Long> oa = new ObstacleAttribute<Long>(Long.TYPE, name);
                oa.value = (Long) value;
                return oa;
            }
        });
        converterForClass.put(Integer.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, String name) {
                ObstacleAttribute<Integer> oa = new ObstacleAttribute<Integer>(Integer.TYPE, name);
                oa.value = (Integer) value;
                return oa;
            }
        });
        converterForClass.put(Double.TYPE, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, String name) {
                ObstacleAttribute<Double> oa = new ObstacleAttribute<Double>(Double.TYPE, name);
                oa.value = (Double) value;
                return oa;

            }
        });
        converterForClass.put(String.class, new Converter() {
            @Override
            public ObstacleAttribute<?> convert(Object value, String name) {
                ObstacleAttribute<String> oa = new ObstacleAttribute<String>(String.class, name);
                oa.value = (String) value;
                return oa;
            }
        });
    }

    /**
     * Converts all Attributes with the "EditableAttribute" Annotation from the given Obstacle
     * instance, to a Mapping between AttributeName (stored in the EditableAttribute Class as value)
     * and the ObstacleAttribute.
     *
     * @param obstacle the given obstacle to convert
     * @return a mapping of attribute names to ObstacleAttribute instances
     */
    public static Map<String, ObstacleAttribute<?>> convertObstacleToAttributeMap(Obstacle obstacle, Context ctx) {

        HashMap<String, ObstacleAttribute<?>> map = new HashMap<String, ObstacleAttribute<?>>();
        Class<?> current = obstacle.getClass();
        while (current.getSuperclass() != null) {
            Field[] fieldsOfObstacle = current.getDeclaredFields();

            for (Field f : fieldsOfObstacle) {
                if (converterForClass.get(f.getType()) != null)
                    try {
                        f.setAccessible(true);
                        if (f.getAnnotation(EditableAttribute.class) != null){
                            String attributeName= f.getAnnotation(EditableAttribute.class).value();
                            ObstacleAttribute<?>  attribute = converterForClass.get(f.getType()).convert(f.get(obstacle), attributeName);

                            map.put(attributeName, attribute);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
            current = current.getSuperclass();
        }

        return map;
    }

    interface Converter {
        ObstacleAttribute<?> convert(Object value, String name);
    }

}
