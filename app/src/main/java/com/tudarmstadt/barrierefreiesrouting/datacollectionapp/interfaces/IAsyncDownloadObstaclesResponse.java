package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.interfaces;

import java.util.List;

import bp.common.model.Obstacle;

/**
 * Created by Vincent on 27.06.2017.
 */

public interface IAsyncDownloadObstaclesResponse {

    void processDownloadedObstacles(List<Obstacle> obstacles);

}
