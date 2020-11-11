package com.example.holidaytravel.holidaytravel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.Domen.Ponuda;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class GradoviFragment extends Fragment {
    View myView;

    GridView gridView ;

    String vrsta;

    DatabaseReference tabela;
    List<Ponuda> listaPonuda;
    String[] nizMesta;

    String[] nizSlika;

    AutoCompleteTextView autoCompleteTextView;

    @SuppressLint("ValidFragment")
    public GradoviFragment(String s) {
        vrsta = s;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.grid_mesta, container, false);

        gridView = (GridView) myView.findViewById(R.id.gridViewMesta);
        //gridView = (GridView) myView.findViewById(R.id.gridViewGradovi);


        listaPonuda = new ArrayList<>();

        autoCompleteTextView = (AutoCompleteTextView) myView.findViewById(R.id.autoCompleteTxtView);

        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Molimo Vas sačekajte...");
        mDialog.show();

        tabela.child("Ponuda").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //uzima svu decu na nivou Ponude
                Iterable<DataSnapshot> deca = dataSnapshot.getChildren();
                //
                for (DataSnapshot dete : deca) {
                    Ponuda p = dete.getValue(Ponuda.class);
                    if(p.getVrsta().equals(vrsta)){
                        listaPonuda.add(p);
                    }
                }

                nizMesta = new String[listaPonuda.size()];
                nizSlika = new String[listaPonuda.size()];
                int i =0;
                for (Ponuda p2:listaPonuda) {
                    nizMesta[i] = p2.getMesto();
                    nizSlika[i] = p2.getSlika();
                    i++;
                }
                //prikaziPonude();

                /*ArrayAdapter adapter = new ArrayAdapter(getContext(),R.layout.grid_item,R.id.textViewGrad,nizMesta); //za prikaz naziva ponude, bez slika backgound
                if(vrsta.equals("Letovanje")) {
                    adapter = new ArrayAdapter(getContext(),R.layout.grid_item_prva,R.id.textViewGrad,nizMesta);
                }else if(vrsta.equals("Evropski gradovi")){
                    adapter = new ArrayAdapter(getContext(),R.layout.grid_item_druga,R.id.textViewGrad,nizMesta);
                }else if(vrsta.equals("Svet")){
                    adapter = new ArrayAdapter(getContext(),R.layout.grid_item_treca,R.id.textViewGrad,nizMesta);
                }
                gridView.setAdapter(adapter);
                final ArrayAdapter finalAdapter = adapter;
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String value = finalAdapter.getItem(position);
                        //Toast.makeText(getContext(),((TextView)view).getText(),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(),value,Toast.LENGTH_LONG).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame,new DetaljiPonude(listaPonuda.get(position))).addToBackStack(null).commit();
                    }
                });*/

                //Adapter za auto complete
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,nizMesta);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        String odabran = (String)parent.getItemAtPosition(position);
                        Ponuda po = vratiPonuduZaMesto(odabran);
                        ft.replace(R.id.content_frame,new DetaljiPonude(po)).addToBackStack(null).commit();
                    }
                });

                CustomAdapter customAdapter = new CustomAdapter(); //prikaz sa nazivom i slikom mesta
                gridView.setAdapter(customAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame,new DetaljiPonude(listaPonuda.get(position))).addToBackStack(null).commit();
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

    private class CustomAdapter extends BaseAdapter{
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
            View view2 = getLayoutInflater().inflate(R.layout.grid_item3,null);

            TextView ime = (TextView) view2.findViewById(R.id.textViewMesta);
            ImageView slika = (ImageView) view2.findViewById(R.id.imgSlikeMesta);

            ime.setText(nizMesta[position]);
            int imageRes = getResources().getIdentifier(nizSlika[position],null,getContext().getPackageName()); //radiii
            //slika.setImageResource(imageRes);
            slika.setBackgroundResource(imageRes);

            return view2;
        }
    }

    private Ponuda vratiPonuduZaMesto(String mesto){
        for (Ponuda p2 : listaPonuda) {
            if(p2.getMesto().equals(mesto)) {
                return p2;
            }
        }
        return new Ponuda();
    }


}
