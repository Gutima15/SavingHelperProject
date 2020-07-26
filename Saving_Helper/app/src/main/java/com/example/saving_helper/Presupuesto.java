package com.example.saving_helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Presupuesto extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText txfNuevoPresupuesto;
    private Button btnNuevoPresupuestoCancel, btnNuevoPresupuestoAceptar, btn_cancelar_cambio_periodo, btn_aceptar_cambio_periodo, btn_cambiar_periodo_presupuesto_back, btn_cambiar_presupuesto_back;
    private RadioButton rbtn_semanal, rbtn_quincenal, rbtn_mensual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);
        this.setTitle("Presupuesto");
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
                //aqui va el codigo del aceptar
                //valida
                //luego envia a la base de datos
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
        dialogBuilder = new AlertDialog.Builder(this);
        final View CambiarPeriodo = getLayoutInflater().inflate(R.layout.presupuesto_cambiar_periodo, null);

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
                //aqui va el codigo del aceptar
                //valida
                //luego envia a la base de datos
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
}