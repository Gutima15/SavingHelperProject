package com.example.saving_helper.Utiles;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GastoListAdapter extends ArrayAdapter<Gasto> {
    private Context mContext;
    private int mResource;

    public GastoListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Gasto> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nombre = getItem(position).getNombre();
        Float monto = getItem(position).getMonto();

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(getItem(position).getFecha());



        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView textView_Nombre = (TextView) convertView.findViewById(R.id.textView_Nombre);
        TextView textView_Monto = (TextView) convertView.findViewById(R.id.textView_Monto);
        TextView textView_Fecha = (TextView) convertView.findViewById(R.id.textView_Fecha);

        textView_Nombre.setText(nombre);
        textView_Monto.setText(monto.toString());
        textView_Fecha.setText(fecha);






        return convertView;
    }
}
