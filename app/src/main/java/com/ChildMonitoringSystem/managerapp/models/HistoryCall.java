package com.ChildMonitoringSystem.managerapp.models;


public class HistoryCall {
    private int PHONE_ID;
    private String PHONE_NUMBER;
    private String CONTACT_NAME;
    private String CALL_TYPE;
    private String CALL_DATE;
    private String CALL_DURATION;
    private String SERI_PHONE;

    @Override
    public String toString() {
        return "HistoryCall{" +
                "PHONE_ID=" + PHONE_ID +
                ", PHONE_NUMBER='" + PHONE_NUMBER + '\'' +
                ", CONTACT_NAME='" + CONTACT_NAME + '\'' +
                ", CALL_TYPE='" + CALL_TYPE + '\'' +
                ", CALL_DATE=" + CALL_DATE +
                ", CALL_DURATION='" + CALL_DURATION + '\'' +
                ", SERI_PHONE='" + SERI_PHONE + '\'' +
                '}';
    }

    public int getPHONE_ID() {
        return PHONE_ID;
    }

    public void setPHONE_ID(int PHONE_ID) {
        this.PHONE_ID = PHONE_ID;
    }

    public String getPHONE_NUMBER() {
        return PHONE_NUMBER;
    }

    public void setPHONE_NUMBER(String PHONE_NUMBER) {
        this.PHONE_NUMBER = PHONE_NUMBER;
    }

    public String getCONTACT_NAME() {
        return CONTACT_NAME;
    }

    public void setCONTACT_NAME(String CONTACT_NAME) {
        this.CONTACT_NAME = CONTACT_NAME;
    }

    public String getCALL_TYPE() {
        return CALL_TYPE;
    }

    public void setCALL_TYPE(String CALL_TYPE) {
        this.CALL_TYPE = CALL_TYPE;
    }

    public String getCALL_DATE() {
        return CALL_DATE;
    }

    public void setCALL_DATE(String CALL_DATE) {
        this.CALL_DATE = CALL_DATE;
    }

    public String getCALL_DURATION() {
        return CALL_DURATION;
    }

    public void setCALL_DURATION(String CALL_DURATION) {
        this.CALL_DURATION = CALL_DURATION;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }
}
