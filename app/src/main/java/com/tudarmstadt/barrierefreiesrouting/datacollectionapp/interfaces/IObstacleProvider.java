package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

import bp.common.model.Obstacle;

/**
 * Implement this interface if you want to provide obstacles
 */
public interface IObstacleProvider {
    Obstacle getObstacle();
}
