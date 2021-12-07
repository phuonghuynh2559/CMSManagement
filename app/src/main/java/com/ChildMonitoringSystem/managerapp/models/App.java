package com.ChildMonitoringSystem.managerapp.models;

public class App {
    private String SERI_PHONE;
    private String ICON;
    private String APPNAME;
    private String pNAME;
    private String verNAME;
    private String verCODE;

    public App(String SERI_PHONE, String ICON, String APPNAME, String pNAME, String verNAME, String verCODE) {
        this.SERI_PHONE = SERI_PHONE;
        this.ICON = ICON;
        this.APPNAME = APPNAME;
        this.pNAME = pNAME;
        this.verNAME = verNAME;
        this.verCODE = verCODE;
    }

    public String getSERI_PHONE() {
        return SERI_PHONE;
    }

    public void setSERI_PHONE(String SERI_PHONE) {
        this.SERI_PHONE = SERI_PHONE;
    }

    public String getICON() {
        return ICON;
    }

    public void setICON(String ICON) {
        this.ICON = ICON;
    }

    public String getAPPNAME() {
        return APPNAME;
    }

    public void setAPPNAME(String APPNAME) {
        this.APPNAME = APPNAME;
    }

    public String getpNAME() {
        return pNAME;
    }

    public void setpNAME(String pNAME) {
        this.pNAME = pNAME;
    }

    public String getVerNAME() {
        return verNAME;
    }

    public void setVerNAME(String verNAME) {
        this.verNAME = verNAME;
    }

    public String getVerCODE() {
        return verCODE;
    }

    public void setVerCODE(String verCODE) {
        this.verCODE = verCODE;
    }
}
