package com.example.holidaytravel.holidaytravel.Domen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Ponuda {
    String id;
    String mesto;
    String cena;
    String opis;
    String lastMinute;
    String slika;
    String datumOd;
    String datumDo;
    String vrsta;
    Hotel hotel;
    String saznajVise;

    public Ponuda() {
    }

    public Ponuda(String mesto, String cena, String opis, String lastMinute, String slika, String datumOd, String datumDo, String vrsta, Hotel hotel, String saznajVise) {
        this.mesto = mesto;
        this.cena = cena;
        this.opis = opis;
        this.lastMinute = lastMinute;
        this.slika = slika;
        this.datumOd = datumOd;
        this.datumDo = datumDo;
        this.vrsta = vrsta;
        this.hotel = hotel;
        this.saznajVise = saznajVise;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getLastMinute() {
        return lastMinute;
    }

    public void setLastMinute(String lastMinute) {
        this.lastMinute = lastMinute;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDatumOd() {
        return datumOd;
    }

    public void setDatumOd(String datumOd) {
        this.datumOd = datumOd;
    }

    public String getDatumDo() {
        return datumDo;
    }

    public void setDatumDo(String datumDo) {
        this.datumDo = datumDo;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getSaznajVise() {
        return saznajVise;
    }

    public void setSaznajVise(String saznajVise) {
        this.saznajVise = saznajVise;
    }

    public String samoDatum(String ceo){
        String skraceni ="";

        if(ceo.length()==18) {
            skraceni = ceo.substring(0, 9);
        }else {
            skraceni = ceo.substring(0,10);
        }

        /*SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        try {
            Date date = sdf.parse(skraceni);
        } catch (Exception e){

        }*/

        return skraceni;
    }

    public String datumi(){
        String vraca="";
        vraca = samoDatum(datumOd) + " do " + samoDatum(datumDo);
        return vraca;
    }

    public String lastMinutePrikaz(){
        return mesto + "\n" + cena + "€";
    }

    public String josKraciPrikaz(){
        String vraca="";
        vraca = "Mesto: " + mesto + "\n" + "Datum: " + datumi() + "\nHotel: " + hotel.toString();
        return vraca;
    }

    public String kraciPrikaz(){
        String vraca="";
        vraca = "Mesto: " + mesto + "\nCena: " + cena + "€\nDatum: " + datumi() + "\nHotel: " + hotel.toString();
        return vraca;
    }

    @Override
    public String toString() {
        return "Mesto=" + mesto + "\nCena=" + cena + "€\nOpis=" + opis + "\nLastMinute=" + lastMinute +"\nDatum: " + datumi();
    }

    public String zaPdf(){
        String vraca="";
        vraca ="\t\tVrsta putovanja: " + vrsta + "\n\t\tCena:" + cena + "€\n\t\tDatum: " + datumi() + "\n\t\tOpis:" + opis + "\n\t\tHotel: " + hotel.toString();
        return vraca;
    }
}
