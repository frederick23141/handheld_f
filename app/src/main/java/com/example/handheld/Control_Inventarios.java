package com.example.handheld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.ClasesOperativas.objOperacionesDb;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.BodegasModelo;

import java.sql.SQLException;
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

    //Definimos las variables para capturar la bodega y la descripcion
    String bodega, descripcion;
    boolean respuesta;
    Integer id_inventario =0;

    //Definimos el listado que contendra las bodegas
    public  List<BodegasModelo> listarBodegas = new ArrayList<>(); //Esta consulta las bodegas en la base de datos
    public List<String> listadoBodegas = new ArrayList<>(); //Esta lista llena el Spinner que llama a las bodegas consultadas



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

        //Se programa el boton de inicio de inventario
        //btnIniciarInventario.setOnClickListener(this::IniciarInventario);
        btnIniciarInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    IniciarInventario();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        //Se inician las bodegas llamando el metodo cargarBodegas una vez se inicializa y cargan los datos iniciales del activity
        listadoBodegas.add("Seleccionar Bodega");
        cargarBodegas();




        //Metodo que valida el elemento seleccionado en el Spinner
        spinnerInventarioBodegas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toastAlert("Se selecciono el elemento: " + position+" El parent es: "+parent.getItemAtPosition(position));
                bodega= String.valueOf(parent.getItemAtPosition(position));

                if (bodega.equals("Seleccionar Bodega"))
                {
                    toastError("Para iniciar el muestreo debe seleccionar uno de los consecutivos validos");
                    spinnerInventarioBodegas.requestFocus();
                }
                else{
                   toastAcierto("Seleccionada una bogeda valida");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



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
            //listadoBodegas.add(bodegas.getBodega()+" "+bodegas.getDescripcion());
            listadoBodegas.add(bodegas.getBodega());
        }

        CargarDatosSpinner(listadoBodegas);
    }

    private void CargarDatosSpinner(List<String> datosSpinner) {

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter(Control_Inventarios.this, android.R.layout.simple_spinner_item,datosSpinner);
        spinnerInventarioBodegas.setAdapter(adapterSpinner);
    }

    private void IniciarInventario() throws SQLException {
        /*Intent intent = new Intent(Control_Inventarios.this, Control_Inventario_Registro_Rollos.class);
        startActivity(intent);*/

          if (crear_inv())
          {
              Intent intent = new Intent(Control_Inventarios.this, Control_Inventario_Registro_Rollos.class);
              startActivity(intent);
          }


    }
    
    
    public boolean crear_inv() throws SQLException {
        boolean resp;
        mostrarAlertDialog();
        if (respuesta)
        {
            String sql_inventario="SELECT (CASE WHEN max(id) IS NULL THEN 1 ELSE max(id)+1 END)AS id FROM JB_control_inventario";
            id_inventario= Integer.valueOf(conexion.obtenerIdInventario(Control_Inventarios.this,sql_inventario));
            toastAlert("El id de inventario obtenido es : " + id_inventario);
            String sql_control_inventario="INSERT INTO JB_control_inventario (id,bodega,operario,fec_ini) VALUES(" + id_inventario +"," + bodega + "," + nit_usuario + ",GETDATE())";
            toastAlert("Se agregaran los siguientes datos en el insert, el id seria: " + id_inventario+ "la bodega seria : " + bodega + "y el usuario es: "+nit_usuario);
            if(objOperacionesDb.ejecutarInsertJjprgproduccion(sql_control_inventario,Control_Inventarios.this ) >  0) {
                switch (bodega) {
                    case "11":
                        congelar_inv(11, "SAGA", "G");
                        break;
                    case "12":
                        congelar_inv(12, "SCLA", "P");
                        break;
                    case "13":
                        congelar_inv(13, "SREC", "R");
                        break;
                    case "14":
                        congelar_inv(14, "SPU", "A");
                        break;
                    case "1":
                        congelar_inv(1, "smpp_b2", "A1");
                        break;
                    case "2":
                        congelar_inv(2, "smpp_b2", "A2");
                        break;
                    case "7":
                        congelar_inv(7, "", "INVB2");
                        break;
                    default:
                        toastAlert("Debe seleccionar una bodega");
                        break;
                }

            }
            resp=true;
        }
        else {
            toastError("Ud apachurro que no queria continuar");
            resp=false;
        }

        return resp;

    }


    private void mostrarAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Título del diálogo");
        builder.setMessage("Mensaje del diálogo");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuesta=true;
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuesta=false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void congelar_inv(int bodega,String trans, String dest )
    {

        toastAcierto("los datos recibidos son bodega : "+ bodega + " numero de transaccion: "+trans +" detalle:" + dest);

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