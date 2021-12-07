package com.ChildMonitoringSystem.managerapp.models;

import java.io.Serializable;

public class Video implements Serializable {
    private String MEDIA_NAME;
    private String SERI_PHONE;
    private String DATE_MEDIA;
    private String SIZE;
    private String DURATION;

    public Video(String MEDIA_NAME, String SERI_PHONE, String DATE_MEDIA, String SIZE, String DURATION) {
        this.MEDIA_NAME = MEDIA_NAME;
        this.SERI_PHONE = SERI_PHONE;
        this.DATE_MEDIA = DATE_MEDIA;
        this.SIZE = SIZE;
        this.DURATION = DURATION;
    }

    public String getMEDIA_NAME() {
        return MEDIA_NAME;
    }

    public void setMEDIA_NAME(String MEDIA_NAME) {
        this.MEDIA_NAME = MEDIA_NAME;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getDATE_MEDIA() {
        return DATE_MEDIA;
    }

    public void setDATE_MEDIA(String DATE_MEDIA) {
        this.DATE_MEDIA = DATE_MEDIA;
    }

    public String getSIZE() {
        return SIZE;
    }

    public void setSIZE(String SIZE) {
        this.SIZE = SIZE;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }
}
