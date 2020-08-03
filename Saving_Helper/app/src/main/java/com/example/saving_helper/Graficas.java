package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.saving_helper.Utiles.Singleton;

import java.sql.Connection;
import java.sql.ResultSet;

public class Graficas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficas);
        Singleton user = Singleton.getInstance();
        setTopGastos();
        setTopGastosEtiqueta();
    }

    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void setTopGastos() {
        Singleton user = Singleton.getInstance();
        String SPSQL = "exec usp_Top3GastosPeriodo " + String.valueOf(user.ID);
        try {
            AzureConnection connexion = new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null) {
                //show error
                System.out.println("error");
            } else {
                ResultSet ps = con.createStatement().executeQuery(SPSQL);
                int cont = 1;

                String Valor1="";
                String Valor2="";
                String Valor3="";

                while (ps.next()) {
                    if (cont == 1){

                        Valor1 ="1. " + ps.getString("NombreGasto") + " , " + ps.getString("MontoGasto");
                        cont++;
                    }
                    else if (cont == 2){
                        Valor2 = "2. " + ps.getString("NombreGasto") + " , " + ps.getString("MontoGasto");
                        cont++;
                    }

                    else if (cont == 3){
                        Valor3 = "3. " + ps.getString("NombreGasto") + " , " + ps.getString("MontoGasto");
                        cont++;
                    }
                    else {
                        break;
                    }

                }
                TextView top1= findViewById(R.id.Gasto1);
                top1.setText(Valor1);
                TextView top2= findViewById(R.id.Gasto2);
                top2.setText(Valor2);
                TextView top3= findViewById(R.id.Gasto3);
                top3.setText(Valor3);
                con.close();

            }

        } catch (Exception ex) {
            Log.d("josebuddy", ex.toString());
        }
    }

    public void setTopGastosEtiqueta() {
        Singleton user = Singleton.getInstance();
        String SPSQL = "exec usp_top3GastosPorEtiqueta " + String.valueOf(user.ID);
        try {
            AzureConnection connexion = new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null) {
                //show error
                System.out.println("error");
            } else {
                ResultSet ps = con.createStatement().executeQuery(SPSQL);
                int cont = 1;

                String Valor1="";
                String Valor2="";
                String Valor3="";

                while (ps.next()) {
                    if (cont == 1){

                        Valor1 ="1. " + ps.getString("nombreEtiqueta") + " , " + ps.getString("gastoEtiqueta");
                        cont++;
                    }
                    else if (cont == 2){
                        Valor2 = "2. " + ps.getString("nombreEtiqueta") + " , " + ps.getString("gastoEtiqueta");
                        cont++;
                    }

                    else if (cont == 3){
                        Valor3 = "3. " + ps.getString("nombreEtiqueta") + " , " + ps.getString("gastoEtiqueta");
                        cont++;
                    }
                    else {
                        break;
                    }

                }
                TextView top1= findViewById(R.id.GastoEtiqueta1);
                top1.setText(Valor1);
                TextView top2= findViewById(R.id.GastoEtiqueta2);
                top2.setText(Valor2);
                TextView top3= findViewById(R.id.GastoEtiqueta3);
                top3.setText(Valor3);
                con.close();

            }

        } catch (Exception ex) {
            Log.d("josebuddy", ex.toString());
        }
    }


}