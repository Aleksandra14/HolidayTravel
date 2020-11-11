package com.example.holidaytravel.holidaytravel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
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
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Biblioteka.Metode;
import com.example.holidaytravel.holidaytravel.Domen.Klijent;
import com.example.holidaytravel.holidaytravel.Domen.Ponuda;
import com.example.holidaytravel.holidaytravel.Domen.Rezervacija;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.DocumentsContract.*;

@SuppressLint("ValidFragment")
public class DetaljiPonude extends Fragment {
    View myView;

    Ponuda ponuda;

    TextView txtMesto;
    TextView txtCena;
    TextView txtLM;
    TextView txtOpis;
    TextView txtDatum;
    TextView txtHotel;
    Button btrRezervisi;
    Button btnSaznaj;
    Button btnTip;
    RelativeLayout rl;
    int i;
    Button btnKreirajPdf;

    MapView mapView;
    private GoogleMap googleMap;

    DatabaseReference tabela;

    ImageButton imgSlika;
    Bitmap bitmap;
    Bitmap bitmap2;

    @SuppressLint("ValidFragment")
    public DetaljiPonude(Ponuda ponuda) {
        this.ponuda = ponuda;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.detalji_ponude, container, false);
        txtMesto = (TextView) myView.findViewById(R.id.txtMesto);
        txtCena = (TextView) myView.findViewById(R.id.txtCena);
        txtLM = (TextView) myView.findViewById(R.id.txtLastMinute);
        txtOpis = (TextView) myView.findViewById(R.id.txtOpis2);
        txtDatum = (TextView) myView.findViewById(R.id.txtDatum2);
        txtHotel = (TextView) myView.findViewById(R.id.txtHotel);
        btrRezervisi = (Button) myView.findViewById(R.id.btnRezervisi);
        btnSaznaj = (Button) myView.findViewById(R.id.btnSaznajVise);
        btnTip = (Button) myView.findViewById(R.id.btnPromeniTip);
        btnKreirajPdf = (Button) myView.findViewById(R.id.btnKreirajPdf);

        rl = (RelativeLayout) myView.findViewById(R.id.layoutMesto);

        tabela = FirebaseDatabase.getInstance().getReference();

        imgSlika = (ImageButton) myView.findViewById(R.id.imgSlika);

        txtMesto.setText(ponuda.getMesto());
        txtCena.setText(txtCena.getText() + ponuda.getCena() + "€");
        txtLM.setText(txtLM.getText() + ponuda.getLastMinute());
        txtOpis.setText(ponuda.getOpis());
        txtDatum.setText(ponuda.datumi());
        txtHotel.setText(txtHotel.getText() + ponuda.getHotel().toString());

        i = getResources().getIdentifier(ponuda.getSlika(),null,getContext().getPackageName());
        //rl.setBackgroundResource(i); moze
        imgSlika.setBackgroundResource(i);
        bitmap = BitmapFactory.decodeResource(getResources(),i);
        int j = getResources().getIdentifier("@drawable/svet1",null,getContext().getPackageName());
        bitmap2 = BitmapFactory.decodeResource(getResources(),j);

