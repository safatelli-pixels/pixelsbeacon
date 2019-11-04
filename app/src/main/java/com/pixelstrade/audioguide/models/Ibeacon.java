package com.pixelstrade.audioguide.models;


import io.realm.RealmObject;

public class Ibeacon extends RealmObject{

    private  int id;
    private String name;
    private String major;
    private String minor;
    private String uuid;
    private boolean enable;
    private String proximity = "IMMEDIATE";
    private String distance ="0" ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getProximity() {
        return proximity;
    }

    public void setProximity(String proximity) {
        this.proximity = proximity;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
