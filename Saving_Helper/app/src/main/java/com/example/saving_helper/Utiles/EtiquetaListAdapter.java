package com.example.saving_helper.Utiles;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saving_helper.Objetos.Etiqueta;
import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.R;

import java.util.ArrayList;
import java.util.List;

public class EtiquetaListAdapter extends ArrayAdapter<Etiqueta> {

    private Context mContext;
    private int mResource;

    public EtiquetaListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Etiqueta> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nombre = getItem(position).getNombre();
        String color = getItem(position).getColor();
        ArrayList<Gasto> gastos = getItem(position).getGastos();


        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView textView_Nombre = (TextView) convertView.findViewById(R.id.textView_Nombre);
        ImageView image_Etiqueta = (ImageView) convertView.findViewById(R.id.image_Etiqueta);
        ListView listView_Gastos = (ListView) convertView.findViewById(R.id.listView_Gastos);





        textView_Nombre.setText(nombre);
        image_Etiqueta.setColorFilter(Color.parseColor(color));




        GastoListAdapter adapter = new GastoListAdapter(mContext, R.layout.layout_etiquetas_gastos,gastos);

        listView_Gastos.setAdapter(adapter);


        ViewGroup.LayoutParams params = listView_Gastos.getLayoutParams();
        params.height =  ((listView_Gastos.getDividerHeight()+100) * (gastos.size())) + 35;
        listView_Gastos.setLayoutParams(params);




        return convertView;
    }
}
