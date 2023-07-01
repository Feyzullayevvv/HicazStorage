package com.example.hicaz.model;

public class UserActions {
    private int Nr;
    private String userName;
    private int EmelliyatNr;
    private String Emeliyyat;

    public int getNr() {
        return Nr;
    }

    public void setNr(int nr) {
        Nr = nr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getEmelliyatNr() {
        return EmelliyatNr;
    }

    public void setEmelliyatNr(int emelliyatNr) {
        EmelliyatNr = emelliyatNr;
    }

    public String getEmeliyyat() {
        return Emeliyyat;
    }

    public void setEmeliyyat(String emeliyyat) {
        Emeliyyat = emeliyyat;
    }
}
