package com.example.holidaytravel.holidaytravel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "DB.db";
    private static final int DB_VERSION = 1;

    SQLiteDatabase db;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlKlijenti = "CREATE TABLE klijenti(id INTEGER PRIMARY KEY AUTOINCREMENT, ime VARCHAR, prezime VARCHAR, adresa VARCHAR, brTelefona VARCHAR, brPasosa VARCHAR);";
        String sqlPonude = "CREATE TABLE ponude(id INTEGER PRIMARY KEY AUTOINCREMENT, mesto VARCHAR, cena DOUBLE, opis TEXT, lastMinute VARCHAR, slika VARCHAR);";
        //String sqlRezervacije = "CREATE TABLE rezervacije(id INTEGER PRIMARY KEY AUTOINCREMENT, klijentID INTEGER REFERENCES klijenti(id) ON UPDATE CASCADE ON DELETE NO ACTION, ponudaID INTEGER REFERENCES ponude(id) ON UPDATE CASCADE ON DELETE NO ACTION;";

        String sqlRezervacije = "CREATE TABLE rezervacije(id INTEGER PRIMARY KEY AUTOINCREMENT, klijentID INTEGER, ponudaID INTEGER, FOREIGN KEY (klijentID) REFERENCES klijenti(id), FOREIGN KEY (ponudaID) REFERENCES ponude(id));";

        db.execSQL(sqlKlijenti);
        db.execSQL(sqlPonude);
        db.execSQL(sqlRezervacije);

       /*ubaciKlijente();
        ubaciPonude();
        ubaciRezervacije();*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlKlijenti = "DROP TABLE IF EXISTS klijenti";
        String sqlRezervacije = "DROP TABLE IF EXISTS rezervacije";

        db.execSQL(sqlKlijenti);
        db.execSQL(sqlRezervacije);

        onCreate(db);
    }

    public static boolean doesDatabaseExist(Context context, String dbName){
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
/*
    public boolean dodajKlijenta(Klijent k){
        SQLiteDatabase dbb = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ime", k.getIme());
        contentValues.put("prezime",k.getPrezime());
        contentValues.put("adresa",k.getAdresa());
        contentValues.put("brTelefona",k.getBrTelefona());
        contentValues.put("brPasosa",k.getBrPasosa());
        dbb.insert("klijenti",null,contentValues);
        dbb.close();
        return true;
    }

    public boolean dodajPonudu(Ponuda p) {
        SQLiteDatabase dbb = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mesto", p.getMesto());
        contentValues.put("cena", p.getCena());
        contentValues.put("opis", p.getOpis());
        contentValues.put("lastMinute", p.getLastMinute());
        /*try {
            FileInputStream fs = new FileInputStream(putanja);
            byte[] imgbyte = new byte[fs.available()];
            fs.read(imgbyte);
            contentValues.put("slika", imgbyte);
            dbb.insert("ponude", null, contentValues);
            fs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentValues.put("slika", p.getSlika());
        dbb.insert("ponude", null, contentValues);
        dbb.close();
        return true;
    }

    public boolean dodajRezervaciju(Rezervacija r){
        SQLiteDatabase dbb = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("klijentID",r.getKlijentID());
        contentValues.put("ponudaID",r.getPonudaID());
        dbb.insert("rezervacije",null,contentValues);
        dbb.close();
        return true;
    }*/

    public Cursor vratiSveKlijente(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM klijenti;";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor vratiKlijenta(String brPas){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM klijenti WHERE brPasosa = " + brPas + ";";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public int vratiIDKlijenta(String brPas){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT id FROM klijenti WHERE brPasosa =" + brPas + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        int id = 0;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        return id;
    }

    public Cursor vratiSvePonude(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM ponude;";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor vratiSveLastMinutePonude(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM ponude WHERE lastMinute = 'da';";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public int vratiIDPonude(String mesto){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT id FROM ponude WHERE mesto =" + mesto + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        int id = 0;
        if(cursor.moveToFirst()){
            id = cursor.getInt(0);
        }
        return id;
    }

    public String vratiMestoPonude(int id){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT mesto FROM ponude WHERE id =" + id + ";";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        String mesto = "";
        if(cursor.moveToFirst()){
            mesto = cursor.getString(0);
        }
        return mesto;
    }

    public Cursor vratiRezervisanePonude(String brPas){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM ponude p JOIN rezervacije r ON r.ponudaID = p.id JOIN klijenti k ON k.id = r.klijentID AND k.brPasosa = " + brPas +";";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor vratiSveRezervacije(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM rezervacije;";
        return sqLiteDatabase.rawQuery(sql,null);
    }

    public Cursor vratiRezervacije(String brPas){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        int id = vratiIDKlijenta(brPas);
        String sql = "SELECT * FROM rezervacije WHERE klijentID =" + id +";";
        return sqLiteDatabase.rawQuery(sql,null);
    }

}
