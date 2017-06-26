package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacles;

/**
 * Created by Bi on 18.05.2017.
 * Copied by Felix on 02.06.2017
 */
public class Stairs extends Obstacle{
    private int numberOfStairs;
    private int heightOfStairs;
    private boolean handleAvailable;
    private int id;

    public Stairs(){

    }

    public Stairs(String name, double longitude, double latitude, int numberOfStairs, int heightOfStairs, boolean handleAvailable){
        super(name, ObstacleTypes.STAIRS, longitude, latitude);
        this.numberOfStairs = numberOfStairs;
        this.heightOfStairs = heightOfStairs;
        this.handleAvailable = handleAvailable;
        this.setId(0);
    }

    public int getNumberOfStairs() {
        return numberOfStairs;
    }

    public void setNumberOfStairs(int num) {
        this.numberOfStairs = num;
    }

    public int getHeightOfStairs() {
        return heightOfStairs;
    }

    public void setHeightOfStairs(int height) {
        this.heightOfStairs = height;
    }

    public boolean getHandleAvailable() {
        return handleAvailable;
    }

    public void setHandleAvailable(boolean bool) {
        this.handleAvailable = bool;
    }

    public void setId(int id){this.id = id;}

    public int getId(){return this.id;}

}