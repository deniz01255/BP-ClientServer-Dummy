package com.tudarmstadt.barrierefreiesrouting.datacollectionapp.obstacles;

/**
 * Created by Bi on 18.05.2017.
 * Copied by Felix on 02.06.2017
 */
public class Obstacle {

    private String typename;
    private ObstacleTypes typecode;

    private double longitude = 49.874978;
    private double latitude = 8.655971;

    public Obstacle(){

    }

    public Obstacle(String name, ObstacleTypes typecode, double longitude, double latitude){
        this.typename = name;
        this.typecode = typecode;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getName() {
        return typename;
    }

    public void setName(String name) {
        this.typename = name;
    }

    public ObstacleTypes getTypecode() {
        return typecode;
    }

    public void setTypecode(ObstacleTypes code) {
        this.typecode = code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
