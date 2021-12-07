package com.ChildMonitoringSystem.managerapp.models;

import retrofit2.http.Field;

public class CreateAccount {
    private String PHONE_NUMBERS,PASSWORD_USERS,NAME,DATE_OF_BIRTH,DATE_CREATE,EMAIL;

    public CreateAccount() {
    }

    public CreateAccount(String PHONE_NUMBERS, String PASSWORD_USERS, String NAME, String DATE_OF_BIRTH, String DATE_CREATE, String EMAIL) {
        this.PHONE_NUMBERS = PHONE_NUMBERS;
        this.PASSWORD_USERS = PASSWORD_USERS;
        this.NAME = NAME;
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
        this.DATE_CREATE = DATE_CREATE;
        this.EMAIL = EMAIL;
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

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getDATE_OF_BIRTH() {
        return DATE_OF_BIRTH;
    }

    public void setDATE_OF_BIRTH(String DATE_OF_BIRTH) {
        this.DATE_OF_BIRTH = DATE_OF_BIRTH;
    }

    public String getDATE_CREATE() {
        return DATE_CREATE;
    }

    public void setDATE_CREATE(String DATE_CREATE) {
        this.DATE_CREATE = DATE_CREATE;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
}