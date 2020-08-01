package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.saving_helper.Objetos.Etiqueta;
import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.Utiles.EtiquetaListAdapter;
import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EtiquetaActivity extends AppCompatActivity implements PopUpEtiquetaActivity.PopUpEtiquetasListener {
    private ArrayList<Etiqueta> etiquetas;
    private ListView mListView;
    private Singleton user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etiqueta);
        user = Singleton.getInstance();
        mListView = findViewById(R.id.listView);
        etiquetas = new ArrayList<>();

        cargarEtiquetas();
        actualizarListaEtiquetas(etiquetas);

    }

    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void OnClickAgregar(View view){
        PopUpEtiquetaActivity popUpEtiquetaActivity = new PopUpEtiquetaActivity();
        popUpEtiquetaActivity.show(getSupportFragmentManager(),"Pop up  agregar etiqueta");
    }


    @Override
    public void agregarEtiqueta(String nombre, String color) {

        Boolean success = false;
        if(etiquetas.contains(new Etiqueta(nombre,color))){
            Toast.makeText(getApplicationContext(),"Ya existe una etiqueta con ese nombre",Toast.LENGTH_SHORT).show();
        }
        else{
            String SPSQL = "{? = call usp_EtiquetaCreate (?,?,?)}";
            try{
                AzureConnection connexion= new AzureConnection();
                Connection con = connexion.connectionClass();
                if (con == null){
                    success = false;
                }else{
                    CallableStatement ps = con.prepareCall(SPSQL);
                    ps.setEscapeProcessing(true);
                    ps.setQueryTimeout(1000);
                    ps.registerOutParameter(1, java.sql.Types.INTEGER);
                    ps.setInt(2,user.ID);
                    ps.setString(3,color);
                    ps.setString(4,nombre);
                    //
                    ps.execute(); // ->1 / 0
                    int resultado= ps.getInt(1);
                    //
                    if (resultado==0){
                        success = true;
                    }else{
                        success=  false;
                    }
                    con.close();
                }
            } catch (Exception ex) {
                Log.d("Chompipe", ex.toString());
            }

            Etiqueta etiqueta = new Etiqueta(nombre,color);

            this.etiquetas.add(etiqueta);
            actualizarListaEtiquetas(etiquetas);
        }



    }

    public void actualizarListaEtiquetas(ArrayList<Etiqueta> listaEtiquetas){
        EtiquetaListAdapter adapter = new EtiquetaListAdapter(this, R.layout.layout_etiquetas,etiquetas);
        mListView.setAdapter(adapter);
    }

    public void cargarEtiquetas(){
        CallableStatement cstmt = null;
        ResultSet rs = null;
        String SPsql = "EXEC usp_EtiquetaRead ?";   // for stored proc taking 2 parameters
        AzureConnection connexion= new AzureConnection();
        Connection con = connexion.connectionClass();
        PreparedStatement ps = null;
        etiquetas = new ArrayList<>();
        try {
            ps = con.prepareStatement(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(1000);
            ps.setInt(1,user.ID);
            rs = ps.executeQuery();

            while (rs.next()) {
                String nombreEtiqueta = rs.getString("NombreEtiqueta");
                String color = rs.getString("ColorHex");
                Float montoGasto = rs.getFloat("MontoGasto");
                Etiqueta nuevaEtiqueta = new Etiqueta(nombreEtiqueta,color);

                if(!etiquetas.contains(nuevaEtiqueta)){
                    etiquetas.add(nuevaEtiqueta);
                }
                if(montoGasto != -1 ){
                    Gasto nuevoGasto = new Gasto(rs.getString("NombreGasto"),montoGasto,rs.getDate("Fecha"));
                    int i = etiquetas.indexOf(nuevaEtiqueta);
                    etiquetas.get(i).getGastos().add(nuevoGasto);
                }
            }
            con.close();
        } catch (SQLException e) {
            Log.d("Chompipe",e.toString());
        }
    }


}