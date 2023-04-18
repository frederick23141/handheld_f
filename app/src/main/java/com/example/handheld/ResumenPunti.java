package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.handheld.atv.holder.adapters.listCajasRecepAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.CajasReceModelo;


import java.util.ArrayList;
import java.util.List;

public class ResumenPunti extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Button btnCargarCajas;

    //Se declaran los elementos necesarios para el list view
    ListView listviewResumenPunti;
    List<CajasReceModelo> ListaCajasRecep= new ArrayList<>();
    ListAdapter listCajasRecepAdapter;

    //Se declara un objeto conexion
    Conexion conexion;

    //Se declaran las variables para recibir los datos de la anterior clase;
    String nit_persona;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_punti);

        btnCargarCajas = findViewById(R.id.btnCargarCajas);

        listviewResumenPunti = findViewById(R.id.listviewResumenPunti);
        listviewResumenPunti.setOnItemClickListener(ResumenPunti.this); //Determinamos a que elemento va dirigido el OnItemClick

        nit_persona = getIntent().getStringExtra("nit_usuario");

        btnCargarCajas.setOnClickListener(v -> {
            String sql;
            conexion = new Conexion();
            sql = "select REFERENCIA, count(REFERENCIA) as cantidad from F_Recepcion_puntilleria where RECEPCIONADO is null group by REFERENCIA order by REFERENCIA";
            ListaCajasRecep = conexion.obtenerCajasRecepcionar(ResumenPunti.this, sql);
            for(int i =0; i<ListaCajasRecep.size();i++){
                String cod = ListaCajasRecep.get(i).getReferencia();
                String sqlDes = "select descripcion from referencias where codigo ='"+ cod +"'";
                String refe = conexion.obtenerDescripcionCodigo(ResumenPunti.this,sqlDes);
                ListaCajasRecep.get(i).setDescripcion(refe);
            }
            listCajasRecepAdapter = new listCajasRecepAdapter(ResumenPunti.this,R.layout.item_row_respunti,ListaCajasRecep);
            listviewResumenPunti.setAdapter(listCajasRecepAdapter);
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ResumenPunti.this, MesasRefePunti.class);

        String sql = "select MESA from F_Recepcion_puntilleria where REFERENCIA = '"+ ListaCajasRecep.get(position).getReferencia() +"' and RECEPCIONADO is null group by MESA";
        intent.putExtra("sql",sql);
        intent.putExtra("referencia",ListaCajasRecep.get(position).getReferencia());
        intent.putExtra("descripcion",ListaCajasRecep.get(position).getDescripcion());
        intent.putExtra("nit_usuario",nit_persona);
        startActivity(intent);

    }
}