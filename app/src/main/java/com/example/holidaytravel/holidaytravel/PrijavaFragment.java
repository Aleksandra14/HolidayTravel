package com.example.holidaytravel.holidaytravel;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Domen.Klijent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrijavaFragment extends Fragment {
    View myView;

    EditText txtKorIme;
    EditText txtLozinka;

    Button btnPrijaviSe;

    String unetoKorIme;

    ImageButton togglePass;
    TextView toggleText;

    public static Klijent ulogovani = null;
    //TextView txtUlogovani;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.prijava, container, false);

        txtKorIme = (EditText) myView.findViewById(R.id.editTextKorisnickoIme);
        txtLozinka = (EditText) myView.findViewById(R.id.editTextLozinka);
        btnPrijaviSe = (Button) myView.findViewById(R.id.btnPrijaviSe);

        togglePass = (ImageButton) myView.findViewById(R.id.togglePass);
        toggleText = (TextView) myView.findViewById(R.id.toggleText);

        //txtUlogovani = (TextView) myView.findViewById(R.id.txtUlogovani);

        //inicijalizacija baze
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tabelaKlijent = database.getReference("Klijent");

        togglePass.setVisibility(View.GONE);
        txtLozinka.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        txtKorIme.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        txtLozinka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(txtLozinka.getText().length() > 0)
                    togglePass.setVisibility(View.VISIBLE);
                else
                    togglePass.setVisibility(View.GONE);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        togglePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toggleText.getText().toString().equals("prikazi")){
                    //Toast.makeText(getContext(),"kliknuo da otkrije",Toast.LENGTH_LONG).show();
                    togglePass.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    toggleText.setText("sakrij");
                    txtLozinka.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    txtLozinka.setSelection(txtLozinka.length()); //stavlja kursor na poslednji index
                } else {
                    //Toast.makeText(getContext(),"da sakrije",Toast.LENGTH_LONG).show();
                    togglePass.setImageResource(R.drawable.ic_visibility_black_24dp);
                    toggleText.setText("prikazi");
                    txtLozinka.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    txtLozinka.setSelection(txtLozinka.length());
                }
            }
        });


        btnPrijaviSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //

                if(ulogovani!=null){
                    Toast.makeText(getContext(),"Već ste ulogovani!! Odjavite se za ponovno logovanje!",Toast.LENGTH_LONG).show();
                    return;
                }

                unetoKorIme = txtKorIme.getText().toString();
                if(unetoKorIme.isEmpty()){
                    Toast.makeText(getContext(),"Niste uneli korisničko ime!!",Toast.LENGTH_LONG).show();
                    txtKorIme.requestFocus();
                    return;
                }
                final String unetaLozinka = txtLozinka.getText().toString();
                if(unetaLozinka.isEmpty()){
                    Toast.makeText(getContext(),"Niste uneli lozinku!!",Toast.LENGTH_LONG).show();
                    txtLozinka.requestFocus();
                    return;
                }

                final ProgressDialog mDialog = new ProgressDialog(getContext());
                mDialog.setMessage("Molimo Vas sačekajte...");
                mDialog.show();

                tabelaKlijent.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //proverava da li klijent postoji u bazi
                        if(dataSnapshot.child(unetoKorIme).exists()){

                            //uzimamo informacije postojeceg klijenta
                            mDialog.dismiss();
                            Klijent klijent = dataSnapshot.child(unetoKorIme).getValue(Klijent.class);
                            if(klijent.getLozinka().equals(unetaLozinka)){
                                Toast.makeText(getContext(),"Uspešno ste se prijavili!",Toast.LENGTH_SHORT).show();
                                ulogovani = klijent;
                                MainActivity.txtUl.setText(ulogovani.getKorisnickoIme());

                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.content_frame,new FirstFragment()).addToBackStack(null).commit();
                                //myView = inflater.inflate(R.layout.activity_main, container, false);
                            }else
                            {
                                Toast.makeText(getContext(),"Nije dobra lozinka!",Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getContext(),"Korisničko ime ne postoji!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return myView;
    }

}
