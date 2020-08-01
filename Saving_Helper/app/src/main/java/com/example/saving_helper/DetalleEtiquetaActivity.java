package com.example.saving_helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saving_helper.Objetos.Etiqueta;
import com.example.saving_helper.Objetos.Gasto;
import com.example.saving_helper.Utiles.DetallesListAdapter;
import com.example.saving_helper.Utiles.GastoListAdapter;
import com.example.saving_helper.Utiles.Singleton;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleEtiquetaActivity extends AppCompatActivity {
    private Etiqueta etiqueta;
    private TextView nombreEtiqueta;
    private Button btn_AgregarGasto;
    private RelativeLayout relativeLayout_ListaGastos;
    private ListView listView_ListaGastos;
    private Singleton user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_etiqueta);

        Intent intent = getIntent();
        etiqueta = intent.getParcelableExtra("Etiqueta");

        nombreEtiqueta = findViewById(R.id.textView_DetalleEtiqueta_NombreEtiqueta);
        btn_AgregarGasto = findViewById(R.id.btn_DetalleEtiqueta_AgregarGasto);
        relativeLayout_ListaGastos = findViewById(R.id.relativeLayout_DetalleEtiqueta_ListaGsto);
        listView_ListaGastos = findViewById(R.id.listView_DetalleEtiqueta_Gastos);
        user = Singleton.getInstance();

        nombreEtiqueta.setText(etiqueta.getNombre());


        if(etiqueta.getGastos().size() != 0){
            Gasto gasto = new Gasto();
            etiqueta.getGastos().add(0,gasto);

            DetallesListAdapter adapter = new DetallesListAdapter(getApplicationContext(), R.layout.layout_lista_gastos,etiqueta.getGastos());
            listView_ListaGastos.setAdapter(adapter);
        }
        else {
            relativeLayout_ListaGastos.setAlpha(0);
        }




    }


    public void onClickBack(View view){
        finish();
    }

    public void onClickEliminar(View view){
        String SPSQL = "{? = call usp_EtiquetaDelete (?,?)}";
        Boolean success = false;
        try{
            AzureConnection connexion= new AzureConnection();
            Connection con = connexion.connectionClass();
            if (con == null){
                success = false;
            }else{
                CallableStatement ps = con.prepareCall(SPSQL);
                ps.setEscapeProcessing(true);
                ps.setQueryTimeout(1000);
                ps.registerOutParameter(1, java.sql.Types.INTEGER);
                ps.setInt(2,user.ID);
                ps.setString(3,etiqueta.getNombre());
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
            success =false;
            Log.d("Chompipe", ex.toString());
        }

        if(success){
            Toast.makeText(getApplicationContext(),"Etiqueta eliminida",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, EtiquetaActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Se ha detectado un error al eliminar la etiqueta",Toast.LENGTH_LONG).show();
        }


    }

    public void OnClickEliminarGasto(int posicionGasto){

    }

}