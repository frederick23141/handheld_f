package com.example.handheld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.atv.holder.adapters.listGalvTerminadoAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.GalvRecepcionModelo;
import com.example.handheld.modelos.GalvRecepcionadoRollosModelo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecepcionEmpaque extends AppCompatActivity {

    TextView txtTRefCaja;

    String referencia;
    //se declaran las variables de los elementos del Layout
    EditText codigoGalva;
    TextView txtTotal, txtTotalSinLeer, txtRollosLeidos;
    Button btnTransaGalv, btnCancelarTrans;

    //se declaran las variables donde estaran los datos que vienen de la anterior clase
    String nit_usuario,fecha_inicio,fecha_final;


    //Se declara un objeto conexion
    Conexion conexion;

    //Se inicializa variables necesarias en la clase
    int yaentre = 0;
    String consecutivo;
    Integer numero_transaccion;
    ObjTraslado_bodLn objTraslado_bodLn = new ObjTraslado_bodLn();
    //objOperacionesDb objOperacionesDb = new objOperacionesDb();
    Ing_prod_ad ing_prod_ad = new Ing_prod_ad();
    List<GalvRecepcionadoRollosModelo> ListarefeRecepcionados= new ArrayList<>();

    //Se inicializa los varibles para el sonido de error
    SoundPool sp;
    int sonido_de_Reproduccion;

    //Se inicializa una instancia para hacer vibrar el celular
    Vibrator vibrator;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion_empaque);

        referencia = getIntent().getStringExtra("referencia");
        txtTRefCaja = findViewById(R.id.txtTRefCaja);
        txtTRefCaja.setText(referencia);


        /*
        //Definimos los elementos del Layout
        codigoGalva = findViewById(R.id.codigoGalva);
        txtTotal = findViewById(R.id.txtTotal);
        txtTotalSinLeer = findViewById(R.id.txtTotalSinLeer);
        txtRollosLeidos = findViewById(R.id.txtRollosLeidos);
        btnTransaGalv = findViewById(R.id.btnTransaGalv);
        btnCancelarTrans = findViewById(R.id.btnCancelarTrans);

        //Recibimos los datos desde la class PedidoInventraio
        nit_usuario = getIntent().getStringExtra("nit_usuario");
        fecha_inicio = getIntent().getStringExtra("fecha_inicio");
        fecha_final = getIntent().getStringExtra("fecha_final");


        //Se Define los varibles para el sonido de error
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC,1);
        sonido_de_Reproduccion = sp.load(this, R.raw.sonido_error_2,1);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /////////////////////////////////////////////////////////////////////////////////////////////
        //Llamamos al metodo para consultar los rollos de galvanizados listos para recoger
        consultarGalvTerminado();

        /////////////////////////////////////////////////////////////////////////////////////////////
        //Se establece el foco en el edit text
        codigoGalva.requestFocus();

        /////////////////////////////////////////////////////////////////////////////////////////////
        //Se programa para que al presionar enter en el edit text haga el proceso
        codigoGalva.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if(yaentre == 0){
                    if(codigoGalva.getText().toString().equals("")){
                        toastError("Por favor escribir o escanear el codigo de barras");
                    }else{
                        closeTecladoMovil();
                        try {
                            codigoIngresado();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    cargarNuevo();
                }
                return true;
            }
            return false;
        });

        btnCancelarTrans.setOnClickListener(this::salir);

        btnTransaGalv.setOnClickListener(v -> {
            int sleer = Integer.parseInt(txtTotalSinLeer.getText().toString());
            int total = Integer.parseInt(txtTotal.getText().toString());
            int leidos = (total - sleer);
            if(sleer==0 && total>0){
                try {
                    realizarTransaccion();
                }catch (Exception e){
                    toastError(e.getMessage());
                }
            }else{
                if(sleer==0 && total==0){
                    toastError("No hay rollos por leer");
                    AudioError();
                }else{
                    if (total == sleer){
                        toastError("No se ha leido ningun rollo");
                        AudioError();
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(EscanerInventario.this);
                        builder.setIcon(R.mipmap.ic_alert).
                                setTitle("Atención").
                                setMessage("Se han leido: "+ leidos +" Rollos \n" +
                                        "Se iniciara el translado solo con los rollos leidos!").
                                setPositiveButton("Aceptar", (dialog, which) -> {
                                    try {
                                        realizarTransaccion();
                                    }catch (Exception e){
                                        toastError(e.getMessage());
                                    }
                                }).setNegativeButton("Cancelar", (dialog, which) -> {

                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            }
        });

         */
    }

    /*
    private void realizarTransaccion() {
        //Creamos una lista para almacenar todas las consultas que se realizaran en la base de datos
        List<Object> listTransactionGal = new ArrayList<>();
        //Creamos una lista para almacenar todas las consultas que se realizaran en la base de datos
        List<Object> listTransaccionBodega;

        // Obtén la fecha y hora actual
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();

        // Define el formato de la fecha y hora que deseas obtener
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoMonth = new SimpleDateFormat("MM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatoYear = new SimpleDateFormat("yyyy");

        // Convierte la fecha actual en un String con el formato definido
        String fechaActualString = formatoFecha.format(fechaActual);
        String monthActualString = formatoMonth.format(fechaActual);
        String yearActualString = formatoYear.format(fechaActual);

        //se adicionan los campos recepcionado, nit_recepcionado y fecha_recepcionado a la tabla
        for(int i=0;i<ListaGalvTerminado.size();i++){
            if(ListaGalvTerminado.get(i).getColor().equals("GREEN")){
                String nro_orden = ListaGalvTerminado.get(i).getNro_orden();
                String nro_rollo = ListaGalvTerminado.get(i).getNro_rollo();

                String sql_rollo= "UPDATE D_rollo_galvanizado_f SET recepcionado='SI', nit_recepcionado='"+ nit_usuario +"', fecha_recepcion='"+ fechaActualString +"' WHERE nro_orden='"+ nro_orden +"' AND consecutivo_rollo='"+nro_rollo+"'";

                try {
                    //Se añade el sql a la lista
                    listTransactionGal.add(sql_rollo);
                }catch (Exception e){
                    Toast.makeText(EscanerInventario.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (listTransactionGal.size()>0){
            if (ing_prod_ad.ExecuteSqlTransaction(listTransactionGal, "JJVPRGPRODUCCION", EscanerInventario.this)){
                ListarefeRecepcionados = conexion.galvRefeRecepcionados(EscanerInventario.this,fechaActualString, monthActualString, yearActualString);
                numero_transaccion = Integer.valueOf(Obj_ordenprodLn.mover_consecutivo("TRB1", EscanerInventario.this));
                listTransaccionBodega = traslado_bodega(ListarefeRecepcionados, calendar);
                if (ing_prod_ad.ExecuteSqlTransaction(listTransaccionBodega, "JJVDMSCIERREAGOSTO", EscanerInventario.this)){
                    toastAcierto("Transaccion Realizada con Exito! --" + numero_transaccion);
                    consultarGalvTerminado();
                }else{
                    toastError("Problemas, No se realizó correctamente la transacción!");
                }
            }else{
                toastError("Error al realizar la transacción!");
            }
        }
    }

    private List<Object> traslado_bodega(List<GalvRecepcionadoRollosModelo> ListarefeRecepcionados, Calendar calendar){
        List<Object> listSql;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = dateFormat.format(calendar.getTime());
        String usuario = nit_usuario;
        String notas = "MOVIL fecha:" + fecha + " usuario:" + usuario;

        listSql = objTraslado_bodLn.listaTrasladoBodegaGalv(ListarefeRecepcionados,numero_transaccion, 2, 3, calendar, notas, usuario, "TRB1", "11",EscanerInventario.this);
        return listSql;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private void consultarGalvTerminado() {
        conexion = new Conexion();

        ListaGalvTerminado = conexion.obtenerGalvTerminado(getApplication(),fecha_inicio,fecha_final);
        GalvTerminadoAdapter = new listGalvTerminadoAdapter(EscanerInventario.this,R.layout.item_row_galvterminado,ListaGalvTerminado);
        listviewGalvTerminado.setAdapter(GalvTerminadoAdapter);

        String totalRollos = String.valueOf(ListaGalvTerminado.size());
        txtTotal.setText(totalRollos);

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //Metodo para ocultar el teclado virtual
    private void closeTecladoMovil() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    private void codigoIngresado() throws SQLException {
        consecutivo = codigoGalva.getText().toString().trim();
        boolean encontrado = false;
        int position = 0;
        for (int i=0;i<ListaGalvTerminado.size();i++){
            String codigoList = ListaGalvTerminado.get(i).getNro_orden()+"-"+ListaGalvTerminado.get(i).getNro_rollo();
            if(consecutivo.equals(codigoList)){
                encontrado = true;
                position = i;
            }
        }
        if (encontrado){
            if(ListaGalvTerminado.get(position).getColor().equals("GREEN")){
                toastError("Rollo ya leido");
                AudioError();
                cargarNuevo();
            }else{
                pintarRollo(position);
                //recepcionarBD(position);
                contarSinLeer();
                contarLeidos();
                toastAcierto("Rollo encontrado");
                cargarNuevo();
            }
        }else{
            toastError("Rollo no encontrado");
            AudioError();
            cargarNuevo();
        }
    }

    //Se realiza para realizar transaccion rollo a rollo, pero despues se cambia de idea
    /*
    private void recepcionarBD(int p) throws SQLException {
        // Obtén la fecha y hora actual
        Date fechaActual = new Date();

        // Define el formato de la fecha y hora que deseas obtener
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Convierte la fecha actual en un String con el formato definido
        String fechaActualString = formatoFecha.format(fechaActual);

        String nro_orden = ListaGalvTerminado.get(p).getNro_orden();
        String nro_rollo = ListaGalvTerminado.get(p).getNro_rollo();


        String sql= "UPDATE D_rollo_galvanizado_f SET recepcionado='SI', nit_recepcionado='"+ nit_usuario +"', fecha_recepcion='"+ fechaActualString +"' WHERE nro_orden='"+ nro_orden +"' AND consecutivo_rollo='"+nro_rollo+"'";
        objOperacionesDb.ejecutarUpdateDbProduccion(sql,EscanerInventario.this);

    }
     */


    private void cargarNuevo() {
        codigoGalva.setText("");
        if (yaentre == 0){
            yaentre = 1;
        }else{
            yaentre = 0;
            codigoGalva.requestFocus();
        }
    }


    //METODO PARA CERRAR LA APLICACION
    @SuppressLint("")
    public void salir(View view){
        finishAffinity();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //METODO DE TOAST PERSONALIZADO : ERROR
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

    //METODO DE TOAST PERSONALIZADO : ACIERTO
    public void toastAcierto(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_acierto, findViewById(R.id.ll_custom_toast_acierto));
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtMens = view.findViewById(R.id.txtMensa);
        txtMens.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void AudioError(){
        sp.play(sonido_de_Reproduccion,100,100,1,0,0);
        vibrator.vibrate(2000);
    }
}