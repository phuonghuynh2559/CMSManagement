package com.ChildMonitoringSystem.managerapp.models;

public class UserRequest {
    private String PHONE_NUMBERS;
    private String PASSWORD_USERS;

    public UserRequest(String PHONE_NUMBERS, String PASSWORD_USERS) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
        this.PASSWORD_USERS = PASSWORD_USERS;
    }

    public UserRequest() {
    }

    public String getPHONE_NUMBERS() {
        return PHONE_NUMBERS;
    }

    public void setPHONE_NUMBERS(String PHONE_NUMBERS) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
    }

    public String getPASSWORD_USERS() {
        return PASSWORD_USERS;
    }

    public void setPASSWORD_USERS(String PASSWORD_USERS) {
        this.PASSWORD_USERS = PASSWORD_USERS;
    }
}
