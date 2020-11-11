package com.example.holidaytravel.holidaytravel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Domen.Klijent;
import com.example.holidaytravel.holidaytravel.Domen.Rezervacija;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.holidaytravel.holidaytravel.R.string.text;

public class ThirdFragment extends Fragment {
    View myView;

    TextView txtNemaRez;
    //TextView txtPrikazRez2;

    ListView listView;
    ArrayAdapter adapter;

    final DatabaseReference tabela = FirebaseDatabase.getInstance().getReference();

    List<Rezervacija> listaRezervacija;
    Klijent ulogovani;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.rezervacije, container, false);

        txtNemaRez = (TextView) myView.findViewById(R.id.txtNemaRez);
        //txtPrikazRez2 = (TextView) myView.findViewById(R.id.txtPrikazRezervacija2);

        listView = (ListView) myView.findViewById(R.id.list);

        listaRezervacija = new ArrayList<>();
        ulogovani = PrijavaFragment.ulogovani;

        if(ulogovani != null){
            //final String klijent = "Ime: " + ulogovani.getIme() + "\n Prezime: " + ulogovani.getPrezime() + "\n Adresa: " + ulogovani.getAdresa() + "\n Broj telefona: " + ulogovani.getBrTelefona() + "\n E-mail: " + ulogovani.getEmail() ;
            txtNemaRez.setText("");

            final ProgressDialog mDialog = new ProgressDialog(getContext());
            mDialog.setMessage("Molimo Vas sačekajte...");
            mDialog.show();

            tabela.child("Rezervacija").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final Iterable<DataSnapshot> deca = dataSnapshot.getChildren();
                    //final String[] rezervacije = new String[(int) dataSnapshot.getChildrenCount()];
                    final List<String> listaKljuceva = new ArrayList<>();
                    for (DataSnapshot dete : deca) {
                        //rezervacije[i] = dete.getKey(); //uzima primarni kljuc svake rezervacije
                        if(dete.child("klijent").child("korisnickoIme").getValue().toString().equals(ulogovani.getKorisnickoIme())){
                            //if(dete.child("klijent").getValue(Klijent.class).equals(ulogovani)){
                            //rezervacije[i] = dete.child("ponuda").child("mesto").getValue().toString();
                            listaRezervacija.add(dete.getValue(Rezervacija.class));
                            listaKljuceva.add(dete.getKey());
                            //i++;
                        }
                    }

                    mDialog.dismiss();
                    /*final List<Rezervacija> lista2 = new ArrayList<>();
                    for(Rezervacija rez : listaRezervacija){
                        if(rez.getKlijent().getKorisnickoIme().equals(ulogovani.getKorisnickoIme()))
                            lista2.add(rez);
                    }*/
                    int i = 0;
                    final String[] nizRezMesta = new String[listaRezervacija.size()];
                    for (Rezervacija rez2 : listaRezervacija) {
                        nizRezMesta[i] = rez2.getPonuda().getMesto();
                        i++;
                    }

                    if(nizRezMesta.length != 0) {
                        adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, R.id.txtViewZaListView, nizRezMesta); //moze i bez treceg parametra
                        listView.setAdapter(adapter);

                       // txtPrikazRez2.setText("\n Kliknite na mesto za prikaz detalja rezervacije.");
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame,new DetaljiRezervacije(listaRezervacija.get(position),listaKljuceva.get(position))).addToBackStack(null).commit();
                            }
                        });

                        //sredjivanje scroll-a za list view
                        listView.setOnTouchListener(new ListView.OnTouchListener(){
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                int action = event.getAction();
                                switch (action){
                                    case MotionEvent.ACTION_DOWN:
                                        v.getParent().requestDisallowInterceptTouchEvent(true);
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        v.getParent().requestDisallowInterceptTouchEvent(false);
                                        break;
                                }
                                v.onTouchEvent(event);
                                return true;
                            }
                        });
                    } else {
                        txtNemaRez.setText( "Nemate nijednu rezervaciju :(");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

        } else {
            listView.setVisibility(View.GONE);
            txtNemaRez.setText("Rezervacije ne možete da pogledate ukoliko niste prijavljeni!");
        }
        return myView;
    }

}
