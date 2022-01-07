package com.ChildMonitoringSystem.managerapp.models;

public class LocationRealTime {
    private String dateLog;
    private String latitude;
    private String longtitude;
    private String seriphone;

    public LocationRealTime(String dateLog, String latitude, String longtitude, String seriphone) {
        this.dateLog = dateLog;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.seriphone = seriphone;
    }

    public LocationRealTime() {
    }

    public String getDateLog() {
        return dateLog;
    }

    public void setDateLog(String dateLog) {
        this.dateLog = dateLog;
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
        return seriphone;
    }

    public void setSeri(String seri) {
        this.seriphone = seri;
    }
}
