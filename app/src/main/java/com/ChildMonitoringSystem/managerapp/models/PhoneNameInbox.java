package com.ChildMonitoringSystem.managerapp.models;

public class PhoneNameInbox {
    private String PHONE_NUMBERS;
    private String NAME;

    public PhoneNameInbox(String PHONE_NUMBERS, String NAME) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
        this.NAME = NAME;
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
}
