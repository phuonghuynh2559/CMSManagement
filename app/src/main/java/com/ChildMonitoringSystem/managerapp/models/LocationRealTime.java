package com.ChildMonitoringSystem.managerapp.models;

public class LocationRealTime {
    private String latitude;
    private String longtitude;
    private String seri;

    public LocationRealTime(String latitude, String longtitude, String seri) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.seri = seri;
    }

    public LocationRealTime() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }
}
