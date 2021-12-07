package com.ChildMonitoringSystem.managerapp.models;

import java.io.Serializable;

public class Inbox implements Serializable {

    private String PHONE_NUMBERS;
    private String NAME;
    private String TYPE;
    private String CONTENT;
    private String DATE_MESSAGE;
    private String SERI_PHONE;

    public Inbox(String PHONE_NUMBERS, String NAME, String TYPE, String CONTENT, String DATE_MESSAGE, String SERI_PHONE) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
        this.NAME = NAME;
        this.TYPE = TYPE;
        this.CONTENT = CONTENT;
        this.DATE_MESSAGE = DATE_MESSAGE;
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getPHONE_NUMBERS() {
        return PHONE_NUMBERS;
    }

    public void setPHONE_NUMBERS(String PHONE_NUMBERS) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }

    public String getDATE_MESSAGE() {
        return DATE_MESSAGE;
    }

    public void setDATE_MESSAGE(String DATE_MESSAGE) {
        this.DATE_MESSAGE = DATE_MESSAGE;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }
}
