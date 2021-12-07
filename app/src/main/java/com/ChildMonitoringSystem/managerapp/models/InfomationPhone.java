package com.ChildMonitoringSystem.managerapp.models;

import java.io.Serializable;

public class InfomationPhone implements Serializable {
    private String SERI_PHONE;
    private String PHONE_USER;
    private String PERMISSION_DENIED;
    private String MODEL;
    private String BRAND;
    private String DEVICE;
    private String PRODUCT;
    private String NAME_SPY;
    private String DATE_SPY;

    public InfomationPhone() {
    }

    public InfomationPhone(String SERI_PHONE, String PHONE_USER, String PERMISSION_DENIED, String MODEL, String BRAND, String DEVICE, String PRODUCT, String NAME_SPY, String DATE_SPY) {
        this.SERI_PHONE = SERI_PHONE;
        this.PHONE_USER = PHONE_USER;
        this.PERMISSION_DENIED = PERMISSION_DENIED;
        this.MODEL = MODEL;
        this.BRAND = BRAND;
        this.DEVICE = DEVICE;
        this.PRODUCT = PRODUCT;
        this.NAME_SPY = NAME_SPY;
        this.DATE_SPY = DATE_SPY;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getPHONE_USER() {
        return PHONE_USER;
    }

    public void setPHONE_USER(String PHONE_USER) {
        this.PHONE_USER = PHONE_USER;
    }

    public String getPERMISSION_DENIED() {
        return PERMISSION_DENIED;
    }

    public void setPERMISSION_DENIED(String PERMISSION_DENIED) {
        this.PERMISSION_DENIED = PERMISSION_DENIED;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public String getBRAND() {
        return BRAND;
    }

    public void setBRAND(String BRAND) {
        this.BRAND = BRAND;
    }

    public String getDEVICE() {
        return DEVICE;
    }

    public void setDEVICE(String DEVICE) {
        this.DEVICE = DEVICE;
    }

    public String getPRODUCT() {
        return PRODUCT;
    }

    public void setPRODUCT(String PRODUCT) {
        this.PRODUCT = PRODUCT;
    }

    public String getNAME_SPY() {
        return NAME_SPY;
    }

    public void setNAME_SPY(String NAME_SPY) {
        this.NAME_SPY = NAME_SPY;
    }

    public String getDATE_SPY() {
        return DATE_SPY;
    }

    public void setDATE_SPY(String DATE_SPY) {
        this.DATE_SPY = DATE_SPY;
    }
}
