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
import android.widget.TextView;

import com.example.handheld.atv.holder.adapters.listMesasRecepAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.MesasModelo;

import java.util.ArrayList;
import java.util.List;

public class MesasRefePunti extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView txtTrefCaja, txtDesRefe;
    Button btnVolver;

    //Se declaran los elementos necesarios para el list view
    ListView listviewMesas;
    List<MesasModelo> ListaMesasRecep= new ArrayList<>();
    ListAdapter MesasAdapter;

    //Se declara un objeto conexion
    Conexion conexion;

    String sql, referencia, descripcion, nit_persona;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas_refe_punti);

        listviewMesas = findViewById(R.id.listviewMesas);
        listviewMesas.setOnItemClickListener(MesasRefePunti.this); //Determinamos a que elemento va dirigido el OnItemClick

        //Recibimos el documento desde la class Main Activity
        sql = getIntent().getStringExtra("sql");
        referencia = getIntent().getStringExtra("referencia");
        descripcion = getIntent().getStringExtra("descripcion");
        nit_persona = getIntent().getStringExtra("nit_usuario");

        txtTrefCaja = findViewById(R.id.txtTrefCaja);
        txtTrefCaja.setText(referencia);
        txtDesRefe = findViewById(R.id.txtDesRefe);
        txtDesRefe.setText(descripcion);
        btnVolver = findViewById(R.id.btnVolver);

        consultarMesas();

        btnVolver.setOnClickListener(v -> finish());

    }

    //METODO CONSULTAR PRODUCCIÓN DE UNA REFERENCIA EN LAS MESAS
    public void consultarMesas(){
        conexion = new Conexion();

        ListaMesasRecep = conexion.obtenerMesas(getApplication(),sql);
        MesasAdapter = new listMesasRecepAdapter(MesasRefePunti.this,R.layout.item_row_mesas_respunti,ListaMesasRecep);
        listviewMesas.setAdapter(MesasAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String mesa = ListaMesasRecep.get(position).getMesa();
        Intent intent = new Intent(MesasRefePunti.this,RecepcionEmpaque.class);
        intent.putExtra("referencia",referencia);
        intent.putExtra("mesa",mesa);
        intent.putExtra("descripcion",descripcion);
        intent.putExtra("nit_usuario",nit_persona);
        startActivity(intent);


    }
}