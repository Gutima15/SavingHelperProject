package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModificarInfoUsuarioActivity extends AppCompatActivity {
    //(Listo :D)De entrada ocupo cargar los datos de la BD en las label, para ello ocupo crear los edit Text a los cual les voy a asignar esos valores
    //Luego ocupo validar que la información ingresada tenga el formato correcto, al menos en correo y contraseña. PD; no cargo datos a las label de contraseña sino que lo manejo para comparación
    //Finalmente si los cambios son válidos y no hay otro usuario con ese nombre registrado (requiero consultar si el username ya existe) actualiza los datos

    Singleton user = Singleton.getInstance();
    String nombreP;
    String apellidos;
    String email;
    String nickname;
    String passwordActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_info_usuario);
        cargarDatosUsuario(user.usuario, user.password);
        //cargo nombre de pila
        EditText nombrePila = findViewById(R.id.nombrePilaMod);
        nombrePila.setText(nombreP);
        //cargo los apellidos
        EditText apellidosField = findViewById(R.id.apellidosMod);
        apellidosField.setText(apellidos);
        //cargo el correo
        EditText correoField = findViewById(R.id.CorreoMod2);
        correoField.setText(email);
        //cargo el nickname
        EditText nick = findViewById(R.id.userNameMod);
        nick.setText(nickname);
        Log.d("Datos",nombreP + " " +apellidos +" " + email+" " + nickname + " " +passwordActual);

    }
    public void activity_menu_con_iconos(View v){
        Intent intent = new Intent(this, MenuConIconos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void activity_pantalla_inicio(View v){
        Intent intent = new Intent(this, PantallaInicio.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //usp_EstudianteRead
    public void cargarDatosUsuario(String userName, String password){
        String z = "";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            AzureConnection connectionA= new AzureConnection();
            Connection con = connectionA.connectionClass();
            if (con == null){
                z = "Se cae";
            }else{
                String SPSQL = "{call usp_EstudianteReadInfo(?,?)}";
                //CallableStatement ps = con.prepareCall(SPSQL);
                ps = con.prepareStatement(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.setString(1,userName);
                ps.setString(2,password);
                rs = ps.executeQuery();
                while (rs.next()) {
                    nickname = rs.getString("userNick");
                    passwordActual = rs.getString("password");
                    nombreP = rs.getString("nombrePila");
                    apellidos = rs.getString("ape");
                    email= rs.getString("correoE");
                    break;
                }
                con.close();
            }
        } catch (Exception ex) {
            z=ex.getMessage();
        }
        System.out.println(z);
    }

    public void actualizarDatosUsuario(View v){
        EditText nombrePila = findViewById(R.id.nombrePilaMod);
        String nombreNuevo = nombrePila.getText().toString();

        EditText apellidosField = findViewById(R.id.apellidosMod);
        String apellidosNuevos = apellidosField.getText().toString();

        EditText correoField = findViewById(R.id.CorreoMod2);
        String correoNuevo= correoField.getText().toString();

        EditText nick = findViewById(R.id.userNameMod);
        String nicknameNuevo= nick.getText().toString();
        /*  --------------------------------------------------------------*/
        EditText nuevoPassword = findViewById(R.id.ContraseñaNueva);
        String nuevoPasswordStr = nuevoPassword.getText().toString();

        EditText validatePassword = findViewById(R.id.contraseñaVerificacion);
        String validateStr = validatePassword.getText().toString();
        /*  --------------------------------------------------------------*/
        boolean flag= true;
        if (hayBlancos(nombreNuevo,apellidosNuevos,correoNuevo,nicknameNuevo)){
            Toast toast = Toast.makeText(getApplicationContext(), "No pueden haber espacios en blanco en esta sección", Toast.LENGTH_LONG);
            toast.show();
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
            flag=false;
        }else{
            if (!validarExpresionRegular(correoNuevo, "^[^@]+@[^@]+\\.[a-zA-Z]{1,50}$")){ //email
                Toast toast = Toast.makeText(getApplicationContext(), "Ingrese un formato válido de correo. Ej: usuario@savingHelper", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                flag=false;
            }else{
                if (user.ID != nicknameID(nicknameNuevo)){
                    Toast toast = Toast.makeText(getApplicationContext(), "Nombre de usuario no disponible.", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                    flag=false;
                }else{
                    actualizarPrimeraParte(nombreNuevo,apellidosNuevos,correoNuevo,nicknameNuevo);
                    user.usuario=nicknameNuevo;
                    Toast toast = Toast.makeText(getApplicationContext(), "Cambio realizado", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                }

            }
        }
        if (noHayBlancosPassword(nuevoPasswordStr,validateStr)){
            if (!validarExpresionRegular(nuevoPasswordStr, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+])[A-Za-z\\d!@#$%^&*()_+]{8,30}$")){ //password
                Toast toast = Toast.makeText(getApplicationContext(), "Ingrese una contraseña con al menos una mayúscula, una minúscula, un número y un símbolo entre 8 y 30 caracteres", Toast.LENGTH_LONG);
                toast.show();
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 280);
                flag=false;
            }else{
                if (validateStr.equals(nuevoPasswordStr)){
                    actualizarSegundaParte(nuevoPasswordStr);
                    user.password=nuevoPasswordStr;
                    Toast toast = Toast.makeText(getApplicationContext(), "Cambio realizado", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 300);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 280);
                    flag=false;
                }
            }
        }
        if (flag){
            Intent intent = new Intent(this, PantallaInicio.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void actualizarPrimeraParte(String nombre ,String apellidos ,String correo ,String nickname) {
        String SPSQL = "{call usp_EstudianteUpdate (?,?,?,?,?)}";
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                Log.d("Error","no conectó");
            }else{
                CallableStatement ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.setInt(1,user.ID);
                ps.setString(2,nombre);
                ps.setString(3, apellidos);
                ps.setString(4,correo);
                ps.setString(5,nickname);
                ps.execute();
                con.close();
            }
        } catch (Exception ex) {
            Log.d("Error","Se cae en primera parte, error: " + ex.toString());
        }
    }

    public  void actualizarSegundaParte(String password){
        String SPSQL = "{call usp_EstudianteUpdatePassword (?,?)}";
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                Log.d("Error", "Se cae en la conexión");
            }else{
                CallableStatement ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.setInt(1,user.ID);
                ps.setString(2,password);
                ps.execute();
                con.close();
            }
        } catch (Exception ex) {
            Log.d("Error", "Se cae en la segunda parte, error: "+ex.toString());
        }
    }

    public int nicknameID(String nick) {
        int result = -1;
        try{
            AzureConnection connectionA= new AzureConnection();
            Connection con = connectionA.connectionClass();
            if (con == null){
               Log.d("Error","No conecta...");
            }else{
                String SPSQL = "{? = call usp_EstudianteReadById(?)}";
                CallableStatement  ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.registerOutParameter(1, java.sql.Types.INTEGER);
                ps.setString(2,nick); //nombre
                ps.execute();
                result = ps.getInt(1);
                if (result==0){
                    result=  ps.getInt("Id");
                }
                con.close();
            }
        } catch (Exception ex) {
            Log.d("Error","Error: "+ ex.toString());
        }
        return result;

    }

    public boolean hayBlancos(String campo1, String campo2, String campo3, String campo4){
        if (campo1.equals("") || campo2.equals("") || campo3.equals("") || campo4.equals("")){
            return true;
        }else{
            return false;
        }
    }

    public boolean noHayBlancosPassword(String campo1, String campo2){
        if (!campo1.equals("") || !campo2.equals("")){
            return true;
        }else{
            return false;
        }
    }

    public boolean validarExpresionRegular(String palabra, String expresionRegular ){
        Pattern p = Pattern.compile(expresionRegular);
        Matcher matcher = p.matcher(palabra);
        return matcher.matches();
    }

}