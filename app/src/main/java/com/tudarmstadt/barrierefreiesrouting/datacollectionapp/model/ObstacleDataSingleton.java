package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.model;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.controller.dynamicObstacleFragmentEditor.ObstacleViewModel;

import org.osmdroid.util.GeoPoint;

import bp.common.model.obstacles.Obstacle;
import bp.common.model.obstacles.Stairs;


/**
 * This Singleton holds the current State of the Obstacle that is or will be edited.
 */
public class ObstacleDataSingleton {

    private static volatile ObstacleDataSingleton instance = null;

    private Obstacle mObstacle;

    private ObstacleViewModel mObstacleViewModel;
    public boolean editorIsSyncedWithSelection = false;

    public GeoPoint currentPositionOfSetObstacle = null;
    public boolean obstacleDataCollectionCompleted = false;
    private Obstacle existingSelectedObstacle;


    private ObstacleDataSingleton() {
    }

    public static ObstacleDataSingleton getInstance() {
        if (instance == null) {
            synchronized (ObstacleDataSingleton.class) {
                if (instance == null) {
                    instance = new ObstacleDataSingleton();
                    instance.setObstacle(new Stairs());
                }
            }
        }
        return instance;
    }

    public Obstacle getObstacle() {
        return mObstacle;
    }

    public void setObstacle(Obstacle mObstacle) {
        this.mObstacle = mObstacle;
    }

    public ObstacleViewModel getmObstacleViewModel() {
        return mObstacleViewModel;
    }

    public void setObstacleViewModel(ObstacleViewModel mObstacleViewModel) {
        this.mObstacleViewModel = mObstacleViewModel;
    }

}
