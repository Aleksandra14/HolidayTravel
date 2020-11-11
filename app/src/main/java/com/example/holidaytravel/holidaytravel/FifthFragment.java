package com.example.holidaytravel.holidaytravel;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class FifthFragment extends Fragment {
    View myView;

    TextView txtONama;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.o_nama, container, false);

        txtONama = (TextView) myView.findViewById(R.id.txtONama);

        String tekst = "Turistička agencija Holiday Travel osnovana je 1990. godine. Članica smo IATA-e od samog osnivanja.\n\n" +
                "Konstantno napredujući, uspeli smo, na osnovu rezultata poslovanja - da se svrstamo u red vodećih turističkih agencija na našem tržištu.\n\n" +
                "To smo postigli realizacijom ispravne poslovne politike koju sprovodi tim  u poslovnici koja je optimalno tehnički opremljena.\n\n" +
                "O našem ugledu rečito govori i podatak da više od 500 turističkih agencija prodaje aranžmane Holiday Travel-a, koji su kreirali naši komercijalisti.";
        txtONama.setText(tekst);
        txtONama.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);

        return myView;
    }
}
