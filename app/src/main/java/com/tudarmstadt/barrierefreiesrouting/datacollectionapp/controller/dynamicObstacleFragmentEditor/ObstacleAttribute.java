package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleAttribute<T> implements Observer {

    public String viewId;

    public final Class<T> typeParameterClass;

    public T value;

    public String name;

    public ObstacleAttribute(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void update(Observable o, Object arg) {
        value = (T) arg;
    }
}
