package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.adapters.listpedidoAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.PedidoModelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listviewPedido;
    private List<PedidoModelo> ListaPedidos = new ArrayList<>();
    ListAdapter PedidoAdapter;
    PedidoModelo pedidoModelo;
    Conexion conexion;
    Button btnSalir;
    Button btnActualizar;
    TextView movimiento;
    String nit_usuario;
    String bod_origen;
    String bod_destino;
    String modelo;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        listviewPedido = findViewById(R.id.listviewPedido);
        btnSalir = findViewById(R.id.btnSalir);
        btnActualizar = findViewById(R.id.btnActualizar);
        movimiento = findViewById(R.id.movimiento);
        listviewPedido.setOnItemClickListener(this); //Determinamos a que elemento va dirigido el OnItemClick

        nit_usuario = getIntent().getStringExtra("nit_usuario");
        bod_origen = getIntent().getStringExtra("bod_origen");
        bod_destino = getIntent().getStringExtra("bod_destino");
        modelo = getIntent().getStringExtra("modelo");

        movimiento.setText("Movimiento: Bodega " + bod_origen + " - " + bod_destino);

        pedidoModelo = new PedidoModelo();
        consultarPedidos();


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarPedidos();
                toastActualizado("Actualizado");
            }
        });
    }

    //METODO CONSULTAR PEDIDOS
    public void consultarPedidos(){
        conexion = new Conexion();

        ListaPedidos = conexion.obtenerPedidos(getApplication());
        PedidoAdapter = new listpedidoAdapter(Pedido.this,R.layout.item_row,ListaPedidos);
        listviewPedido.setAdapter(PedidoAdapter);
    }


    //METODO PARA CERRAR LA APLICACION
    @SuppressLint("")
    public void salir(View view){
        finishAffinity();;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //Toast.makeText(this, "Elemento clicado: " + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,Escaner.class);
        //Enviamos al siguiente Activity los datos del Listview Seleccionado
        intent.putExtra("numero", ListaPedidos.get(position).getNumero());
        intent.putExtra("fecha",ListaPedidos.get(position).getFecha());
        intent.putExtra("codigo",ListaPedidos.get(position).getCodigo());
        intent.putExtra("pendiente",ListaPedidos.get(position).getPendiente());
        intent.putExtra("descripcion",ListaPedidos.get(position).getDescripcion());
        //Enviamos al siguiente activity los datos traidos desde el primer activity
        intent.putExtra("nit_usuario",nit_usuario);
        intent.putExtra("bod_origen",bod_origen);
        intent.putExtra("bod_destino",bod_destino);
        intent.putExtra("modelo",modelo);
        startActivity(intent);
    }

    //METODO DE TOAST PERSONALIZADO : ACTUALIZADO
    public void toastActualizado(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_actualizado, (ViewGroup) findViewById(R.id.ll_custom_toast_actualizado));
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView txtMensaje = view.findViewById(R.id.txtMsgToast);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    }
