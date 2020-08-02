package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;

public class AgregarDinero extends AppCompatActivity {
    private Button btnAceptarAgregarDinero, btnCancelarAgregarDinero, btn_agregar_dinero_back;
    private Singleton user = Singleton.getInstance();
    private TextView lblSaldo;
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dinero);
        float saldoOnDB = get_saldo_DB();
        lblSaldo = findViewById(R.id.lblSaldo);
        lblSaldo.setText("₡" + df.format(saldoOnDB).toString());
        //botones
        btnAceptarAgregarDinero = findViewById(R.id.btnAceptarAgregarDinero);
        btnAceptarAgregarDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarDinero();
            }
        });
    }

    public void agregarDinero(){
        final EditText txfDineroAAgregar = findViewById(R.id.txfDineroAAgregar);
        final TextView lblSaldo = findViewById(R.id.lblSaldo);
        if(validar_nuevo_saldo(txfDineroAAgregar.getText().toString())){
            float actual = Float.parseFloat(lblSaldo.getText().toString().substring(1)); /*LO DE LA BASE*/
            float value = Float.parseFloat(txfDineroAAgregar.getText().toString()) + actual;
            String toLabel = "₡" + df.format(value).toString();
            set_saldo_DB(value);
            lblSaldo.setText(toLabel);
            txfDineroAAgregar.setText("");
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Indique un valor", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
        }

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

    private float get_saldo_DB(){
        float saldo = 0;
        int id = user.ID;
        String SPSQL = "exec usp_GetSaldoById " + String.valueOf(id);
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                Toast toast = Toast.makeText(getApplicationContext(), "Error: Conexion nula", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
            }else{
                ResultSet usp = con.createStatement().executeQuery(SPSQL);
                while(usp.next()){
                    saldo = usp.getFloat("Saldo");
                    break;
                }
                con.close();
            }
        } catch (Exception ex) {
            String error = "Error: " + ex.toString();
            Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
        }
        return saldo;
    }

    private void set_saldo_DB(float newSaldo){
        int id = user.ID;
        String SPSQL = "exec usp_SetSaldoById " + String.valueOf(id) + ", " + String.valueOf(newSaldo) ;
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                Toast toast = Toast.makeText(getApplicationContext(), "Error: Conexion nula", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
            }else{
                con.createStatement().execute(SPSQL);
                con.close();
            }
        } catch (Exception ex) {

            String error = "Error: " + ex.toString();
            Toast toast = Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
        }
    }

    private boolean validar_nuevo_saldo(String fromTxf){
        return !fromTxf.equals("");
    }
}