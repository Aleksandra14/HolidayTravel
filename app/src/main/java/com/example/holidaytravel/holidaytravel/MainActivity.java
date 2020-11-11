package com.example.holidaytravel.holidaytravel;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Biblioteka.Metode;
import com.example.holidaytravel.holidaytravel.Domen.Klijent;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView txtUl;
    SwitchCompat drawerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Povezivanje textview-a za ulogovanog korisnika
        View headerView = navigationView.getHeaderView(0);
        txtUl = (TextView) headerView.findViewById(R.id.txtUlogovani);
        if(PrijavaFragment.ulogovani != null){
            txtUl.setText(PrijavaFragment.ulogovani.getKorisnickoIme());
        } else {
            txtUl.setText("Niste prijavljeni");
        }

        //Povezivanje switch compat-a
        drawerSwitch = (SwitchCompat) navigationView.getMenu().findItem(R.id.night_mode).getActionView();
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            drawerSwitch.setChecked(true);
        }
        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (!drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (!drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            }
        });

        /*txtUl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrijavaFragment.ulogovani!=null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new RezervacijaFragment(PrijavaFragment.ulogovani.getKorisnickoIme())).commit();
                } else {
                    Toast.makeText(getApplicationContext(),"Niste prijavljeni",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
    }

    public void onClickAction(View v){
        Klijent k = PrijavaFragment.ulogovani;
        if(k != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new KlijentFragment(k.getKorisnickoIme())).addToBackStack(null).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Toast.makeText(getApplicationContext(),"Niste prijavljeni",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_pocetna) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new FirstFragment()).addToBackStack(null).commit();

        } else if (id == R.id.nav_ponuda) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new VrstePutovanja()).addToBackStack(null).commit();

        } else if (id == R.id.nav_rezervacije) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new  ThirdFragment()).addToBackStack(null).commit();

        } else if (id == R.id.nav_kontakt) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new  FourthFragment()).addToBackStack(null).commit();

        } else if (id == R.id.nav_o_nama) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new  FifthFragment()).addToBackStack(null).commit();

        }else if (id == R.id.nav_prijava) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new PrijavaFragment()).addToBackStack(null).commit();

        } else if (id == R.id.nav_registracija) {
            fragmentManager.beginTransaction().replace(
                    R.id.content_frame, new RegistracijaFragment()).addToBackStack(null).commit();

        } else  if(id == R.id.nav_odjava){
            if(PrijavaFragment.ulogovani != null) {
                Toast.makeText(getApplicationContext(), "Odjavili ste se!", Toast.LENGTH_LONG).show();
                txtUl.setText("Niste prijavljeni");
                PrijavaFragment.ulogovani = null;
                fragmentManager.beginTransaction().replace(R.id.content_frame, new PrijavaFragment()).addToBackStack(null).commit();
            } else {
                Toast.makeText(getApplicationContext(), "Niste se ni prijavili!", Toast.LENGTH_LONG).show();
            }
        } else if(id == R.id.night_mode){
            return false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
