package com.example.holidaytravel.holidaytravel.Domen;

import java.util.ArrayList;
import java.util.List;

public class Klijent {
    String korisnickoIme="";
    String lozinka;
    String ime;
    String prezime;
    String adresa;
    String brTelefona;
    String email;
    //List<Ponuda> listaPonuda;

    public Klijent() {
        //listaPonuda = new ArrayList<>();
    }

    public Klijent(String lozinka, String ime, String prezime, String adresa, String brTelefona, String email) {
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.brTelefona = brTelefona;
        this.email = email;
        //listaPonuda = new ArrayList<Ponuda>();
    }

    public Klijent(String ime, String prezime, String adresa, String brTelefona, String email) {
        this.ime = ime;
        this.prezime = prezime;
        this.adresa = adresa;
        this.brTelefona = brTelefona;
        this.email = email;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBrTelefona() {
        return brTelefona;
    }

    public void setBrTelefona(String brTelefona) {
        this.brTelefona = brTelefona;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public List<Ponuda> getListaPonuda() {
        return listaPonuda;
    }

    public void setListaPonuda(List<Ponuda> listaPonuda) {
        this.listaPonuda = listaPonuda;
    }*/


    public String podaciZaRez(){
        return ime + " " + prezime + "\n" + adresa + "\n" + brTelefona + "\n" + email;
    }

    @Override
    public String toString() {
        return "Klijent: " + ime + " " + prezime + "\n" + adresa + "\n" + brTelefona + "\n" + email;
    }
}
