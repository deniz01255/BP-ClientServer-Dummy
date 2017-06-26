package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.abstacleIMPL;

import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacles.Obstacle;
import com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacles.ObstacleTypes;

/**
 * Created by deniz on 24.06.17.
 */

public class Ramp extends Obstacle {

    private double degree;
    private int id;

    public Ramp(){

    }

    public Ramp(String name, double longitude, double latitude, double degree , boolean handleAvailable){
        super(name, ObstacleTypes.STAIRS, longitude, latitude);
        this.degree = degree;
        this.setId(1);
    }

    public void setDegree(double degree){this.degree = degree;}
    public double getDegree(){return this.degree;}

    public void setId(int id){this.id = id;}
    public int getId(){return this.id;}

}
