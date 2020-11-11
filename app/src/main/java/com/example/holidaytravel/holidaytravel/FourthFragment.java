package com.example.holidaytravel.holidaytravel;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Domen.Poslovnica;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FourthFragment extends Fragment {
    View myView;

    //TextView txt;
    TextView posl1;
    TextView posl2;
    TextView posl3;

    TextView tel1;
    TextView tel2;
    TextView tel3;

    List<Poslovnica> listaPosl;

    //SupportMapFragment mapFragment;
    MapView mMapView;
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.kontakt, container, false);

        //txt = (TextView) myView.findViewById(R.id.txtPoslovnice);
        posl1 = (TextView) myView.findViewById(R.id.txtPoslovnica1);
        posl2 = (TextView) myView.findViewById(R.id.txtPoslovnica2);
        posl3 = (TextView) myView.findViewById(R.id.txtPoslovnica3);

        tel1 = (TextView) myView.findViewById(R.id.txtTel1);
        tel2 = (TextView) myView.findViewById(R.id.txtTel2);
        tel3 = (TextView) myView.findViewById(R.id.txtTel3);

        listaPosl = new ArrayList<Poslovnica>();

        mMapView = (MapView) myView.findViewById(R.id.mapView);

        /*mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);*/

        final DatabaseReference tabela = FirebaseDatabase.getInstance().getReference();

        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Molimo Vas sačekajte...");
        mDialog.show();

        tabela.child("Poslovnica").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> deca = dataSnapshot.getChildren();
                for(DataSnapshot dete : deca){
                    listaPosl.add(dete.getValue(Poslovnica.class));
                    //Poslovnica poslovnica = new Poslovnica(dete.child("grad").getValue().toString(),dete.child("adresa").getValue().toString(),dete.child("latitude").getValue().toString(),dete.child("longitude").getValue().toString());
                    //listaPosl.add(poslovnica);
                }

                String poslovnice="";

                if(listaPosl.size() != 0){
                    for (Poslovnica p : listaPosl) {
                        poslovnice = poslovnice + p.toString() + "\n";
                    }
                    posl1.setText(listaPosl.get(0).skraceniToString());
                    tel1.setText(listaPosl.get(0).getTelefon());
                    //Linkify.addLinks(tel1, Linkify.PHONE_NUMBERS);

                    posl2.setText(listaPosl.get(1).skraceniToString());
                    tel2.setText(listaPosl.get(1).getTelefon());
                    //Linkify.addLinks(tel2, Linkify.ALL);

                    posl3.setText(listaPosl.get(2).skraceniToString());
                    tel3.setText(listaPosl.get(2).getTelefon());
                    //Linkify.addLinks(tel3, Linkify.ALL);

                }else {
                    poslovnice = "Trenutno nemamo nijednu poslovnicu :(";
                    posl1.setText(poslovnice);
                }

                tel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tel = listaPosl.get(0).getTelefon();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+tel));
                        startActivity(intent);
                    }
                });

                tel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tel = listaPosl.get(1).getTelefon();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+tel));
                        startActivity(intent);
                    }
                });

                tel3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tel = listaPosl.get(2).getTelefon();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+tel));
                        startActivity(intent);
                    }
                });

                //txt.setText("");
                //txt.setText(poslovnice);

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        googleMap = mMap;

                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        googleMap.setMyLocationEnabled(true);

                        for (Poslovnica p : listaPosl) {
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(p.getLatitude()),Double.parseDouble(p.getLongitude()))).title(p.getAdresa()).snippet("Naša poslovnica u " + p.getGrad() +"u"));
                        }

                        // postavljanje markera na google mapu
                /*googleMap.addMarker(new MarkerOptions().position(new LatLng(44.808546,20.458622)).title("Balkanska 51").snippet("Naša poslovnica u Beogradu"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.slika))) za sliku kod lokacije
                googleMap.addMarker(new MarkerOptions().position(new LatLng(45.248506,19.848840)).title("Stražilovska 27").snippet("Naša poslovnica u Novom Sadu"));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(43.315356,21.894667)).title("Obrenovačka 3").snippet("Naša poslovnica u Nišu"));*/

                        // za automatsko zumiranje na zadatu lokaciju
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(44.808546,20.458622)).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                    }
                });

                mDialog.dismiss();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //String poslovnice = "\n\nBalkanska 51, Beograd \nStražilovska 27, Novi Sad \nObrenovačka 3, Niš";
        //txt.setText(poslovnice);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

   /* @Override
    public void onMapReady(GoogleMap googleMap) {
        //MapsInitializer.initialize(getContext());

    }*/

}
