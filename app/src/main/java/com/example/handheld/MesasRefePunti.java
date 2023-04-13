package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.handheld.atv.holder.adapters.listMesasRecepAdapter;
import com.example.handheld.atv.holder.adapters.listpedidoAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.CajasReceModelo;
import com.example.handheld.modelos.MesasModelo;

import java.util.ArrayList;
import java.util.List;

public class MesasRefePunti extends AppCompatActivity implements AdapterView.OnItemClickListener {

    TextView txtTrefCaja;

    //Se declaran los elementos necesarios para el list view
    ListView listviewMesas;
    List<MesasModelo> ListaMesasRecep= new ArrayList<>();
    ListAdapter MesasAdapter;

    //Se declara un objeto conexion
    Conexion conexion;

    String sql;
    String fecha;
    String turno;
    String referencia;

    String sql_refe;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesas_refe_punti);

        listviewMesas = findViewById(R.id.listviewMesas);
        listviewMesas.setOnItemClickListener(MesasRefePunti.this); //Determinamos a que elemento va dirigido el OnItemClick

        //Recibimos el documento desde la class Main Activity
        sql = getIntent().getStringExtra("sql");
        fecha = getIntent().getStringExtra("fecha");
        turno = getIntent().getStringExtra("turno");
        referencia = getIntent().getStringExtra("referencia");

        txtTrefCaja = findViewById(R.id.txtTrefCaja);
        txtTrefCaja.setText(referencia);

        consultarMesas();
    }

    //METODO CONSULTAR PEDIDOS
    public void consultarMesas(){
        conexion = new Conexion();

        ListaMesasRecep = conexion.obtenerMesas(getApplication(),sql);
        MesasAdapter = new listMesasRecepAdapter(MesasRefePunti.this,R.layout.item_row_mesas_respunti,ListaMesasRecep);
        listviewMesas.setAdapter(MesasAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String mesa = ListaMesasRecep.get(position).getMesa();
        if (turno.equals("Turno 1")){
            sql_refe = "select * from F_Recepcion_puntilleria where FECHA >='"+ fecha +" 06:00:00' and FECHA <= '"+ fecha +" 14:00:00' and MESA = '"+ mesa +"' and REFERENCIA='"+ referencia +"'";
        }else{
            sql_refe = "select * from F_Recepcion_puntilleria where FECHA >='"+ fecha +" 14:00:00' and FECHA <= '"+ fecha +" 22:00:00' and MESA = '"+ mesa +"' and REFERENCIA='"+ referencia +"'";
        }
        Intent intent = new Intent(MesasRefePunti.this,RecepcionEmpaque.class);
        intent.putExtra("sql",sql_refe);
        intent.putExtra("fecha",fecha);
        intent.putExtra("turno",turno);
        intent.putExtra("referencia",referencia);
        startActivity(intent);


    }
}