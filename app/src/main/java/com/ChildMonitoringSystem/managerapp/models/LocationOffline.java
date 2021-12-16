package com.ChildMonitoringSystem.managerapp.models;

public class LocationOffline {
    private String DATE_LOG;
    private String LONGTITUDE;
    private String LATITUDE;
    private String SERI_PHONE;
    public LocationOffline() {
    }

    public LocationOffline(String DATE_LOG, String LATITUDE, String LONGTITUDE, String SERI_PHONE) {
        this.DATE_LOG = DATE_LOG;
        this.LATITUDE = LATITUDE;
        this.LONGTITUDE = LONGTITUDE;
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getDATE_LOG() {
        return DATE_LOG;
    }

    public void setDATE_LOG(String DATE_LOG) {
        this.DATE_LOG = DATE_LOG;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGTITUDE() {
        return LONGTITUDE;
    }

    public void setLONGTITUDE(String LONGTITUDE) {
        this.LONGTITUDE = LONGTITUDE;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }
}
