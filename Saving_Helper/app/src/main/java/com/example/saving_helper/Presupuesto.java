package com.example.saving_helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Presupuesto extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText txfNuevoPresupuesto;
    private Button btnNuevoPresupuestoCancel, btnNuevoPresupuestoAceptar, btn_cancelar_cambio_periodo, btn_aceptar_cambio_periodo, btn_cambiar_periodo_presupuesto_back, btn_cambiar_presupuesto_back;
    private RadioButton rbtn_semanal, rbtn_quincenal, rbtn_mensual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*CONTEMPLAR LA CARGA DESDE LA BASE DE DATOS*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);

        final Button btnCambiarPresupuesto = findViewById(R.id.btnCambiarPresupuesto);
        btnCambiarPresupuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNuevoPresupuestoDialog();
            }
        });
        final Button btnCambiarPeriodo = findViewById(R.id.btnCambiarTipoDePeriodo);
        btnCambiarPeriodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPresupuestoCambiarPeriodoDialog();
            }
        });
    }

    public void createNuevoPresupuestoDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View CambiarPresupuesto = getLayoutInflater().inflate(R.layout.presupuesto_popup, null);
        final TextView label = findViewById(R.id.lblPresupuesto);
        txfNuevoPresupuesto = (EditText) CambiarPresupuesto.findViewById(R.id.txfNuevoPresupuesto);

        btnNuevoPresupuestoAceptar = (Button) CambiarPresupuesto.findViewById(R.id.nuevoPresupuestoAceptar);
        btnNuevoPresupuestoCancel = (Button) CambiarPresupuesto.findViewById(R.id.nuevoPresupuestoCancel);
        btn_cambiar_presupuesto_back = (Button) CambiarPresupuesto.findViewById(R.id.btn_cambiar_presupuesto_back);

        dialogBuilder.setView(CambiarPresupuesto);
        dialog = dialogBuilder.create();
        dialog.show();

        btnNuevoPresupuestoAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = Integer.parseInt(txfNuevoPresupuesto.getText().toString());
                if(value > 0){
                    //envia el valor a la base de datos
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
        });

        btnNuevoPresupuestoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_cambiar_presupuesto_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void createPresupuestoCambiarPeriodoDialog(){

        /*CONTEMPLAR CARGA DESDE DB CUAL ES EL TIPO*/

        dialogBuilder = new AlertDialog.Builder(this);
        final View CambiarPeriodo = getLayoutInflater().inflate(R.layout.presupuesto_cambiar_periodo, null);
        final TextView lblTipoPeriodo = findViewById(R.id.lblTipoPeriodo);

        btn_aceptar_cambio_periodo = (Button) CambiarPeriodo.findViewById(R.id.btn_aceptar_cambio_periodo);
        btn_cancelar_cambio_periodo = (Button) CambiarPeriodo.findViewById(R.id.btn_cancelar_cambio_periodo);
        btn_cambiar_periodo_presupuesto_back = (Button) CambiarPeriodo.findViewById(R.id.btn_cambiar_periodo_presupuesto_back);

        rbtn_semanal = (RadioButton) CambiarPeriodo.findViewById(R.id.rbtn_semanal);
        rbtn_quincenal = (RadioButton) CambiarPeriodo.findViewById(R.id.rbtn_quincenal);
        rbtn_mensual = (RadioButton) CambiarPeriodo.findViewById(R.id.rbtn_mensual);

        dialogBuilder.setView(CambiarPeriodo);
        dialog = dialogBuilder.create();
        dialog.show();

        btn_aceptar_cambio_periodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rbtn_semanal.isChecked()){
                    lblTipoPeriodo.setText(rbtn_semanal.getText());
                }
                else if(rbtn_quincenal.isChecked()){
                    lblTipoPeriodo.setText(rbtn_quincenal.getText());
                }
                else if(rbtn_mensual.isChecked()){
                    lblTipoPeriodo.setText(rbtn_mensual.getText());
                }
                /*ENVIAR A LA BASE DE DATOS*/
                dialog.dismiss();
            }
        });

        btn_cancelar_cambio_periodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_cambiar_periodo_presupuesto_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}