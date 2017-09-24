package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor;

import java.util.HashMap;
import java.util.Map;

import bp.common.model.obstacles.Obstacle;


/**
 * ObstacleViewModel ist used for binding the view and for making an abstraction between the view and
 * the specific model implementation.
 * <p>
 * The ObstacleViewModel stores model data of the Obstacle, that is used to display (model->view)
 * the Obstacle and to store the changes (view -> model).
 * <p>
 * The Attributes are loaded on runtime.
 */
public class ObstacleViewModel {

    public Obstacle internalObstacle;


    /**
     * The attributes mapped to the attributeName
     */
    public Map<String, ObstacleAttribute<?>> attributesMap = new HashMap<>();


    public ObstacleViewModel() {
    }

    public ObstacleViewModel(Map<String, ObstacleAttribute<?>> attributes) {
        attributesMap = attributes;
    }

}