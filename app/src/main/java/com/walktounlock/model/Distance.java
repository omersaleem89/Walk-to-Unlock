package com.walktounlock.model;

/**
 * Created by umer on 31-Mar-18.
 */

public class Distance {
    String id,date,totalDistance;
    public Distance(){}
    public Distance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Distance(String date, String totalDistance) {
        this.date = date;
        this.totalDistance = totalDistance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }
}
