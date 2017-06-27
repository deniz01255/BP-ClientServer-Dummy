package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller;

import android.app.Activity;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.fragments.MapEditorFragment;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.network.BpServerHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import bp.common.model.IObstacle;
import bp.common.model.Obstacle;
import bp.common.model.Stairs;

/**
 * Created by Vincent on 27.06.2017.
 */

public class ObstacleViewModel {

    private Map<Field, ObstacleAttribute<?>> attributes = new HashMap<>();

    public ObstacleViewModel(Map<Field, ObstacleAttribute<?>> attributes){
        this.attributes = attributes;
    }


    public void commit(Activity activity, MapEditorFragment mapEditorFragment){

        BpServerHandler.PostNewObstacle(activity, mapEditorFragment, finalizeObstacle());

    }

    private IObstacle finalizeObstacle() {
        Stairs stairs = new Stairs();

        stairs.setLatitude(49);
        stairs.setLongitude(8);

        return new Stairs();
    }
}