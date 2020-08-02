package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher; // Para usar
import java.util.regex.Pattern; // expresiones regulares

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class RegistroActivity extends AppCompatActivity {

    EditText correoField;
    EditText contraField;
    EditText nombreField;
    EditText apellidosField;
    EditText userNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        correoField = findViewById(R.id.correo);
        contraField = findViewById(R.id.contraseñaInicial);
        nombreField = findViewById(R.id.nombrePila);
        apellidosField = findViewById(R.id.apellidos);
        userNameField = findViewById(R.id.userName);
    }
    public void retrocederInicio(View v){
        finish();
    }

    public void resgistrarUsuario (View v){
        String textoCorreo = correoField.getText().toString();
        String textoContra = contraField.getText().toString();
        if (!validarExpresionRegular(textoCorreo, "^[^@]+@[^@]+\\.[a-zA-Z]{1,50}$")){ //email
            Toast toast = Toast.makeText(getApplicationContext(), "Ingrese un formato válido de correo. Ej: usuario@savingHelper", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
        } else{
            if (!validarExpresionRegular(textoContra, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,30}$")){ //password
                Toast toast = Toast.makeText(getApplicationContext(), "Ingrese una contraseña con al menos una mayúscula, una minúscula, un número y un símbolo entre 8 y 30 caracteres", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 280);
            } else{
                //Cambio de pantalla
                crearUsuario(textoCorreo,textoContra);
                Intent intent = new Intent(this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    public boolean validarExpresionRegular(String palabra, String expresionRegular ){
        Pattern p = Pattern.compile(expresionRegular);
        Matcher matcher = p.matcher(palabra);
        return matcher.matches();
    }

    public void crearUsuario(String textCorreo, String textContra){
        String textoNombreP =  nombreField.getText().toString();
        String textoApellidos = apellidosField.getText().toString();
        String textoUserName = userNameField.getText().toString();
        String z = "";
        try{
            AzureConnection connectionA= new AzureConnection();
            Connection con = connectionA.connectionClass();
            if (con == null){
                z = "Se cae";
            }else{
                String SPSQL = "{? = call usp_EstudianteCreate(?,?,?,?,?)}";
                CallableStatement  ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.registerOutParameter(1, java.sql.Types.INTEGER);
                ps.setString(2,textoNombreP); //nombre
                ps.setString(3,textoApellidos); //apellidos
                ps.setString(4,textCorreo); //correo
                ps.setString(5,textoUserName); //Nickname
                ps.setString(6,textContra); //contra
                ps.execute();
                int result = ps.getInt(1);
                if (result==0){
                    Log.d("InsFunc","Si funca");
                }else{
                    Log.d("InsNoF","No funca");
                }
                con.close();
            }
        } catch (Exception ex) {
            z=ex.getMessage();
        }
        System.out.println(z);
    }

}