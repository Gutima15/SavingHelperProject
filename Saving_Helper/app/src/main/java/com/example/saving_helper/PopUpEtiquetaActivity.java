package com.example.saving_helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.saving_helper.Utiles.CuadroColorAdapater;

public class PopUpEtiquetaActivity extends AppCompatDialogFragment {
    private EditText editText_Nombre;
    private GridView gridView;
    private Button btn_Cancelar;
    private Button btn_Agregar;
    private String colorSeleccionado;
    private ImageView imageView_ColorActual;
    private PopUpEtiquetasListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_pop_up_etiqueta, null);
        builder.setView(view);

        editText_Nombre = view.findViewById(R.id.editText_PopUpEtiqueta_nombre);
        gridView = view.findViewById(R.id.grid_PopUpEtiqueta);
        btn_Agregar = view.findViewById(R.id.button_PopUpEtiqueta_Agregar);
        btn_Cancelar = view.findViewById(R.id.button_PopUpEtiqueta_Cancelar);
        imageView_ColorActual = view.findViewById(R.id.image_ColorCuadroActual);

        colorSeleccionado = "";




        btn_Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(datosCorrectos()){
                    listener.agregarEtiqueta(editText_Nombre.getText().toString(),colorSeleccionado);
                    getDialog().dismiss();
                }
                else {
                    Toast.makeText(getContext(), "Parametos incorrectos", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });





        final String[] colores = {"#F2E46B","#CFADAD","#CE4242","#15BCF9","#A662F9","#31BA5A","#CC44CC","#0CAD9A"};

        CuadroColorAdapater cuadroColorAdapater = new CuadroColorAdapater(getContext(),colores);

        gridView.setAdapter(cuadroColorAdapater);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               colorSeleccionado = colores[i];


                LinearLayout linearLayout = (LinearLayout) gridView.getChildAt(i);
                ImageView imageView =  (ImageView) linearLayout.getChildAt(0);;

                imageView_ColorActual.setImageDrawable(imageView.getDrawable());


            }
        });


        return builder.create();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PopUpEtiquetasListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement PopUpEtiquetasListener");
        }
    }

    public boolean datosCorrectos(){
        if(colorSeleccionado.equals("") || editText_Nombre.getText().toString().equals("")){
            return false;
        }
        else{
            return true;
        }
    }


    public interface PopUpEtiquetasListener {
        void agregarEtiqueta(String nombre, String color);
    }






}