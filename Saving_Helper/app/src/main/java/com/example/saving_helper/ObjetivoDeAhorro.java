package com.example.saving_helper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ObjetivoDeAhorro extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText txfNuevoObjetivo;
    private Button btnNuevoObjetivoCancel, btnNuevoObjetivoAceptar, btn_cambiar_objetivo_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*CONTEMPLAR LA CARGA DESDE LA BASE DE DATOS*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivo_de_ahorro);

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
                /*SE VA A LA OTRA VENTANA*/
            }
        });
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
}