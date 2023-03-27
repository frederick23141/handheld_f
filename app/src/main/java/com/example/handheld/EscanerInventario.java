package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.handheld.atv.holder.adapters.listGalvTerminadoAdapter;
import com.example.handheld.atv.holder.adapters.listpedidoAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.GalvRecepcionModelo;
import com.example.handheld.modelos.PedidoModelo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EscanerInventario extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //se declaran las variables donde estaran los datos que vienen de la anterior clase
    String nit_usuario,fecha_inicio,fecha_final;

    //Se declaran los elementos necesarios para el list view
    ListView listviewGalvTerminado;
    List<GalvRecepcionModelo> ListaGalvTerminado= new ArrayList<>();
    ListAdapter GalvTerminadoAdapter;
    GalvRecepcionModelo galvRecepcionModelo;

    //Se declara un objeto conexion
    Conexion conexion;

    objOperacionesDb objOperacionesDb = new objOperacionesDb();

    Button btnTransaInve;

    Integer id = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_inventario);

        //Recibimos los datos desde la class PedidoInventraio
        nit_usuario = getIntent().getStringExtra("nit_usuario");
        fecha_inicio = getIntent().getStringExtra("fecha_inicio");
        fecha_final = getIntent().getStringExtra("fecha_final");

        //Definimos los elementos necesarios para el list view
        listviewGalvTerminado = findViewById(R.id.listviewGalvTerminado);
        listviewGalvTerminado.setOnItemClickListener(this); //Determinamos a que elemento va dirigido el OnItemClick
        galvRecepcionModelo = new GalvRecepcionModelo();

        //Llamamos al metodo para consultar los rollos de galvanizados listos para recoger
        consultarGalvTerminado();


        btnTransaInve = findViewById(R.id.btnTransaInve);

        btnTransaInve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id>0){
                    try {
                        terminar_inventario();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void consultarGalvTerminado() {
        conexion = new Conexion();

        ListaGalvTerminado = conexion.obtenerGalvTerminado(getApplication(),fecha_inicio,fecha_final);
        GalvTerminadoAdapter = new listGalvTerminadoAdapter(EscanerInventario.this,R.layout.item_row_galvterminado,ListaGalvTerminado);
        listviewGalvTerminado.setAdapter(GalvTerminadoAdapter);
    }

    private void terminar_inventario() throws SQLException {
        String sql = "UPDATE J_inventario_enc SET fecha_terminado = GETDATE() WHERE id =" + id;
        if (objOperacionesDb.ejecutarUpdateDbProduccion(sql,EscanerInventario.this)>0){
            Toast.makeText(this, "Holaaa", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}