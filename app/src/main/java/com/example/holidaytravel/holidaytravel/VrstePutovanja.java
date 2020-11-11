package com.example.holidaytravel.holidaytravel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.holidaytravel.holidaytravel.R;

public class VrstePutovanja extends Fragment {
    View myView;

    GridView gridView ;

    String[] nizVrsta = {"Letovanje","Evropski gradovi","Svet"};
    int[] nizSlika = {R.drawable.letovanje,R.drawable.evropskigradovi,R.drawable.svet2};
    //String[] nizSl = {"@drawable/parga2","@drawable/istanbul2","@drawable/newyork"}; //moze i ovako

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.vrste_putovanja, container, false);

        gridView = (GridView) myView.findViewById(R.id.gridViewVrstePutovanja);

        CustomAdapter customAdapter = new CustomAdapter();

        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getContext(),"Uspesan klik",Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,new GradoviFragment(nizVrsta[position])).addToBackStack(null).commit();
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
            View view2 = getLayoutInflater().inflate(R.layout.grid_item2,null);

            TextView ime = (TextView) view2.findViewById(R.id.textViewVrsta);
            ImageView slika = (ImageView) view2.findViewById(R.id.imgSlikeVrsta);

            ime.setText(nizVrsta[position]);
            slika.setImageResource(nizSlika[position]);

            //int imageRes = getResources().getIdentifier(nizSl[position],null,getContext().getPackageName()); //radiii
            //slika.setImageResource(imageRes);

            return view2;
        }
    }




}
