package com.example.holidaytravel.holidaytravel.Domen;

public class Hotel {
    String naziv;
    String adresa;
    String latitude;
    String longitude;
    String zvezdice;

    public Hotel() {
    }

    public Hotel(String naziv, String adresa, String latitude, String longitude) {
        this.naziv = naziv;
        this.adresa = adresa;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZvezdice() {
        return zvezdice;
    }

    public void setZvezdice(String zvezdice) {
        this.zvezdice = zvezdice;
    }

    @Override
    public String toString() {
        return naziv + " " + zvezdice;
    }
}