        mapView = (MapView) myView.findViewById(R.id.mapa2);

        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                // za prikaz promene na moju lokaciju button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // postavljanje markera na google mapu
                googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ponuda.getHotel().getLatitude()),Double.parseDouble(ponuda.getHotel().getLongitude()))).title(ponuda.getHotel().getAdresa()).snippet("Lokacija hotela"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.slika))) za sliku kod lokacije

                // za automatsko zumiranje na zadatu lokaciju
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(Double.parseDouble(ponuda.getHotel().getLatitude()),Double.parseDouble(ponuda.getHotel().getLongitude()))).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
            }
        });

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setButtons();

        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setButtons(){
        btrRezervisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrijavaFragment.ulogovani==null){
                    Toast.makeText(getContext(),"Niste prijavljeni",Toast.LENGTH_LONG).show();
                    return;
                }
                prikaziProzorProvere();
            }
        });
        btnSaznaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(ponuda.getSaznajVise()));
                startActivity(browserIntent);
            }
        });
        btnTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(googleMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                else
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        imgSlika.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Toast.makeText(getContext(),"Dugi klik",Toast.LENGTH_LONG).show();
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Želite da sačuvate sliku?");

                alertDialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if( (MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),bitmap,"naslov","opis")) != null )
                            Snackbar.make(getActivity().findViewById(android.R.id.content), "Slika je sačuvana :)", Snackbar.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(),"Slika nije sačuvana :(",Toast.LENGTH_SHORT).show();
                    }
                });

                alertDialog.setNegativeButton("Ne",null);

                AlertDialog alert = alertDialog.create();
                alert.show();

                return true;
            }
        });

        btnKreirajPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    final PdfDocument document = new PdfDocument();
                    //Rect r = new Rect(0,0,100,100);
                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595,842,1).create();
                    PdfDocument.Page page = document.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    Paint paint = new Paint();
                    int primarnaTamna = 2042591;
                    int primarna =23051116;
                    int plava = 0136204;
                    //paint.setColor(Color.RED);
                    //canvas.drawCircle(50,50,30,paint);
                    //paint.setColor(R.color.colorPrimary);
                   // paint.setColor(-65536);
                    paint.setColor(Color.rgb(204,25,91));
                    paint.setTextSize(20);
                    canvas.drawText("Holiday Travel",10,25,paint);

                    paint.setColor(Color.rgb(0,136,204)); //long
                    //paint.setColor(Color.rgb(63,81,181));
                    paint.setTextSize(30);
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawText(ponuda.getMesto(),196,75,paint);

                    //paint.setColor(Color.red(230));
                    paint.setColor(Color.rgb(230,51,116));
                    paint.setTextSize(11);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.drawText(ponuda.getVrsta(),10,110,paint);
                    canvas.drawText(" ",10,120,paint);
                    String cena = "Cena: " + ponuda.getCena() + "€";
                    canvas.drawText(cena,10,140,paint);
                    String datum = "Datum: " + ponuda.datumi();
                    canvas.drawText(datum,10,160,paint);


                    String opis = "Opis: " + ponuda.getOpis();
                    //Za multiline text
                    TextPaint mTextPaint=new TextPaint();
                    mTextPaint.setColor(Color.rgb(230,51,116));
                    mTextPaint.setTextSize(11);
                    mTextPaint.setTextAlign(Paint.Align.LEFT);
                    StaticLayout mTextLayout = new StaticLayout(opis, mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    canvas.save();
                    int textX = 10;
                    int textY = 170;

                    canvas.translate(textX, textY);
                    mTextLayout.draw(canvas);
                    canvas.restore();

                    //canvas.drawText(opis,10,180,paint);

                    String hotel = "Hotel: " + ponuda.getHotel().toString();
                    canvas.drawText(hotel,10,220,paint);

                    paint.reset();
                    paint.setFilterBitmap(true);
                    canvas.drawBitmap(bitmap2,200,230,paint);

                    //View content = (View) getView().getParent();
                    //content.draw(page.getCanvas());

                    document.finishPage(page);

                    final String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Documents/";
                    File file = new File(directory_path);

                    //File outputFile = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));

                    if(!file.exists()){
                        file.mkdirs();
                    }

                    //String targetPdf = directory_path + "ponuda.pdf";
                   // final File filePath = new File(targetPdf);

                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setTitle("Želite da sačuvate ponudu kao pdf?");
                    //alertDialog.setMessage("Unesite naziv dokumenta:");

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    //layout za message, tj za txtView i edtView
                    LinearLayout layoutM = new LinearLayout(getContext());
                    layoutM.setOrientation(LinearLayout.VERTICAL);
                    params.setMargins(25,25,25,25);
                    layoutM.setLayoutParams(params);
                    layoutM.setPadding(25,25,25,25);
                    layoutM.setBackgroundColor(getResources().getColor(R.color.colorBackground));

                    //inicijalizacija text view-a
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

                    /*NumberPicker np = new NumberPicker(getContext());
                    np.setLayoutParams(params);
                    np.setMinValue(1);
                    np.setMaxValue(5);
                    np.setWrapSelectorWheel(false); //da stane kad dodje do min/max vrednosti*/

                    layoutM.addView(txtView);
                    layoutM.addView(edtText);
                    //layoutM.addView(np);

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

                    /*alertDialog.setPositiveButton("Ne", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });*/

                    alertDialog.setNegativeButton("Ne",null);

                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }
        });
    }

    private void prikaziProzorProvere() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Proverite podatke pre konačne rezervacije!");
        final Klijent k = PrijavaFragment.ulogovani;
        final Ponuda p = ponuda;
        final Rezervacija r = new Rezervacija();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        //layout za message, tj za txtView
        LinearLayout layoutM = new LinearLayout(getContext());
        layoutM.setOrientation(LinearLayout.VERTICAL);
        params.setMargins(25,25,25,25);
        layoutM.setLayoutParams(params);
        layoutM.setPadding(25,25,25,25);
        layoutM.setBackgroundColor(getResources().getColor(R.color.colorBackground));

        TextView txtView = new TextView(getContext());
        txtView.setLayoutParams(params);
        txtView.setText(k.getIme() + " " + k.getPrezime() + "\n\n" + ponuda.kraciPrikaz());
        txtView.setTextColor(getResources().getColor(R.color.colorCrna));
        txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,17);

        TextView txtView2 = new TextView(getContext());
        txtView2.setLayoutParams(params);
        txtView2.setText("Da li ste sigurni?");
        txtView.setTextColor(getResources().getColor(R.color.colorCrna));
        txtView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);

        layoutM.addView(txtView);
        layoutM.addView(txtView2);
        alertDialog.setView(layoutM);

        /*//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        TextView txtView = new TextView(getContext());
        //txtView.setLayoutParams(lp);
        txtView.setText("\t\t\t" + k.getIme() + " " + k.getPrezime() + "\n" + ponuda.kraciPrikaz() + "\n\n" + "\t\t\tDa li ste sigurni?");
        txtView.setTypeface(Typeface.DEFAULT);
        txtView.setTextColor(Color.BLACK);
        txtView.setBottom(33);
        txtView.setTextSize(20);
        alertDialog.setView(txtView);*/

        //alertDialog.setMessage("Da li ste sigurni?");

        // final EditText edt = new EditText(getContext());
        // edt.setLayoutParams(lp);
        // alertDialog.setView(edt);

        alertDialog.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Pravi rezervaciju
                r.setKlijent(k);
                r.setPonuda(p);
                tabela.child("Rezervacija").push().setValue(r);

                prikaziProzorCalendar();

                //uzmi datum polaska i ubaci ga u kalendar korisnika
                //ubaciUKalendar(p);

                Toast.makeText(getContext(),"Vaša rezervacija je uneta :)",Toast.LENGTH_LONG).show();
            }
        });

        alertDialog.setNegativeButton("Ne",null);

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    private void prikaziProzorCalendar(){
        final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(getContext());
        alertDialog2.setTitle("Da li želite da ubacite rezervisanu ponudu u kalendar?");

        alertDialog2.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //uzmi datum polaska i ubaci ga u kalendar korisnika
                ubaciUKalendar(ponuda);
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Vaša rezervacija je uneta u kalendar :)", Snackbar.LENGTH_LONG).show();
            }
        });

        alertDialog2.setNegativeButton("Ne",null);

        AlertDialog alert2 = alertDialog2.create();
        alert2.show();
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
