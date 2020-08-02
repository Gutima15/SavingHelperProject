package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModificarGastoActivity extends AppCompatActivity {
    private ArrayList<String> etiquetas;
    private Singleton user;
    private Spinner spinner;
    private EditText nombreGasto;
    private EditText monto;
    private EditText fecha;
    private TextView saldoDisponoble;

    private String nombreOld;
    private String etiquetaOldName;
    private int etiquetaOldId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_gasto);

        user = Singleton.getInstance();
        cargarEtiquetas();
        spinner=findViewById(R.id.etiquetasDropDown);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, etiquetas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        Intent intent = getIntent(); //agarra lo que le pasa la vista anterior
        String etiquetaActual= intent.getStringExtra("EtiquetaActual");

        etiquetaOldName= etiquetaActual; //actualizo etiqueta oldName
        etiquetaOldId= getEtiquetaID(etiquetaOldName); //Tengo el ID de la etiqueta

        //llamar a procedimiento almacenado que me de el ID de la etiqueta (retorna un entero)
        spinner.setSelection(etiquetas.indexOf(etiquetaActual));
        Gasto gasto = intent.getParcelableExtra("gasto"); //el objeto gasto

        nombreGasto = findViewById(R.id.nombreGasto);
        monto = findViewById(R.id.monto);
        fecha = findViewById(R.id.editTextDate);
        //nombre
        nombreGasto.setText(gasto.getNombre());
        nombreOld= gasto.getNombre();
        //monto
        monto.setText( String.valueOf(gasto.getMonto()));
        //fecha
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        fecha.setText(format.format(gasto.getFecha()));
        //saldo
        saldoDisponoble = findViewById(R.id.SaldoDispobleMod);
        saldoDisponoble.setText(String.valueOf(getSaldoDisponible(user.ID)));
    }
    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public int getEtiquetaID(String nombreEti){
        CallableStatement cstmt = null;
        ResultSet rs = null;
        String SPsql = "EXEC usp_EtiquetaRead ?";   // for stored proc taking 2 parameters
        AzureConnection connexion= new AzureConnection();
        Connection con = connexion.connectionClass();
        PreparedStatement ps = null;
        int result=0;
        try {
            ps = con.prepareStatement(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(1000);
            ps.setInt(1,user.ID);
            rs = ps.executeQuery();
            while (rs.next()) {
                String IDEtiqueta = rs.getString("NombreEtiqueta");
                if (nombreEti == IDEtiqueta){
                    result = rs.getInt("IDetiqueta");
                }
            }
            con.close();


        } catch (SQLException e) {
            Log.d("G15",e.toString());
        }
        return result;
    }

    public int getGastoID(){ //Me devuelve el ID para poder hacer el update luego, uso los old para buscarlo ocupo, etiquetaID y nombreGasto
        CallableStatement cstmt = null;
        ResultSet rs = null;
        String SPsql = "EXEC usp_ReadGastoByUserID ?";   // recorro gastos del usuario
        AzureConnection connexion= new AzureConnection();
        Connection con = connexion.connectionClass();
        PreparedStatement ps = null;
        etiquetas = new ArrayList<>();
        int result =0;
        try {
            ps = con.prepareStatement(SPsql);
            ps.setEscapeProcessing(true);
            ps.setQueryTimeout(1000);
            ps.setInt(1,user.ID);
            rs = ps.executeQuery();
            while (rs.next()) {
                String nombreGast = rs.getString("NombreGasto");
                int IDEti = rs.getInt("IdEtiqueta");
                if(nombreGast == nombreOld & IDEti== etiquetaOldId){
                    result = rs.getInt("GastoID");
                }
            }
            con.close();
        } catch (SQLException e) {
            Log.d("G15",e.toString());
        }
        return result;
    }

    public void updateGastoValues(int IDGasto, int IDEtiqueta, float monto, String nombre, String fecha){
        String SPSQL = "{? = call usp_GastoUpdate (?,?,?,?,?)}";
        Boolean success = false;
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                success = false;
            }else{
                CallableStatement ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.setInt(1,getGastoID());
                ps.setInt(2,IDEtiqueta);
                ps.setFloat(3, monto);
                ps.setString(4,nombre);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                ps.setDate(5, (Date) format.parse(fecha));
                //
                ps.execute();
                con.close();
            }
        } catch (Exception ex) {
            success =false;
            Log.d("g15", ex.toString());
        }
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
            Log.d("G15",e.toString());
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

    public void modificarGasto(){
        nombreGasto = findViewById(R.id.nombreGasto);
        String nombreField= nombreGasto.getText().toString();

        monto = findViewById(R.id.monto);
        String montoField = monto.getText().toString();


        fecha = findViewById(R.id.editTextDate);
        String fechaField = fecha.getText().toString();

        spinner=findViewById(R.id.etiquetasDropDown);
        String spinerField = spinner.getSelectedItem().toString();
        if (!hayBlancos(nombreField,montoField,fechaField,spinerField)){
            if(!validarExpresionRegular(fechaField, "^([0-2][0-9]|3[0-1])(\\/)(0[1-9]|1[0-2])\\2(\\d{4})$")){
                Toast toast = Toast.makeText(getApplicationContext(), "Ingrese un formato válido de fecha. Ej: dd/mm/aaaa", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
            }else{
                //Funcionan tonses loa actualizo
                updateGastoValues(getGastoID(), getEtiquetaID(spinerField), Float.parseFloat(montoField), nombreField, fechaField);
                Intent intent = new Intent(this, EtiquetaActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Todos los campos deben poseer texto", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 280);
        }
    }

    public boolean validarExpresionRegular(String palabra, String expresionRegular ){
        Pattern p = Pattern.compile(expresionRegular);
        Matcher matcher = p.matcher(palabra);
        return matcher.matches();
    }

    public boolean hayBlancos(String nombre, String monto, String fecha, String etiqueta){
        if (nombre == "" | monto == ""| fecha == "" | etiqueta == ""){
            return true;
        }else{
            return false;
        }
    }

    public void activity_pantallaEtiqueta(View v) {
        Intent intent = new Intent(this, EtiquetaActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}