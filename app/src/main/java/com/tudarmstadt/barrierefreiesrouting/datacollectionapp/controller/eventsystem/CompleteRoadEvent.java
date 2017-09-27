package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.eventsystem;

import bp.common.model.ways.Way;

/**
 * Created by deniz on 11.09.17.
 */

public class CompleteRoadEvent {

    private Way way;

    public CompleteRoadEvent(Way way) {

        this.way = way;
    }

    public Way getObstacle() {
        return way;
    }
}
