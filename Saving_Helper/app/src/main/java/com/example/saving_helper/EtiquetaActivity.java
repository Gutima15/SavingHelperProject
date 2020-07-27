package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.saving_helper.Objetos.Etiqueta;
import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.Utiles.EtiquetaListAdapter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EtiquetaActivity extends AppCompatActivity implements PopUpEtiquetaActivity.PopUpEtiquetasListener {
    private ArrayList<Etiqueta> etiquetas;
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiqueta);

        mListView = findViewById(R.id.listView);

        //Datos de ejemplo
        Etiqueta transporte = new Etiqueta("Transporte","#DE4646");
        Etiqueta alimentacion = new Etiqueta("Alimentación","#F8CACA");
        Etiqueta gastoG = new Etiqueta("Gastos generales","#FDF39E");
        Etiqueta dormir = new Etiqueta("Dormir","#FF69E853");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");


        Gasto transpote1 = null;
        Gasto transpote2 = null;
        Gasto transpote3 = null;
        Gasto transpote4 = null;
        Gasto alimentacion1 = null;
        Gasto alimentacion2 = null;
        Gasto alimentacion3 = null;
        Gasto alimentacion4 = null;
        Gasto gastosG1 = null;
        Gasto dormir1 = null;
        try {
            transpote1 = new Gasto("Taxi",5000, format.parse("26/05/2020"));
            transpote2 = new Gasto("Uber",4750, format.parse("28/05/2020"));
            transpote3 = new Gasto("Uber Heredia",7500, format.parse("29/05/2020"));



            alimentacion1 = new Gasto("Sangu",3500, format.parse("25/05/2020"));
            alimentacion2 = new Gasto("Sangu",3500, format.parse("28/05/2020"));
            alimentacion3 = new Gasto("Promo sushi",12000, format.parse("30/05/2020"));
            alimentacion4 = new Gasto("Villa Italia",10000, format.parse("31/05/2020"));

            gastosG1 = new Gasto("Calculadora científica",13000, format.parse("30/05/2020"));

            dormir1 = new Gasto("El costo de estar en el TEC",22500, format.parse("15/05/2020"));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ArrayList<Gasto> arrayListTranporte = new ArrayList<Gasto>();
        arrayListTranporte.add(transpote1);
        arrayListTranporte.add(transpote2);
        arrayListTranporte.add(transpote3);
        transporte.setGastos(arrayListTranporte);



        ArrayList<Gasto> arrayListAlimentacion = new ArrayList<Gasto>();
        arrayListAlimentacion.add(alimentacion1);
        arrayListAlimentacion.add(alimentacion2);
        arrayListAlimentacion.add(alimentacion3);
        arrayListAlimentacion.add(alimentacion4);
        alimentacion.setGastos(arrayListAlimentacion);



        ArrayList<Gasto> arrayListGastosG = new ArrayList<Gasto>();
        arrayListGastosG.add(gastosG1);

        gastoG.setGastos(arrayListGastosG);

        ArrayList<Gasto> arrayListDormir = new ArrayList<>();
        arrayListDormir.add(dormir1);

        dormir.setGastos(arrayListDormir);

        etiquetas = new ArrayList<>();

        etiquetas.add(transporte);
        etiquetas.add(alimentacion);
        etiquetas.add(gastoG);
        etiquetas.add(dormir);





        actualizarListaEtiquetas(etiquetas);



    }


    public void OnClickAgregar(View view){
        PopUpEtiquetaActivity popUpEtiquetaActivity = new PopUpEtiquetaActivity();
        popUpEtiquetaActivity.show(getSupportFragmentManager(),"Pop up  agregar etiqueta");
    }


    @Override
    public void agregarEtiqueta(String nombre, String color) {
        Etiqueta etiqueta = new Etiqueta(color,nombre);

        this.etiquetas.add(etiqueta);
        actualizarListaEtiquetas(etiquetas);


    }

    public void actualizarListaEtiquetas(ArrayList<Etiqueta> listaEtiquetas){
        EtiquetaListAdapter adapter = new EtiquetaListAdapter(this, R.layout.layout_etiquetas,etiquetas);
        mListView.setAdapter(adapter);
    }
}