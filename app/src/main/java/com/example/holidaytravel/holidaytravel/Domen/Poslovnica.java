package com.example.holidaytravel.holidaytravel.Domen;

public class Poslovnica {
    //String id;
    String grad;
    String adresa;
    String latitude;
    String longitude;
    String telefon;
    String radnoVreme;

    public Poslovnica() {
    }

    public Poslovnica(String grad, String adresa, String latitude, String longitude, String radnoVreme, String telefon) {
        this.grad = grad;
        this.adresa = adresa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radnoVreme = radnoVreme;
        this.telefon = telefon;
    }

    /*public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }*/

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
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

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getRadnoVreme() {
        return radnoVreme;
    }

    public void setRadnoVreme(String radnoVreme) {
        this.radnoVreme = radnoVreme;
    }

    @Override
    public String toString() {
        return grad + ": " + adresa + "\n\t" + radnoVreme +  "\n\t" + telefon;
    }

    public String skraceniToString() {
        return grad + ": " + adresa + "\n\t" + radnoVreme;
    }
}
