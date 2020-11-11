package com.example.holidaytravel.holidaytravel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Biblioteka.Metode;
import com.example.holidaytravel.holidaytravel.Domen.Klijent;
import com.example.holidaytravel.holidaytravel.Domen.Ponuda;
import com.example.holidaytravel.holidaytravel.Domen.Rezervacija;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SecondFragment extends Fragment {

    View myView;
    DatabaseHelper db;

    ImageView imgView1;
    ImageView imgView2;
    ImageView imgView3;
    ImageView imgView4;

    TextView txtPonuda1;
    TextView txtPonuda2;
    TextView txtPonuda3;
    TextView txtPonuda4;

    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    ScrollView scrollView;

    List<Ponuda> lista;

    DatabaseReference tabela;

    TextView[] nizT;
    ImageView[] nizI;

    Button btnDetalji1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.ponuda, container, false);

        imgView1 = (ImageView) myView.findViewById(R.id.img1);
        imgView2 = (ImageView) myView.findViewById(R.id.img2);
        imgView3 = (ImageView) myView.findViewById(R.id.img3);
        imgView4 = (ImageView) myView.findViewById(R.id.img4);


        txtPonuda1 = (TextView) myView.findViewById(R.id.txt1);
        txtPonuda2 = (TextView) myView.findViewById(R.id.txt2);
        txtPonuda3 = (TextView) myView.findViewById(R.id.txt3);
        txtPonuda4 = (TextView) myView.findViewById(R.id.txt4);

        nizT = new TextView[]{txtPonuda1,txtPonuda2,txtPonuda3,txtPonuda4};
        nizI = new ImageView[]{imgView1,imgView2,imgView3,imgView4};

        btn1 = (Button) myView.findViewById(R.id.btn1);
        btn2 = (Button) myView.findViewById(R.id.btn2);
        btn3 = (Button) myView.findViewById(R.id.btn3);
        btn4 = (Button) myView.findViewById(R.id.btn4);

        scrollView = (ScrollView) myView.findViewById(R.id.scrollViewID2);

        lista = new ArrayList<Ponuda>();

        btnDetalji1 = (Button) myView.findViewById(R.id.btnDetalji1);

        //final FirebaseDatabase baza = FirebaseDatabase.getInstance();
        //final DatabaseReference tabela = baza.getReference();
        tabela = FirebaseDatabase.getInstance().getReference();

        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Molimo Vas sačekajte...");
        mDialog.show();

        tabela.child("Ponuda").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //uzima svu decu na nivou Ponude
                Iterable<DataSnapshot> deca = dataSnapshot.getChildren();
                //
                for (DataSnapshot dete : deca) {
                    Ponuda p = dete.getValue(Ponuda.class);
                    lista.add(p);
                }
                prikaziSvePonude();
                mDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value." + databaseError.toString());
            }
        });

        setScroll();

        setButtons();

        return myView;
    }

    private void prikaziSvePonude(){
        int j = 0;
        for (TextView tv : nizT) {
            tv.setText(lista.get(j).toString());
            tv.setMovementMethod(new ScrollingMovementMethod());
            int imageRes = getResources().getIdentifier(lista.get(j).getSlika(),null,getContext().getPackageName());
            nizI[j].setImageResource(imageRes);
            j++;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setScroll(){
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda1.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        txtPonuda1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda1.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda2.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        txtPonuda2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda2.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda3.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        txtPonuda3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda3.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda4.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        txtPonuda4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                txtPonuda4.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    private void setButtons(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrijavaFragment.ulogovani==null){
                    Toast.makeText(getContext(),"Niste prijavljeni",Toast.LENGTH_LONG).show();
                    return;
                }
                //FragmentTransaction ft = getFragmentManager().beginTransaction();
                //ft.replace(R.id.content_frame,new RezervacijaFragment(1));
                prikaziProzorProvere(1);
               // ft.commit();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrijavaFragment.ulogovani==null){
                    Toast.makeText(getContext(),"Niste prijavljeni",Toast.LENGTH_LONG).show();
                    return;
                }
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,new RezervacijaFragment(2)).commit();*/
                prikaziProzorProvere(2);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrijavaFragment.ulogovani==null){
                    Toast.makeText(getContext(),"Niste prijavljeni",Toast.LENGTH_LONG).show();
                    return;
                }
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,new RezervacijaFragment(3)).commit();*/
                prikaziProzorProvere(3);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrijavaFragment.ulogovani==null){
                    Toast.makeText(getContext(),"Niste prijavljeni",Toast.LENGTH_LONG).show();
                    return;
                }
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,new RezervacijaFragment(4)).commit();*/
                prikaziProzorProvere(4);
            }
        });

        btnDetalji1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,new FirstFragment()).commit();
            }
        });
    }

    private void prikaziProzorProvere(int p){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Proverite podatke pre konačne rezervacije!");
        final Ponuda ponuda = lista.get(p-1);
        final Rezervacija r = new Rezervacija();
        final Klijent k = PrijavaFragment.ulogovani;

        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        TextView txtView = new TextView(getContext());
        //txtView.setLayoutParams(lp);
        txtView.setText(k.getIme() + " " + k.getPrezime() + "\n" + ponuda.toString() + "\n\n");
        txtView.setTypeface(Typeface.DEFAULT);
        txtView.setTextColor(Color.BLACK);
        txtView.setBottom(33);
        alertDialog.setView(txtView);

        alertDialog.setMessage("Da li ste sigurni?");

        // final EditText edt = new EditText(getContext());
        // edt.setLayoutParams(lp);
        // alertDialog.setView(edt);

        alertDialog.setNegativeButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Pravi rezervaciju
                r.setKlijent(k);
                r.setPonuda(ponuda);
                tabela.child("Rezervacija").push().setValue(r);

                //uzmi datum polaska i ubaci ga u kalendar korisnika
                ubaciUKalendar(ponuda);

                Toast.makeText(getContext(),"Vaša rezervacija je uneta :)",Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setPositiveButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void ubaciUKalendar(Ponuda ponuda){
        ContentResolver cr = getContext().getContentResolver(); //pocetak
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.CALENDAR_ID,1); //jer primarni calendar telefona ima id 1
        //cv.put(CalendarContract.Events.CALENDAR_ID,2);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().getTimeZone().getID());

        cv.put(CalendarContract.Events.TITLE,"Putovanje :)");
        cv.put(CalendarContract.Events.DESCRIPTION,ponuda.getOpis());
        cv.put(CalendarContract.Events.EVENT_LOCATION,ponuda.getMesto());

        long pocetak = Metode.getDateCurrentMillis(ponuda.getDatumOd());
        long kraj = Metode.getDateCurrentMillis(ponuda.getDatumDo());
        cv.put(CalendarContract.Events.DTSTART, pocetak);
        cv.put(CalendarContract.Events.DTEND, kraj);

        cv.put(CalendarContract.Events.STATUS,1); //1 za potvrdu
        cv.put(CalendarContract.Events.HAS_ALARM,1);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI,cv);

        //Toast.makeText(getContext(),"Uspesno dodat dogadjaj",Toast.LENGTH_SHORT).show();

        //long eventID = Long.parseLong(uri.getLastPathSegment());
        int id = Integer.parseInt(uri.getLastPathSegment());

        //Dodavanje Reminder-a dogadjaju (sa alert-om)
        ContentResolver cr2 = getContext().getContentResolver(); //radiiii
        ContentValues cv2 = new ContentValues();
        cv2.put(CalendarContract.Reminders.EVENT_ID,id);
        cv2.put(CalendarContract.Reminders.MINUTES,1440); //1 dan pre ide alert
        cv2.put(CalendarContract.Reminders.METHOD,CalendarContract.Reminders.METHOD_ALERT);
        Uri uri2 = cr2.insert(CalendarContract.Reminders.CONTENT_URI,cv2);

        ContentResolver cr22 = getContext().getContentResolver();
        ContentValues cv22 = new ContentValues();
        cv22.put(CalendarContract.Reminders.EVENT_ID,id);
        cv22.put(CalendarContract.Reminders.MINUTES,2880); //2 dana pre ide mejl
        cv22.put(CalendarContract.Reminders.METHOD,CalendarContract.Reminders.METHOD_EMAIL);
        Uri uri22 = cr22.insert(CalendarContract.Reminders.CONTENT_URI,cv22); //kraj
    }


}
