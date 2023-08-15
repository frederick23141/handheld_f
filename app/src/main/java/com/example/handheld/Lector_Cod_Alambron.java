package com.example.handheld;


//Importaciones propias de android o terceros

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.LectorCodCargueModelo;
import com.example.handheld.ClasesOperativas.objOperacionesDb;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Lector_Cod_Alambron extends AppCompatActivity {

    //se declaran las variables de los elementos del Layout
    EditText ePlaca,ePesoCarga,ePesoDescargado,eCantRollos;
    TextView txtDate;
    Button btnContinuar, btnCancelar,btnSalida;

    //Se declara un objeto conexion
    Conexion conexion;

    //Definimos las variables de clase
    String nit_usuario;
    String id_Inirequisicion;


    //Difinimos las variables propias de la clase

    Boolean cargaComp = false;
    double numero_transaccion;

    //Se declaran los objetos de otras clases necesarias
    objOperacionesDb objOperacionesDb = new objOperacionesDb();
    //Ing_prod_ad ing_prod_ad = new Ing_prod_ad();
    //Gestion_alambronLn obj_gestion_alambronLn = new Gestion_alambronLn();
    //ObjTraslado_bodLn objTraslado_bodLn = new ObjTraslado_bodLn();

    //Se declara el ArrayList que captura las requisiciones abiertas y nunca finalizadas
        private  ArrayList<LectorCodCargueModelo> requisicionesPendientes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_cod_alambron);


        //Definimos los elemetos del layout en la clase
        btnSalida = findViewById(R.id.btnSalida);
        btnContinuar = findViewById(R.id.btnContinuar);
        btnCancelar = findViewById(R.id.btnCancelar);
        ePlaca = findViewById(R.id.ePlaca);
        ePesoCarga = findViewById(R.id.ePesoCarga);
        ePesoDescargado = findViewById(R.id.ePesoDescargado);
        eCantRollos = findViewById(R.id.eCantRollos);

        //Inicializamos la variable conexion para su uso en todos los metodos y funciones en que sea necesario su uso
        conexion = new Conexion();


        //Definimos la variables necesarias recibiendo los datos enviados por la anterior clase

        nit_usuario = getIntent().getStringExtra("nit_usuario");

        //Se establece el foco en el edit text
        ePlaca.requestFocus();


        // Programamos el campo de fecha sobre el TextView del diseño
        txtDate = findViewById(R.id.TxtDate);
        txtDate.setKeyListener(null); // Bloquear la edición del campo

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateTime = dateFormat.format(calendar.getTime());

        txtDate.setText(dateTime);

       /* //inhabilitamos el ingreso de Carga, Descarga y Cantidad sin antes haber ingresado la placa
        ePesoCarga.setEnabled(false);
        ePesoDescargado.setEnabled(false);
        eCantRollos.setEnabled(false);
        btnCancelar.setEnabled(false);
        // btnContinuar.setEnabled(false);*/



        //Se programa el boton de salida de la apicación
        btnSalida.setOnClickListener(this::salir);


        //Se programa el Boton Cancelar el cual llama al metodo Reiniciar
        btnCancelar.setOnClickListener(v -> reiniciar());



        /*//Llamamos el metodo que verificara las transacciones pendientes
        verificarTransaccionesPendientes(nit_usuario);*/


        //se programa el boton Continuar
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validarYCrearRequisicion();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

       ValidarRequisicionesIniciadas();
    }


    //Metodo para Cancelar
    public void reiniciar(){
        ePlaca.setText("");
        ePesoCarga.setText("");
        ePesoDescargado.setText("");
        eCantRollos.setText("");
    }

    public void continua(){

        Intent intent = new Intent(this, lector_cod_alambron_cargue.class);
        intent.putExtra("id_Alambronrequision",id_Inirequisicion);
        intent.putExtra("eCantRollos",eCantRollos.getText().toString());
        intent.putExtra("nit_usuario",nit_usuario.toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Lector_Cod_Alambron.this.finish();
        startActivity(intent);

    }

    //Metodo para Validacion inicial de requisicion
    private boolean validar_requisicion() {
        String mensaje = "";
        boolean resp = false;
        nit_usuario = getIntent().getStringExtra("nit_usuario");
        if (nit_usuario.equals("")) {
            mensaje = "Ingrese una cédula correcta";
            toastError(mensaje);
        } else if (ePlaca.getText().toString().equals("")) {
            mensaje = "Ingrese la placa del camión";
            toastError(mensaje);
            ePlaca.requestFocus();
        } else if (ePesoCarga.getText().toString().equals("")) {
            mensaje = "Ingrese  el peso de la mula cargada";
            toastError(mensaje);
            ePesoCarga.requestFocus();
        } else if (ePesoDescargado.getText().toString().equals("")) {
            mensaje = "Ingrese el peso descargado de la mula";
            toastError(mensaje);
            ePesoDescargado.requestFocus();
        } else if (eCantRollos.getText().toString().equals("")) {
            mensaje = "Ingrese la cantidad de rollos que trae el carro";
            toastError(mensaje);
            eCantRollos.requestFocus();
        } else {
            resp = true;
        }
        if (resp) {
            if (Double.parseDouble(ePesoCarga.getText().toString()) <= Double.parseDouble(ePesoDescargado.getText().toString())) {
                mensaje = "El peso de la mula cargada debe ser mayor al peso descargado!";
                toastError(mensaje);
                resp = false;
            }
        }
        if (!mensaje.equals("")) {
            toastError("!Todos los campos deben ser llenados correctamente para continuar el proceso¡");
        }
        return resp;
    }


    //Se crea el metodo para crear e iniciar la Requisicion

    private boolean iniciarRequisicion() throws SQLException {
        conexion = new Conexion();
        String sql_id= "SELECT (CASE WHEN MAX(id) IS NULL THEN 1 ELSE MAX(id)+1 END) as id FROM J_alambron_requisicion";
        id_Inirequisicion = conexion.obtenerIdAlamRequesicion(Lector_Cod_Alambron.this,sql_id);
        toastError("El id obtenido es:"+id_Inirequisicion);
        boolean resp = false;
        String placa = ePlaca.getText().toString();
        String cargado = ePesoCarga.getText().toString();
        String descargado = ePesoDescargado.getText().toString();
        String num_rollos = eCantRollos.getText().toString();
        String sql = "INSERT INTO J_alambron_requisicion (id,fecha_inicial,nit,placa,peso_cargado,peso_descargado,num_rollos) " +
                "VALUES(" + id_Inirequisicion + ",GETDATE()," + nit_usuario + ",'" + placa + "'," + cargado + "," + descargado + "," + num_rollos + ") ";
        if (objOperacionesDb.ejecutarInsertJjprgproduccion(sql, Lector_Cod_Alambron.this) > 0) {
            resp = true;
        }
        return resp;
    }




    //Se crea el Metodo para hacer el llamado de los metodos de validarrequisicion e inciarrequisicion
    private void validarYCrearRequisicion() throws SQLException {
        if (validar_requisicion()) {
            if (iniciarRequisicion()) {
                toastAcierto("Requisición creada con éxito");
                continua();//Si pasa correctamente se abre la siguiente ventana
                btnContinuar.setEnabled(false);
            } else {
                toastError("Error al crear la requisición");
            }
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////// SE CREAN LOS METODOS PARA VALIDAR LAS REQUISICIONES QUE NO SE CERRARON/////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void ValidarRequisicionesIniciadas(){
        verificarTransaccionesPendientes(nit_usuario);
        if(requisicionesPendientes.isEmpty()){
            toastAcierto("Se iniciara un nuevo proceso de Descargue de Alambron");
            eCantRollos.setText("");
        }
        else{
            toastAlert("Existe un descargue de Alambron incompleto, se cargaran los datos para terminar el proceso");
            continua();
        }
    }



    public List<LectorCodCargueModelo> verificarTransaccionesPendientes(String nit) {
        String sql="SELECT CAST (e.nit_proveedor  AS varchar(25) ) + '-' + CAST (e.numero_importacion AS varchar(25) ) \n" +
                "+'-'+ CAST (d.id_det AS varchar(25) )+'-'+ CAST (r.numero_rollo  AS varchar(25) ) As consecutivo,\n" +
                "e.nit_proveedor ,e.numero_importacion,e.fecha,d.codigo,r.numero_rollo ,r.peso,d.costo_kilo,d.id_det ,\n" +
                "r.numero_rollo , a.id As id_requisicion, a.placa , a.peso_cargado,a.peso_descargado,a.num_rollos  \n" +
                "FROM J_alambron_importacion_det_rollos r , J_alambron_solicitud_det d , J_alambron_solicitud_enc e ,J_alambron_requisicion a\n" +
                "WHERE r.num_transaccion is null AND r.nit_proveedor <> 999999999 AND   d.nit_proveedor = e.nit_proveedor AND\n" +
                "r.nit_proveedor = d.nit_proveedor AND  d.num_importacion = e.numero_importacion  AND\n" +
                "r.num_importacion = d.num_importacion  AND r.id_solicitud_det  = d.id_det \n" +
                "AND a.id = r.id_requisicion \n" +
                "AND a.nit = '" + nit + "'";

        /*String sql= "SELECT CAST (e.nit_proveedor  AS varchar(25) ) + '-' + CAST (e.numero_importacion AS varchar(25) ) \n" +
                "+'-'+ CAST (d.id_det AS varchar(25) )+'-'+ CAST (r.numero_rollo  AS varchar(25) ) As consecutivo,\n" +
                "e.nit_proveedor ,e.numero_importacion,e.fecha,d.codigo,r.numero_rollo ,r.peso,d.costo_kilo,d.id_det ,\n" +
                "r.numero_rollo , a.id As id_requisicion, a.placa , a.peso_cargado,a.peso_descargado,a.num_rollos  \n" +
                "FROM J_alambron_importacion_det_rollos r , J_alambron_solicitud_det d , J_alambron_solicitud_enc e ,J_alambron_requisicion a\n" +
                "WHERE r.num_transaccion is null AND r.nit_proveedor <> 999999999 AND   d.nit_proveedor = e.nit_proveedor AND\n" +
                "r.nit_proveedor = d.nit_proveedor AND  d.num_importacion = e.numero_importacion  AND\n" +
                "r.num_importacion = d.num_importacion  AND r.id_solicitud_det  = d.id_det \n" +
                "AND a.id =' " + 3485 + "' \n " + "AND r.id_requisicion =' " + 3485 + "' \n" +
                "AND a.nit = '" + nit + "'";*/

        requisicionesPendientes=conexion.lista_pendientes_requisicion(Lector_Cod_Alambron.this,sql);


        for (LectorCodCargueModelo consecutivo : requisicionesPendientes) {
            System.out.println(consecutivo);//imprimimos todos los datos de la lista
            id_Inirequisicion= consecutivo.getConsecutivo(); //Asignamos en la posicion el id de requisicion existente
            System.out.println("El numero de requisicion es: "+id_Inirequisicion);
            eCantRollos.setText(consecutivo.getNumero_rollos_descargar());
        }

        return requisicionesPendientes;
    }






    //Se programa el boton para salir de la aplicacion
    @SuppressLint("")
    public void salir(View view){
        finishAffinity();
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



}