package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.handheld.ClasesOperativas.Gestion_alambronLn;
import com.example.handheld.ClasesOperativas.ObjTraslado_bodLn;
import com.example.handheld.ClasesOperativas.objOperacionesDb;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.BodegasModelo;

import java.util.ArrayList;
import java.util.List;

public class Control_Inventarios extends AppCompatActivity {

    //Se define el elemento de tipo conexion
    Conexion conexion;

    //Se declaran los objetos de otras clases necesarias

     objOperacionesDb objOperacionesDb = new objOperacionesDb();

    //Se declaran las variables que obtienen los datos desde el activity
    Spinner spinnerInventarioBodegas;
    Button btnIniciarInventario,btnSalida;

    //Definimos las variables de clase
    String nit_usuario;

    //Definimos el listado que contendra las bodegas
    public  List<BodegasModelo> listarBodegas = new ArrayList<>();
    public List<String> listadoBodegas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_inventarios);

        //Inicializamos la variable conexion para su uso en todos los metodos y funciones en que sea necesario su uso
        conexion = new Conexion();

        //Definimos los elemetos del layout en la clase
        btnSalida = findViewById(R.id.btnSalida);
        btnIniciarInventario = findViewById(R.id.btnIniciarInventario);
        spinnerInventarioBodegas = findViewById(R.id.spinnerInventarioBodegas);

        //Definimos la variables necesarias recibiendo los datos enviados por la anterior clase

        nit_usuario = getIntent().getStringExtra("nit_usuario");

        //Se programa el boton de salida de la apicación
        btnSalida.setOnClickListener(this::salir);

        //Se programa el boton de salida de la apicación
        btnIniciarInventario.setOnClickListener(this::IniciarInventario);


        //Se inician las bodegas
        listadoBodegas.add("Seleccionar Bodega");
        cargarBodegas();



    }


    //Se programa el boton para salir de la aplicacion
    @SuppressLint("")
    public void salir(View view){
        finishAffinity();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////// SE GENERAN LOS METODOS Y FUNCIONES QUE SE USARAN PARA INICIAR INVENTARIO///////////
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void cargarBodegas(){
        String sql_bodegas="SELECT CAST(bodega As varchar(25)) As bodega, CAST(bodega As varchar(25)) + '-' + descripcion As descripcion FROM bodegas  WHERE bodega IN (1,2,11,12,13,14,17)";
        listarBodegas = conexion.listarBodegas( Control_Inventarios.this,sql_bodegas);

        for (BodegasModelo bodegas : listarBodegas) {
            System.out.println(bodegas);
            listadoBodegas.add(bodegas.getBodega()+" "+bodegas.getDescripcion());
        }

        CargarDatosSpinner(listadoBodegas);
    }

    private void CargarDatosSpinner(List<String> datosSpinner) {

        /*for (String consecutivo : datosListview) {
            System.out.println(consecutivo);
            Log.d("TAG", consecutivo);
            toastAlert(consecutivo);
        }*/
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter(Control_Inventarios.this, android.R.layout.simple_spinner_item,datosSpinner);
        spinnerInventarioBodegas.setAdapter(adapterSpinner);
    }

    private void IniciarInventario(View view) {
        Intent intent = new Intent(Control_Inventarios.this, Control_Inventario_Registro_Rollos.class);
        startActivity(intent);
    }


}