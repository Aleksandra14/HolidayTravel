package com.example.holidaytravel.holidaytravel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.holidaytravel.holidaytravel.Domen.Ponuda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FirstFragment extends Fragment {
    View myView;

    GridView gridView ;

    List<Ponuda> listaLMPonuda;

    String[] nizDetalja;
    String[] nizSlika;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.grid_last_minute, container, false);

        gridView = (GridView) myView.findViewById(R.id.gridViewLastMinute);
        //textView = (TextView) myView.findViewById(R.id.textViewLastMinute);

        final DatabaseReference tabela = FirebaseDatabase.getInstance().getReference();

        listaLMPonuda = new ArrayList<>();

        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Molimo Vas sačekajte...");
        mDialog.show();

        tabela.child("Ponuda").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //uzima svu decu na nivou Ponude
                Iterable<DataSnapshot> deca = dataSnapshot.getChildren();
                for (DataSnapshot dete : deca){
                    if(dete.child("lastMinute").getValue().toString().equals("da"))
                        listaLMPonuda.add(dete.getValue(Ponuda.class));
                }

                nizDetalja = new String[listaLMPonuda.size()];
                nizSlika = new String[listaLMPonuda.size()];
                int i =0;
                for (Ponuda p : listaLMPonuda) {
                    nizDetalja[i] = p.lastMinutePrikaz();
                    nizSlika[i] = p.getSlika();
                    i++;
                }

                CustomAdapter customAdapter = new CustomAdapter(); //prikaz sa nazivom i slikom mesta
                gridView.setAdapter(customAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame,new DetaljiPonude(listaLMPonuda.get(position))).addToBackStack(null).commit();
                    }
                });

                mDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value." + databaseError.toString());
            }
        });

        return myView;
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return nizSlika.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view2 = getLayoutInflater().inflate(R.layout.grid_item_lm,null);

            TextView ime = (TextView) view2.findViewById(R.id.textViewLastMinute);
            ImageView slika = (ImageView) view2.findViewById(R.id.imgLastMinute);

            ime.setText(nizDetalja[position]);
            int imageRes = getResources().getIdentifier(nizSlika[position],null,getContext().getPackageName()); //radiii
            slika.setImageResource(imageRes);
            //slika.setBackgroundResource(imageRes);

            return view2;
        }
    }

}
