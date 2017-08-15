package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;

import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Stairs;


/**
 * Created by vincent on 8/13/17.
 */

public class ObstacleDataSingleton {

    private static volatile ObstacleDataSingleton instance = null;

    private Obstacle mObstacle;

    private ObstacleViewModel mObstacleViewModel;
    public boolean editorIsSyncedWithSelection = false;

    private ObstacleDataSingleton() {}

    public static ObstacleDataSingleton getInstance() {
        if (instance == null) {
            synchronized(ObstacleDataSingleton.class) {
                if (instance == null) {
                    instance = new ObstacleDataSingleton();
                    instance.setmObstacle(new Stairs());

                }
            }
        }
        return instance;
    }


    public Obstacle getmObstacle() {
        return mObstacle;
    }

    public void setmObstacle(Obstacle mObstacle) {
        this.mObstacle = mObstacle;
    }

    public ObstacleViewModel getmObstacleViewModel() {
        return mObstacleViewModel;
    }

    public void setmObstacleViewModel(ObstacleViewModel mObstacleViewModel) {
        this.mObstacleViewModel = mObstacleViewModel;
    }
}
