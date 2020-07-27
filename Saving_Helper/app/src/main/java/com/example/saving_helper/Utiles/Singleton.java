package com.example.saving_helper.Utiles;
import android.util.Log;

import com.example.saving_helper.AzureConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Singleton {
    // static variable single_instance of type Singleton
    private static Singleton single_instance = null;

    // variable of type String
    public String usuario;
    public String password;
    public int ID;

    // private constructor restricted to this class itself
    private Singleton()
    {
    }

    // static method to create instance of Singleton class
    public static Singleton getInstance()
    {
        if (single_instance == null)
            single_instance = new Singleton();

        return single_instance;
    }
    public void setDatos(String usuario, String password){
        this.usuario = usuario;
        this.password = password;
    }

    public void setId(){
        AzureConnection connexion= new AzureConnection();
        Connection con = connexion.connectionClass();
        String query = "select ID from Estudiante where Nickname = '"+usuario+ "'" ;
        Statement stmt = null;
        int Id=0;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();

            Id = rs.getInt("ID");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.ID = Id;
    }
}
