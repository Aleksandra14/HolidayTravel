package com.example.holidaytravel.holidaytravel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Biblioteka.Metode;
import com.example.holidaytravel.holidaytravel.Domen.Ponuda;
import com.example.holidaytravel.holidaytravel.Domen.Rezervacija;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class DetaljiRezervacije extends Fragment {
    View myView;

    Rezervacija rezervacija;
    String kljucRez;

    TextView txtMesto;
    TextView txtKlijentInfo;
    TextView txtCena;
    TextView txtLM;
    TextView txtOpis;
    TextView txtDatum;
    TextView txtHotel;

    Button btnDodajUKalendar;
    Button btnSacuvajPdf;
    Button btnPogledajPonudu;
    Button btnOtkazi;

    Bitmap bitmap;
    Bitmap bitmap2;

    final DatabaseReference tabela = FirebaseDatabase.getInstance().getReference();

    @SuppressLint("ValidFragment")
    public DetaljiRezervacije(Rezervacija r, String kljuc) {
        rezervacija = r;
        kljucRez = kljuc;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detalji_rezervacije, container, false);
        txtMesto = (TextView) myView.findViewById(R.id.txtMesto2);
        txtKlijentInfo = (TextView) myView.findViewById(R.id.txtKlijentInfo);
        txtCena = (TextView) myView.findViewById(R.id.txtCena2);
        txtLM = (TextView) myView.findViewById(R.id.txtLastMinute2);
        txtOpis = (TextView) myView.findViewById(R.id.txtOpis22);
        txtDatum = (TextView) myView.findViewById(R.id.txtDatum22);
        txtHotel = (TextView) myView.findViewById(R.id.txtHotel2);

        btnDodajUKalendar = (Button) myView.findViewById(R.id.btnDodajUKalendar);
        btnSacuvajPdf = (Button) myView.findViewById(R.id.btnSacuvajPdf);
        btnPogledajPonudu = (Button) myView.findViewById(R.id.btnPogledajPonudu);
        btnOtkazi = (Button) myView.findViewById(R.id.btnOtkazi);

        txtMesto.setText("Rezervacija - " + rezervacija.getPonuda().getMesto());
        txtKlijentInfo.setText(rezervacija.getKlijent().podaciZaRez());
        txtCena.setText(txtCena.getText() + rezervacija.getPonuda().getCena() + "€");
        txtLM.setText(txtLM.getText() + rezervacija.getPonuda().getLastMinute());
        txtOpis.setText(rezervacija.getPonuda().getOpis());
        txtDatum.setText(rezervacija.getPonuda().datumi());
        txtHotel.setText(txtHotel.getText() + rezervacija.getPonuda().getHotel().toString());

        int i = getResources().getIdentifier(rezervacija.getPonuda().getSlika(),null,getContext().getPackageName());
        bitmap = BitmapFactory.decodeResource(getResources(),i); //slika ponude
        int j = getResources().getIdentifier("@drawable/svet1",null,getContext().getPackageName());
        bitmap2 = BitmapFactory.decodeResource(getResources(),j); //logo slika

        setButtons();

        return myView;
    }

    private void setButtons() {
        btnDodajUKalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubaciUKalendar(rezervacija.getPonuda());
                String poruka = "Vaša rezervacija " + rezervacija.getPonuda().getMesto() + " je uneta u kalendar :)";
                Snackbar.make(getActivity().findViewById(android.R.id.content), poruka, Snackbar.LENGTH_LONG).show();
            }
        });

        btnSacuvajPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sacuvajPdf(rezervacija);
            }
        });

        btnPogledajPonudu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,new DetaljiPonude(rezervacija.getPonuda())).addToBackStack(null).commit();
            }
        });

        btnOtkazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otkaziRezervaciju();
            }
        });
    }

    private void ubaciUKalendar(Ponuda ponuda){
        ContentResolver cr = getContext().getContentResolver(); //pocetak
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.CALENDAR_ID,1); //jer primarni calendar telefona ima id 1
        //cv.put(CalendarContract.Events.CALENDAR_ID,2);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE,Calendar.getInstance().getTimeZone().getID());

        cv.put(CalendarContract.Events.TITLE,ponuda.getVrsta());
        cv.put(CalendarContract.Events.DESCRIPTION,ponuda.josKraciPrikaz());
        cv.put(CalendarContract.Events.EVENT_LOCATION,ponuda.getHotel().getAdresa());

        long pocetak = Metode.getDateCurrentMillis(ponuda.getDatumOd());
        long kraj = Metode.getDateCurrentMillis(ponuda.getDatumDo());
        cv.put(CalendarContract.Events.DTSTART, pocetak);
        cv.put(CalendarContract.Events.DTEND, kraj);

        cv.put(CalendarContract.Events.STATUS,1); //1 za potvrdu
        cv.put(CalendarContract.Events.HAS_ALARM,1);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI,cv);

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

    private void sacuvajPdf(Rezervacija rezervacija) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            final PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595,842,1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.rgb(204,25,91));
            paint.setTextSize(20);
            canvas.drawText("Holiday Travel",10,25,paint);

            paint.setColor(Color.rgb(0,136,204)); //long
            //paint.setColor(Color.rgb(63,81,181));
            paint.setTextSize(30);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText("Rezervacija - " + rezervacija.getPonuda().getMesto(),170,75,paint);

            //paint.setColor(Color.red(230));
            paint.setColor(Color.rgb(230,51,116));
            paint.setTextSize(11);
            paint.setTextAlign(Paint.Align.LEFT);

            //canvas.drawText(rezervacija.getPonuda().getVrsta(),10,110,paint);
            //canvas.drawText(" ",10,120,paint);

            //String klijent = rezervacija.getKlijent().podaciZaRez();
            canvas.drawText("Klijent : " + rezervacija.getKlijent().getIme() + " " + rezervacija.getKlijent().getPrezime(),10,140,paint);
            canvas.drawText(rezervacija.getKlijent().getAdresa(),53,160,paint);
            canvas.drawText(rezervacija.getKlijent().getBrTelefona(),53,180,paint);
            canvas.drawText(rezervacija.getKlijent().getEmail(),53,200,paint);
            String cena = "Cena : " + rezervacija.getPonuda().getCena() + "€";
            canvas.drawText(cena,10,220,paint);
            String datum = "Datum : " + rezervacija.getPonuda().datumi();
            canvas.drawText(datum,10,240,paint);

            String opis = "Opis : " + rezervacija.getPonuda().getOpis();
            //Za multiline text
            TextPaint mTextPaint=new TextPaint();
            mTextPaint.setColor(Color.rgb(230,51,116));
            mTextPaint.setTextSize(11);
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            StaticLayout mTextLayout = new StaticLayout(opis, mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            canvas.save();
            int textX = 10;
            int textY = 250;
            canvas.translate(textX, textY);
            mTextLayout.draw(canvas);
            canvas.restore();

            //canvas.drawText(opis,10,180,paint);

            String hotel = "Hotel : " + rezervacija.getPonuda().getHotel().toString();
            canvas.drawText(hotel,10,300,paint);

            paint.reset();
            paint.setFilterBitmap(true);
            canvas.drawBitmap(bitmap2,200,310,paint);

            document.finishPage(page);

            final String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Documents/";
            File file = new File(directory_path);

            if(!file.exists()){
                file.mkdirs();
            }

            //String targetPdf = directory_path + "ponuda.pdf";
            // final File filePath = new File(targetPdf);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle("Želite da sačuvate rezervaciju kao pdf?");
            //alertDialog.setMessage("Unesite naziv dokumenta:");

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            //layout za message, tj za txtView i edtView
            LinearLayout layoutM = new LinearLayout(getContext());
            layoutM.setOrientation(LinearLayout.VERTICAL);
            params.setMargins(25,25,25,25);
            layoutM.setLayoutParams(params);
            layoutM.setPadding(25,25,25,25);
            layoutM.setBackgroundColor(getResources().getColor(R.color.colorBackground));

            //inicijalizacija edit text-a
            TextView txtView = new TextView(getContext());
            txtView.setLayoutParams(params);
            txtView.setText("Unesite naziv dokumenta:");
            txtView.setTextColor(getResources().getColor(R.color.colorCrna));
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);

            //inic edit text-a
            final EditText edtText = new EditText(getContext());
            edtText.setLayoutParams(params);
            edtText.setTextColor(Color.BLACK);
            edtText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);
            edtText.setBackgroundColor(Color.parseColor("#f075a2"));
            //edtText.setTypeface(null,Typeface.BOLD);
            edtText.setPadding(15,15,15,15);

            layoutM.addView(txtView);
            layoutM.addView(edtText);

            alertDialog.setView(layoutM);

            alertDialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                        if(edtText.getText().toString().equals("")){
                            Toast.makeText(getContext(),"Niste uneli naziv",Toast.LENGTH_LONG).show();
                        } else {
                            String targetPdf = directory_path + edtText.getText().toString() + ".pdf";
                            final File filePath = new File(targetPdf);
                            document.writeTo(new FileOutputStream(filePath));
                            //Toast.makeText(getContext(),"Dokument je sačuvan :)",Toast.LENGTH_SHORT).show();
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Dokument je sačuvan :)", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Log.e("main","error" + e.toString());
                        Toast.makeText(getContext(),"Došlo je do greške prilikom čuvanja dokumenta",Toast.LENGTH_LONG).show();
                    }
                    document.close();
                }
            });

            alertDialog.setNegativeButton("Ne",null);

            AlertDialog alert = alertDialog.create();
            alert.show();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Pdf ne može da se sačuva na ovom telefonu :(", Snackbar.LENGTH_LONG).show();
        }
    }

    private void otkaziRezervaciju(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Želite da otkažete rezervaciju?");

        alertDialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                tabela.child("Rezervacija").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Iterable<DataSnapshot> deca = dataSnapshot.getChildren();
                        for (DataSnapshot dete : deca) {
                            if(dete.getKey().equals(kljucRez)){
                                dete.getRef().removeValue();
                                Toast.makeText(getContext(),"Rezervacija je otkazana",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        alertDialog.setNegativeButton("Ne",null);

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

}
