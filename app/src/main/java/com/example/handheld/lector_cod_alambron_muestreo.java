package com.example.handheld;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handheld.ClasesOperativas.Gestion_alambronLn;
import com.example.handheld.ClasesOperativas.objOperacionesDb;
import com.example.handheld.conexionDB.Conexion;

import java.sql.SQLException;
import java.util.ArrayList;

public class lector_cod_alambron_muestreo extends AppCompatActivity {

    //Se declara un objeto conexion
    Conexion conexion;

    //Se declaran los objetos de otras clases necesarias
    objOperacionesDb objOperacionesDb = new objOperacionesDb();
    Gestion_alambronLn obj_gestion_alambronLn = new Gestion_alambronLn();


    //Se declaran los objetos obtenidos desde el Activity
    Button btnmuestreo;

    EditText eTorsionAlambron,eTraccionAlambron,eRecaladoAlambron,eCalidadAlambron;
    Spinner SpinConsecutivos;

    String consecutivo,nit_proveedor,num_importacion,id_detalle,numero_rollo;

    ArrayList<String> listaConsecutivos =  new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_cod_alambron_muestreo);



        //Definimos los elemetos del layout en la clase
        eTorsionAlambron =findViewById(R.id.eTorsionAlambron);
        eTraccionAlambron = findViewById(R.id.eTraccionAlambron);
        eRecaladoAlambron = findViewById(R.id.eRecaladoAlambron);
        eCalidadAlambron=findViewById(R.id.eCalidadAlambron);
        btnmuestreo=findViewById(R.id.btnmuestreo);
        SpinConsecutivos= findViewById(R.id.SpinConsecutivos);

        //Se inicializa la variable de conexion
        conexion = new Conexion();

        //Definimos la variables necesarias recibiendo los datos enviados por la anterior clase

        Intent intent = getIntent();
        listaConsecutivos = intent.getStringArrayListExtra("listaConsecutivos");

        //Se llama el metodo que rellena el Spinner con el ArrayList anterior
        transaccionMuestreo( listaConsecutivos);


        btnmuestreo.setOnClickListener(v -> {
            try {
                terminar_muestra();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


        //Metodo que valida el elemento seleccionado en el Spinner
        SpinConsecutivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toastAlert("Se selecciono el elemento: " + position+" El parent es: "+parent.getItemAtPosition(position));
                consecutivo= String.valueOf(parent.getItemAtPosition(position));

                if (consecutivo.equals("Seleccionar"))
                {
                    toastError("Para iniciar el muestreo debe seleccionar uno de los consecutivos validos");
                    eTorsionAlambron.setEnabled(false);
                    eTraccionAlambron.setEnabled(false);
                    eRecaladoAlambron.setEnabled(false);
                    eCalidadAlambron.setEnabled(false);
                    SpinConsecutivos.requestFocus();
                }
                else{
                    nit_proveedor = obj_gestion_alambronLn.extraerDatoCodigoBarras("proveedor", consecutivo);
                    num_importacion = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_importacion", consecutivo);
                    id_detalle = obj_gestion_alambronLn.extraerDatoCodigoBarras("detalle", consecutivo);
                    numero_rollo = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_rollo", consecutivo);

                    eTorsionAlambron.setEnabled(true);
                    eTraccionAlambron.setEnabled(true);
                    eRecaladoAlambron.setEnabled(true);
                    eCalidadAlambron.setEnabled(true);
                    eTorsionAlambron.requestFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //Metodo que carga los consecutivos agregados correctamente en el ListView del Activity lector_cod_alambron_cargue
    private void transaccionMuestreo(ArrayList<String> datosListview) {

        /*for (String consecutivo : datosListview) {
            System.out.println(consecutivo);
            Log.d("TAG", consecutivo);
            toastAlert(consecutivo);
        }*/
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter(lector_cod_alambron_muestreo.this, android.R.layout.simple_spinner_item,datosListview);
        SpinConsecutivos.setAdapter(adapterSpinner);
    }

    //Metodo que valida que todos los datos se llenen correctamente
    private boolean validar_muestra(String torsion,String traccion,String recalado,String calidad){

        boolean resp=false;
        String mensaje = "";

        if (torsion.equals("")) {
            toastError("Debe ingresar un dato de torsion");
            eTorsionAlambron.requestFocus();
        } else if (traccion.equals(""))
        {
            toastError("Debe ingresarse un dato de traccion");
            eTraccionAlambron.requestFocus();
        } else if (recalado.equals(""))
        {
            toastError("Debe ingresar un dato de recalcado");
            eRecaladoAlambron.requestFocus();
        } else if (calidad.equals("")) {
            toastError("Debe ingresarse un dato de calidad");
            eCalidadAlambron.requestFocus();
        }
        else {
            resp = true;
        }
        if (resp) {
            mensaje="Todos los campos se llenaron correctamente";
            toastAcierto(mensaje);
        }
        if (mensaje.equals("")) {
            toastError("¡Todos los campos deben ser llenados correctamente para continuar el proceso!");
        }
        return resp;
    }

    //Metodo para realizar el insert en la tabla J_alambron_muestra
    private void terminar_muestra() throws SQLException {

        String torsion = eTorsionAlambron.getText().toString();
        String traccion = eTraccionAlambron.getText().toString();
        String recalado = eRecaladoAlambron.getText().toString();
        String calidad = eCalidadAlambron.getText().toString();

        if (validar_muestra(torsion,traccion,recalado,calidad)){
            String sql_muestra="INSERT INTO J_alambron_muestra (num_importacion, id_solicitud_det, numero_rollo, torsion, traccion, recalado, calidad_final,nit_proveedor) " +
                    "VALUES (" + num_importacion + ", " + id_detalle + ",  " + numero_rollo + ", " + torsion + ",  " + traccion + ",  " + recalado +",  " + calidad + "," + nit_proveedor +")";
            if (objOperacionesDb.ejecutarInsertJjprgproduccion(sql_muestra,lector_cod_alambron_muestreo.this) >0){
                toastAcierto("El muestreo se realizo correctamente");
            }
            else
            {
                toastError("Ha ocurrido un error al momento de registrar el muestreo");
            }

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_error_mimap).
                setTitle("¿Continuar?").
                setMessage("¿Desea continuar con el proceso de muestreo?").
                setPositiveButton("Aceptar", (dialogInterface, i) -> {nuevomuestreo();
                }).
                setNegativeButton("Salir", (dialogInterface, i) -> {this.finish();
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //Metodo para iniciar un nuevo muestreo
    public void nuevomuestreo(){
        eCalidadAlambron.setText("");
        eRecaladoAlambron.setText("");
        eTorsionAlambron.setText("");
        eTraccionAlambron.setText("");
        SpinConsecutivos.requestFocus();
        SpinConsecutivos.setSelection(0);
    }






    ///////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////METODOS PARA LOS MENSAJES PERSONALIZADOS DE ACIERTO Y ERROR ///////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////


    //METODO DE TOAST PERSONALIZADO PARA ERROR
    public void toastError(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_per_no_encon, findViewById(R.id.ll_custom_toast_per_no_encon));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    //METODO DE TOAST PERSONALIZADO PARA ACIERTOS
    public void toastAcierto(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_acierto, findViewById(R.id.ll_custom_toast_acierto));
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtMens = view.findViewById(R.id.txtMensa);
        txtMens.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }


    //METODO DE TOAST PERSONALIZADO PARA ALERTAR
    public void toastAlert(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_per_no_encon, findViewById(R.id.ll_custom_toast_per_no_encon));
        view.setBackgroundColor(Color.YELLOW);
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);
        txtMensaje.setTextColor(Color.BLACK); // Cambiar el color del texto a negro

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}