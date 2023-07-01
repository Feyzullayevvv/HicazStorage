package com.example.hicaz.model;

public class MedaxilFaktura {
    private int Nr;
    private int MalNr;
    private String Mal;
    private String Vahid;
    private double Ceki;
    private double Mebleg;
    private int MedaxilNr;
    private String tarix;



    public String getTarix() {
        return tarix;
    }

    public void setTarix(String tarix) {
        this.tarix = tarix;
    }

    public int getNr() {
        return Nr;
    }

    public void setNr(int nr) {
        Nr = nr;
    }

    public int getMalNr() {
        return MalNr;
    }

    public void setMalNr(int malNr) {
        MalNr = malNr;
    }

    public String getMal() {
        return Mal;
    }

    public void setMal(String mal) {
        Mal = mal;
    }

    public String getVahid() {
        return Vahid;
    }

    public void setVahid(String vahid) {
        Vahid = vahid;
    }

    public double getCeki() {
        return Ceki;
    }

    public void setCeki(double ceki) {
        Ceki = ceki;
    }

    public double getMebleg() {
        return Mebleg;
    }

    public void setMebleg(double mebleg) {
        Mebleg = mebleg;
    }

    public int getMedaxilNr() {
        return MedaxilNr;
    }

    public void setMedaxilNr(int medaxilNr) {
        MedaxilNr = medaxilNr;
    }
}
