package com.example.holidaytravel.holidaytravel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Domen.Klijent;
import com.example.holidaytravel.holidaytravel.Domen.Ponuda;
import com.example.holidaytravel.holidaytravel.Domen.Rezervacija;
import com.example.holidaytravel.holidaytravel.PrijavaFragment;
import com.example.holidaytravel.holidaytravel.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@SuppressLint("ValidFragment")
public class KlijentFragment extends Fragment {
    View view;

    EditText editIme;
    EditText editPrezime;
    EditText editAdresa;
    EditText editBrTel;
    EditText editEmail;
    Button btnIzmeni;

    Klijent k;
    String korIme;

    @SuppressLint("ValidFragment")
    public KlijentFragment(String ki) {
        korIme = ki;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.klijent,container,false);

        editIme = (EditText) view.findViewById(R.id.editIme);
        editPrezime = (EditText) view.findViewById(R.id.editPrezime);
        editAdresa = (EditText) view.findViewById(R.id.editAdresa);
        editBrTel = (EditText) view.findViewById(R.id.editBrTel);
        editEmail = (EditText) view.findViewById(R.id.editEmail);
        btnIzmeni = (Button) view.findViewById(R.id.btnIzmeni);

        final DatabaseReference tabela = FirebaseDatabase.getInstance().getReference();

        k = PrijavaFragment.ulogovani;

        editIme.setText(k.getIme());
        editPrezime.setText(k.getPrezime());
        editAdresa.setText(k.getAdresa());
        editBrTel.setText(k.getBrTelefona());
        editEmail.setText(k.getEmail());

        btnIzmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Klijent novi = new Klijent(k.getLozinka(),editIme.getText().toString(),editPrezime.getText().toString(),editAdresa.getText().toString(),editBrTel.getText().toString(),editEmail.getText().toString());
                novi.setKorisnickoIme(k.getKorisnickoIme());
                tabela.child("Klijent").child(k.getKorisnickoIme()).setValue(novi);
                PrijavaFragment.ulogovani = novi;
                Toast.makeText(getContext(),"Podaci su izmenjeni",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
