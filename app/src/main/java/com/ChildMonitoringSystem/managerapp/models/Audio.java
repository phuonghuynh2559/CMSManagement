package com.ChildMonitoringSystem.managerapp.models;

public class Audio {
    private String IMAGES_NAME;
    private String SERI_PHONE;
    private String DATE_IMAGE;

    public Audio(String IMAGES_NAME, String SERI_PHONE, String DATE_IMAGE) {
        this.IMAGES_NAME = IMAGES_NAME;
        this.SERI_PHONE = SERI_PHONE;
        this.DATE_IMAGE = DATE_IMAGE;
    }

    public String getIMAGES_NAME() {
        return IMAGES_NAME;
    }

    public void setIMAGES_NAME(String IMAGES_NAME) {
        this.IMAGES_NAME = IMAGES_NAME;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getDATE_IMAGE() {
        return DATE_IMAGE;
    }

    public void setDATE_IMAGE(String DATE_IMAGE) {
        this.DATE_IMAGE = DATE_IMAGE;
    }
}
