package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import java.util.HashMap;
import java.util.Map;

import bp.common.model.Obstacle;

/**
 * The ObstacleViewModel stores model data of the Obstacle, that is used to display (model->view)
 * the Obstacle and to store the changes (view -> model).
 *
 * The Attributes are loaded on runtime.
 */
public class ObstacleViewModel {

    /**
     * The attributes mapped to the attributeName
     */
    public Map<String, ObstacleAttribute<?>> attributesMap = new HashMap<>();


    public ObstacleViewModel(Map<String, ObstacleAttribute<?>> attributes) {
        attributesMap = attributes;
    }

}