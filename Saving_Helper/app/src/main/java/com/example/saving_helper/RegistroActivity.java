package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher; // Para usar
import java.util.regex.Pattern; // expresiones regulares
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        correoField = findViewById(R.id.correo);
        contraField = findViewById(R.id.contraseñaInicial);
    }
    public void retrocederInicio(View v){
        finish();
    }

    public void resgistrarUsuario (View v){
        String textoCorreo = correoField.getText().toString();
        String textoContra = contraField.getText().toString();
        if (!validarExpresionRegular(textoCorreo, "^[^@]+@[^@]+\\.[a-zA-Z]{3,50}$")){ //email
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
                Toast toast = Toast.makeText(getApplicationContext(), "ahora vamos a login", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
            }
        }
    }

    public boolean validarExpresionRegular(String palabra, String expresionRegular ){
        Pattern p = Pattern.compile(expresionRegular);
        Matcher matcher = p.matcher(palabra);
        return matcher.matches();
    }
    public String pruebaC (View v){
        String z = "";
        String passWord = "";
        Boolean success= true;
        try{
            AzureConnection conexion= new AzureConnection();
            Connection con = conexion.connectionClass();
            if (con == null){
                z = "Se cae";
            }else{
                String query = "select * from Estudiante";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if(rs.next()){
                    passWord = rs.getString("Contraseña");
                    z= "IT WORKS BRO";
                    con.close();
                }else{
                    z = "Invalid query";
                    success= false;
                }
            }
        } catch (Exception ex) {
            success= false;
            z=ex.getMessage();
        }
        System.out.println(z);
        System.out.println(passWord);
        return passWord;
    }

}