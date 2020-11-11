package com.example.holidaytravel.holidaytravel.Domen;

public class Rezervacija {
    String id;
    //String klijentID;
    //String ponudaID;
    Klijent klijent;
    Ponuda ponuda;

    public Rezervacija() {
    }

    /*public Rezervacija(String klijentID, String ponudaID) {
        this.klijentID = klijentID;
        this.ponudaID = ponudaID;
    }*/

   public Rezervacija(Klijent klijent, Ponuda ponuda) {
        this.klijent = klijent;
        this.ponuda = ponuda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Klijent getKlijent() {
        return klijent;
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
    }

    public Ponuda getPonuda() {
        return ponuda;
    }

    public void setPonuda(Ponuda ponuda) {
        this.ponuda = ponuda;
    }
   /* public String getKlijentID() {
        return klijentID;
    }

    public void setKlijentID(String klijentID) {
        this.klijentID = klijentID;
    }

    public String getPonudaID() {
        return ponudaID;
    }

    public void setPonudaID(String ponudaID) {
        this.ponudaID = ponudaID;
    }*/

    @Override
    public String toString() {
        return "Klijent: " + klijent.ime + " " + klijent.prezime + "\n\n" + ponuda;
    }
}
