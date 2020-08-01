package com.example.saving_helper.Utiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.saving_helper.DetalleEtiquetaActivity;
import com.example.saving_helper.MenuConIconos;
import com.example.saving_helper.ModificarGastoActivity;
import com.example.saving_helper.ModificarInfoUsuarioActivity;
import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetallesListAdapter extends ArrayAdapter<Gasto> {
    private Context mContext;
    private int mResource;
    private String etiqueta;

    public DetallesListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Gasto> objects, String etiquetaActual) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        etiqueta = etiquetaActual;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String nombre;
        Float monto;
        String fecha;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView textView_Nombre = (TextView) convertView.findViewById(R.id.textView_Nombre);
        TextView textView_Monto = (TextView) convertView.findViewById(R.id.textView_Monto);
        TextView textView_Fecha = (TextView) convertView.findViewById(R.id.textView_Fecha);
        Button btn_Editar = (Button) convertView.findViewById(R.id.btn_EditarGasto);


        if(position != 0){
             nombre = getItem(position).getNombre();
             monto = getItem(position).getMonto();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
             fecha = format.format(getItem(position).getFecha());
            textView_Nombre.setText(nombre);
            textView_Monto.setText(monto.toString());
            textView_Fecha.setText(fecha);
            btn_Editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Falta agregar salto a la otra vista
                    Intent intent = new Intent(getContext(), ModificarGastoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EtiquetaActual",etiqueta);
                    intent.putExtra("gasto",getItem(position));
                    mContext.startActivity(intent);
                }
            });

        }
        else{
            textView_Nombre.setText("Nombre");
            textView_Monto.setText("Monto");
            textView_Fecha.setText("Fecha");

            textView_Nombre.setTextColor(Color.parseColor("#003C8F"));
            textView_Monto.setTextColor(Color.parseColor("#003C8F"));
            textView_Fecha.setTextColor(Color.parseColor("#003C8F"));
            btn_Editar.setAlpha(0);
        }

        return convertView;
    }
}
