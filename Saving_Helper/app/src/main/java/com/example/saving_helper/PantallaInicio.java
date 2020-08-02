package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class PantallaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);
        updateMontoDisp();
        updateAhorroDiario();
    }

    public void activity_menu_con_iconos(View v) {
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openAgregarGasto(View v) {
        Intent intent = new Intent(this, AgregarGastoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    public void activity_agregar_dinero(View v) {
        Intent intent = new Intent(this, AgregarDinero.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    // monto disponible --------------------------------------------------------------------------//

    public void updateMontoDisp() {
        Singleton user = Singleton.getInstance();
        float montoDisp = getMontoDB(user.ID);
        String montoStr = "¢" + String.valueOf(montoDisp);
        Log.d("josebuddy", montoStr);
        TextView SaldoDisponibleMonto = findViewById(R.id.SaldoDisponibleMonto);
        SaldoDisponibleMonto.setText(montoStr);
    }


    public float getMontoDB(int id) {
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
    // monto disponible --------------------------------------------------------------------------//

    public void updateAhorroDiario(){
        Singleton user = Singleton.getInstance();
        String tipoPeriodo = getTipoPeriodo(user.ID);
        Float presupuesto = getPresupuestp(user.ID);
        Log.d("josebuddy", tipoPeriodo);

        if (tipoPeriodo.equals("Semanal") ) {
            presupuesto = presupuesto/7;
        }
        else if (tipoPeriodo.equals("Diario") ) {
            //1/1;
            presupuesto = presupuesto/1;
        }
        else if (tipoPeriodo.equals("Mensual")) {
            presupuesto = presupuesto/30;
        }
        String montoStr = "¢" + String.valueOf(presupuesto);
        Log.d("josebuddy", String.valueOf(presupuesto));
        TextView Diario = findViewById(R.id.AhorroDiarioMonto);
        Diario.setText(montoStr);


    }

    public float getPresupuestp(int id) {
        float presupuesto = 0;

        String SPSQL = "exec usp_GetPresupuesto " + String.valueOf(id);
        try {
            AzureConnection connexion = new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null) {
                //show error
                System.out.println("error");
            } else {
                ResultSet ps = con.createStatement().executeQuery(SPSQL);
                while (ps.next()) {
                    presupuesto = ps.getFloat("Pres");
                    break;
                }
                con.close();
            }
        } catch (Exception ex) {
            Log.d("josebuddy", ex.toString());
            ;
        }
        return presupuesto;
    }

    public String getTipoPeriodo(int userId){
        String nombrePeriodo = "";

        String SPSQL = "exec usp_GetNombrePeriodo " + String.valueOf(userId);
        try {
            AzureConnection connexion = new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null) {
                //show error
                System.out.println("error");
            } else {
                ResultSet ps = con.createStatement().executeQuery(SPSQL);
                while (ps.next()) {
                    nombrePeriodo = ps.getString("NomPeriodo");
                    break;
                }
                con.close();
            }
        } catch (Exception ex) {
            Log.d("josebuddy", ex.toString());;
        }
        return nombrePeriodo;


    }

}