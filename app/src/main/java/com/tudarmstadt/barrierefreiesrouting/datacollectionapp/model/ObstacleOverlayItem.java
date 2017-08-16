package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import bp.common.model.obstacles.Obstacle;

/**
 * Created by vincent on 8/16/17.
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
