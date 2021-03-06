package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreferenceDialogFragmentCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText nombreGasto;
    private EditText monto;
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
        nombreGasto= findViewById(R.id.nombreGasto2);
        monto = findViewById(R.id.monto2);


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

    public void agregarGasto(View v){
        String spinerField = spinner.getSelectedItem().toString();
        String nombreG = nombreGasto.getText().toString();
        float montoBase = Float.valueOf(monto.getText().toString());
        String SPSQL = "{call usp_GastoCreate (?,?,?,?,?)}";
        try {
            AzureConnection connexion = new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null) {
                //show error
                System.out.println("error");
            } else {
                CallableStatement ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.setInt(1,user.ID);
                ps.setString(2,spinerField);
                ps.setFloat(3,montoBase);
                ps.setString(4,nombreG);

                java.util.Date fech =  new java.util.Date();
                java.sql.Date fechaSQL = new java.sql.Date(fech.getTime());
                ps.setDate(5, fechaSQL);
                ps.execute();
                con.close();
            }
        } catch (Exception ex) {
            Log.d("josebuddy", ex.toString());;
        }
        Toast toast = Toast.makeText(getApplicationContext(), "El gasto ha sido añadido",Toast.LENGTH_LONG );
        toast.show();
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);

        Intent intent = new Intent(this, EtiquetaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}