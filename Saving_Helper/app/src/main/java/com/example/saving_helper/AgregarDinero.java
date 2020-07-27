package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AgregarDinero extends AppCompatActivity {
    private Button btnAceptarAgregarDinero, btnCancelarAgregarDinero, btn_agregar_dinero_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dinero);
        btnAceptarAgregarDinero = findViewById(R.id.btnAceptarAgregarDinero);
        btnAceptarAgregarDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarDinero();
            }
        });
        btnCancelarAgregarDinero = findViewById(R.id.btnCancelarAgregarDinero);
        btnCancelarAgregarDinero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SE DEVUELVE A LA PANTALLA PRINCIPAL*/
            }
        });
        btn_agregar_dinero_back = findViewById(R.id.btn_agregar_dinero_back);
        btn_agregar_dinero_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*SE DEVUELVE AL MENU*/
            }
        });
    }

    public void agregarDinero(){
        final EditText txfDineroAAgregar = findViewById(R.id.txfDineroAAgregar);
        final TextView lblSaldo = findViewById(R.id.lblSaldo);
        /*OPTENER EL VALOR DE LA BASE DE DATOS*/
        float consulta = 0; /*LO DE LA BASE*/
        float value = Float.parseFloat(txfDineroAAgregar.getText().toString()) + consulta;
        String toLabel = "₡" + String.valueOf(value);
        lblSaldo.setText(toLabel);
    }
}