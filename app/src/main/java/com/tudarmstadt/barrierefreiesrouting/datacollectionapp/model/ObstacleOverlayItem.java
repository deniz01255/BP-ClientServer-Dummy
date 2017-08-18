package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import bp.common.model.obstacles.Obstacle;

/**
 * Used as wrapper class to store an obstacle in addition to the overlayitem.
 *
 * This is required in order to get the obstacle that should be edited when the user
 * clicks on an obstacle overlay item on the map and initiates the edit process.
 */
public class ObstacleOverlayItem extends OverlayItem {


    private Obstacle obstacle;

    public ObstacleOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint, Obstacle obstacle) {
        super(aTitle, aSnippet, aGeoPoint);
        this.obstacle = obstacle;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }
}
