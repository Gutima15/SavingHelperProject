package com.example.saving_helper;
//Conectar a BD Azure
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AzureConnection {

    public Connection connectionClass(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = "";
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://saving-helper.database.windows.net:1433;DatabaseName=Saving_Helper_DB;user=desarrollo@saving-helper;password=SavingHelper2020;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se){
            Log.e("error here 1: ", se.getMessage());
        }catch (ClassNotFoundException e){
            Log.e("error here 1: ", e.getMessage());
        }catch(Exception ex){
            Log.e("error here 1: ", ex.getMessage());
        }
        return connection;
    }
}
