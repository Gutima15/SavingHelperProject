package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }
    public void openRegistroActivity(View v){
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    public void openPantallaInicio(View v){
        EditText usuario = findViewById(R.id.nombreUsuario);
        EditText contra = findViewById(R.id.contraseña);
        if (validarIngreso(usuario, contra)){
            Singleton singleUser = Singleton.getInstance();
            singleUser.setDatos(usuario.getText().toString(),contra.getText().toString());
            singleUser.setId();
            Intent intent = new Intent(this, PantallaInicio.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Contraseña o nombre de usuario inválidos", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
        }
    }
    public boolean validarIngreso(EditText user, EditText password){
        Boolean success= false;
        String SPSQL = "{? = call usp_EstudianteRead (?,?)}";
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                success = false;
            }else{
                CallableStatement  ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(30);
                ps.registerOutParameter(1, java.sql.Types.INTEGER);
                ps.setString(2,user.getText().toString());
                ps.setString(3,password.getText().toString());
                //
                ps.execute(); // ->1 / 0
                int resultado= ps.getInt(1);
                //
                if (resultado==0){
                    success = true;
                }else{
                    success=  false;
                }
                con.close();
            }
        } catch (Exception ex) {
            success = false;
        }
        return success;
    }

}