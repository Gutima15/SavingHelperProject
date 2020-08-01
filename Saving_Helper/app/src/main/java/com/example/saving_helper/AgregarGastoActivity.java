package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.saving_helper.Objetos.Etiqueta;
import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AgregarGastoActivity extends AppCompatActivity {
    private ArrayList<String> etiquetas;
    private Singleton user;
    private Spinner spinner;
    private TextView saldoDisponoble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_gasto);
        user = Singleton.getInstance();
        cargarEtiquetas();
        spinner=findViewById(R.id.etiquetasDropDown2);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, etiquetas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        Intent intent = getIntent(); //agarra lo que le pasa la vista anterior
        String etiquetaActual= intent.getStringExtra("EtiquetaActual");
        spinner.setSelection(etiquetas.indexOf(etiquetaActual));
        saldoDisponoble = findViewById(R.id.saldoDisponibleAgr);
        saldoDisponoble.setText(String.valueOf(getSaldoDisponible(user.ID)));
    }
    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void activity_pantalla_inicio(View v){
        Intent intent = new Intent(this, PantallaInicio.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                if(!etiquetas.contains(nombreEtiqueta)){
                    etiquetas.add(nombreEtiqueta);
                }
            }
            con.close();
        } catch (SQLException e) {
            Log.d("G15", e.toString());
        }
    }
    public float getSaldoDisponible(int id) {
        float saldoDisp = 0;

        String SPSQL = "exec usp_obtenerMonto " + String.valueOf(id);
        try {
            AzureConnection connexion = new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null) {
                //show error
                System.out.println("error");
            } else {
                ResultSet ps = con.createStatement().executeQuery(SPSQL);
                while (ps.next()) {
                    saldoDisp = ps.getFloat("SaldoActual");
                    break;
                }
                con.close();
            }
        } catch (Exception ex) {
            Log.d("josebuddy", ex.toString());;
        }
        return saldoDisp;
    }

}