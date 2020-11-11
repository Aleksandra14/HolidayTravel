package com.example.holidaytravel.holidaytravel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Domen.Klijent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class RegistracijaFragment extends Fragment {
    View myView;

    EditText txtKorIme;
    EditText txtLozinka;
    EditText txtIme;
    EditText txtPrezime;
    EditText txtAdresa;
    EditText txtBrTel;
    EditText txtEmail;

    Button btnRegistrujSe;

    Boolean registrovan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.registracija, container, false);

        txtKorIme = (EditText) myView.findViewById(R.id.editKorisnickoIme2);
        txtLozinka = (EditText) myView.findViewById(R.id.editLozinka2);
        txtIme = (EditText) myView.findViewById(R.id.editIme);
        txtPrezime = (EditText) myView.findViewById(R.id.editPrezime);
        txtAdresa = (EditText) myView.findViewById(R.id.editAdresa);
        txtBrTel = (EditText) myView.findViewById(R.id.editBrTelefona);
        txtEmail = (EditText) myView.findViewById(R.id.editEmail);

        btnRegistrujSe = (Button) myView.findViewById(R.id.btnRegistrujSe);

        registrovan = false;

        //inicijalizacija baze
        final DatabaseReference tabelaKlijent = FirebaseDatabase.getInstance().getReference().child("Klijent");

        btnRegistrujSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //klikom na dugme registrujSe dogodice se sledece:
                registrovan = false;
                final String unetoKorIme = txtKorIme.getText().toString();
                if (unetoKorIme.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli korisničko ime!!", Toast.LENGTH_LONG).show();
                    txtKorIme.requestFocus();
                    return;
                }

                final String unetaLozinka = txtLozinka.getText().toString();
                if (unetaLozinka.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli lozinku!!", Toast.LENGTH_LONG).show();
                    txtLozinka.requestFocus();
                    return;
                }

                final String unetoIme = txtIme.getText().toString();
                if (unetoIme.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli ime!!", Toast.LENGTH_LONG).show();
                    txtIme.requestFocus();
                    return;
                }
                final String unetoPrezime = txtPrezime.getText().toString();
                if (unetoPrezime.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli prezime!!", Toast.LENGTH_LONG).show();
                    txtPrezime.requestFocus();
                    return;
                }
                final String unetaAdresa = txtAdresa.getText().toString();
                if (unetaAdresa.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli adresu!!", Toast.LENGTH_LONG).show();
                    txtAdresa.requestFocus();
                    return;
                }
                final String unetBrTel = txtBrTel.getText().toString();
                if (unetBrTel.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli brTel!!", Toast.LENGTH_LONG).show();
                    txtBrTel.requestFocus();
                    return;
                }
                final String unetEmail = txtEmail.getText().toString();
                if (unetEmail.isEmpty()) {
                    Toast.makeText(getContext(), "Niste uneli e-mail!!", Toast.LENGTH_LONG).show();
                    txtEmail.requestFocus();
                    return;
                }

                final ProgressDialog mDialog = new ProgressDialog(getContext());
                mDialog.setMessage("Molimo Vas sačekajte...");
                mDialog.show();

                tabelaKlijent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //provera da li vec postoji uneto korisnicko ime
                        if (dataSnapshot.child(unetoKorIme).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(getContext(),"Korisničko ime već postoji!",Toast.LENGTH_LONG).show();
                            //registrovan = false;
                        } else {
                            mDialog.dismiss();
                            Klijent klijent = new Klijent(unetaLozinka, unetoIme, unetoPrezime, unetaAdresa, unetBrTel, unetEmail);
                            klijent.setKorisnickoIme(unetoKorIme);
                            tabelaKlijent.child(unetoKorIme).setValue(klijent);
                            Toast.makeText(getContext(), "Uspešno ste se registrovali!", Toast.LENGTH_LONG).show();
                            //registrovan = true;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Greska sa bazom!", Toast.LENGTH_LONG).show();
                        Log.w(TAG, "Failed to read value.", databaseError.toException());
                    }
                });

                /*if(!registrovan){
                    //Toast.makeText(getContext(),"Korisničko ime već postoji!",Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getContext(),"Uspešno ste se registrovali!",Toast.LENGTH_LONG).show();
                }*/
            }

        });
        return myView;
    }

}
