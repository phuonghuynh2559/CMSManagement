package com.ChildMonitoringSystem.managerapp.models;

public class Contact {
    private String PHONE_NUMBERS;
    private String NAME;
    private String SERI_PHONE;

    @Override
    public String toString() {
        return "Contact{" +
                "PHONE_NUMBERS='" + PHONE_NUMBERS + '\'' +
                ", NAME='" + NAME + '\'' +
                ", SERI_PHONE='" + SERI_PHONE + '\'' +
                '}';
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

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }
}
