package com.ChildMonitoringSystem.managerapp.models;

import retrofit2.http.Field;

public class HistorySignin {
    private String PHONE_USERS,DATE_LOGIN,INFO;

    public HistorySignin(String PHONE_USERS, String DATE_LOGIN, String INFO) {
        this.PHONE_USERS = PHONE_USERS;
        this.DATE_LOGIN = DATE_LOGIN;
        this.INFO = INFO;
    }

    public String getPHONE_USERS() {
        return PHONE_USERS;
    }

    public void setPHONE_USERS(String PHONE_USERS) {
        this.PHONE_USERS = PHONE_USERS;
    }

    public String getDATE_LOGIN() {
        return DATE_LOGIN;
    }

    public void setDATE_LOGIN(String DATE_LOGIN) {
        this.DATE_LOGIN = DATE_LOGIN;
    }

    public String getINFO() {
        return INFO;
    }

    public void setINFO(String INFO) {
        this.INFO = INFO;
    }
}
