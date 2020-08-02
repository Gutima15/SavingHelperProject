package com.example.saving_helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

import com.example.saving_helper.Utiles.Singleton;

import java.sql.Connection;
import java.sql.ResultSet;

public class ObjetivoDeAhorro extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText txfNuevoObjetivo;
    private Button btnNuevoObjetivoCancel, btnNuevoObjetivoAceptar, btn_cambiar_objetivo_back;
    private TextView lblValorObjetivo, lblValorProgresoObjetivo;
    private Singleton user = Singleton.getInstance();
    private DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*CONTEMPLAR LA CARGA DESDE LA BASE DE DATOS*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivo_de_ahorro);
        float ObjetivoOnDB = get_ObjetivoAhorro_DB();
        float PorcentajeAvanceOnDB = get_PorcentajeProgreso_DB();
        lblValorObjetivo = findViewById(R.id.lblValorObjetivo);
        lblValorObjetivo.setText("₡" + df.format(ObjetivoOnDB).toString());
        lblValorProgresoObjetivo  = findViewById(R.id.lblValorProgresoObjetivo);
        lblValorProgresoObjetivo.setText(df.format(PorcentajeAvanceOnDB).toString() + "%");



        final Button btnCambiarObjetivoAhorro = findViewById(R.id.btnCambiarObjetivoAhorro);
        btnCambiarObjetivoAhorro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNuevoObjetivoDialog();
            }
        });
        final Button btnVerDetallesAhorro = findViewById(R.id.btnVerDetallesAhorro);
        btnVerDetallesAhorro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), DetallesProgreso.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
            }
        });
    }
    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void createNuevoObjetivoDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View CambiarObjetivo = getLayoutInflater().inflate(R.layout.objetivo_de_ahorro_popup, null);
        final TextView label = findViewById(R.id.lblValorObjetivo);
        txfNuevoObjetivo = (EditText) CambiarObjetivo.findViewById(R.id.txfNuevoObjetivo);

        btnNuevoObjetivoAceptar = (Button) CambiarObjetivo.findViewById(R.id.btnNuevoObjetivoAceptar);
        btnNuevoObjetivoCancel = (Button) CambiarObjetivo.findViewById(R.id.btnNuevoObjetivoCancel);
        btn_cambiar_objetivo_back = (Button) CambiarObjetivo.findViewById(R.id.btn_cambiar_objetivo_back);

        dialogBuilder.setView(CambiarObjetivo);
        dialog = dialogBuilder.create();
        dialog.show();

        btnNuevoObjetivoAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txfNuevoObjetivo.getText().toString().equals("")){
                    int value = Integer.parseInt(txfNuevoObjetivo.getText().toString());
                    if(value > 0){
                        //DB
                        set_objetivo_DB(value);
                        float PorcentajeAvanceOnDB = get_PorcentajeProgreso_DB();
                        lblValorProgresoObjetivo  = findViewById(R.id.lblValorProgresoObjetivo);
                        lblValorProgresoObjetivo.setText(df.format(PorcentajeAvanceOnDB).toString() + "%");
                        //GUI
                        String toLabel = "₡" + value;
                        label.setText(toLabel);
                        dialog.dismiss();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Ingrese un valor mayor a 0", Toast.LENGTH_LONG);
                        toast.show();
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                    }
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Ingrese un valor", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                }

            }
        });

        btnNuevoObjetivoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_cambiar_objetivo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private float get_ObjetivoAhorro_DB(){
        float ObjetivoAhorro = 0;
        int id = user.ID;
        String SPSQL = "exec usp_GetObjetivoAhorroById " + String.valueOf(id);
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                //show error
                Toast toast = Toast.makeText(getApplicationContext(), "Error: Conexion nula", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
            }else{
                ResultSet usp = con.createStatement().executeQuery(SPSQL);
                while(usp.next()){
                    ObjetivoAhorro = usp.getFloat("ObjetivoAhorro");
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
        return ObjetivoAhorro;
    }

    private void set_objetivo_DB(float newObjetivo){
        int id = user.ID;
        String SPSQL = "exec usp_SetObjetivoAhorroById " + String.valueOf(id) + ", " + String.valueOf(newObjetivo) ;
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

    private float get_PorcentajeProgreso_DB(){
        float PorcentajeAhorro = 0;
        int id = user.ID;
        String SPSQL = "exec usp_GetPorcentajeProgresoAhorroById " + String.valueOf(id);
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
                    PorcentajeAhorro = usp.getFloat("Porcentaje");
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
        return PorcentajeAhorro;
    }
}