package com.example.handheld;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.conexionDB.Conexion;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IngreProTerPunti extends AppCompatActivity {

    //se declaran las variables de los elementos del Layout
    EditText codigoCaja, eCedula, eMesa, eCodigo;
    TextView txtCreferencia;
    Button btnCancelarCaja, btnIngreTermiPunti, btnSalir;

    //se declaran las variables donde estaran los datos que vienen de la anterior clase
    String nit_usuario, nombre_usuario;

    //Se declara un objeto conexion
    Conexion conexion;

    //Se inicializa variables necesarias en la clase
    int yaentre = 0;
    String codCaja, descripcion, conversion;
    objOperacionesDb objOperacionesDb = new objOperacionesDb();

    //Se inicializa los varibles para el sonido de error
    SoundPool sp;
    int sonido_de_Reproduccion;

    //Se inicializa una instancia para hacer vibrar el celular
    Vibrator vibrator;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingre_pro_ter_punti);

        //Definimos los elementos del Layout
        codigoCaja = findViewById(R.id.codigoCaja);
        txtCreferencia = findViewById(R.id.txtCreferencia);
        eCedula = findViewById(R.id.eCedula);
        eMesa = findViewById(R.id.eMesa);
        eCodigo = findViewById(R.id.eCodigo);
        btnCancelarCaja = findViewById(R.id.btnCancelarCaja);
        btnIngreTermiPunti = findViewById(R.id.btnIngreTermiPunti);
        btnSalir = findViewById(R.id.btnSalir);

        //inhabilitamos el ingreso de cedula, mesa y codigo sin antes haber leido el codigo de la caja
        eCedula.setEnabled(false);
        eMesa.setEnabled(false);
        eCodigo.setEnabled(false);
        btnCancelarCaja.setEnabled(false);
        btnIngreTermiPunti.setEnabled(false);

        //Recibimos los datos desde la class PedidoInventraio
        nit_usuario = getIntent().getStringExtra("nit_usuario");
        nombre_usuario = getIntent().getStringExtra("nombre_usuario");

        //Se establece el foco en el edit text
        codigoCaja.requestFocus();

        //Se Define los varibles para el sonido de error
        sp = new SoundPool(2, AudioManager.STREAM_MUSIC,1);
        sonido_de_Reproduccion = sp.load(this, R.raw.sonido_error_2,1);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        //Se programa para que al presionar enter en el edit text haga el proceso
        codigoCaja.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if(yaentre == 0){
                    if(codigoCaja.getText().toString().trim().equals("")){
                        toastError("Por favor escribir o escanear el codigo de barras");

                    }else{
                        closeTecladoMovil();
                        verificarCodigo();
                    }
                }else{
                    cargarNuevo();
                }
                return true;
            }
            return false;
        });

        btnCancelarCaja.setOnClickListener(v -> reiniciar());

        btnSalir.setOnClickListener(this::salir);

        btnIngreTermiPunti.setOnClickListener(v -> {
            if (eCedula.getText().toString().equals("") || eMesa.getText().toString().equals("") || eCodigo.getText().toString().equals("")){
                toastError("Campos sin llenar");
            }else{
                if (eCedula.getText().toString().equals(nit_usuario)){
                    String mesa = eMesa.getText().toString().trim();
                    String codigo = eCodigo.getText().toString().trim();
                    // Obtén la fecha y hora actual
                    Date fechaActual = new Date();

                    // Define el formato de la fecha y hora que deseas obtener
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    // Convierte la fecha actual en un String con el formato definido
                    String fechaActualString = formatoFecha.format(fechaActual);

                    String cantidad;

                    if (conversion.equals("0.5")){
                        cantidad = "50";
                    }else{
                        cantidad = "1";
                    }

                    String referencia = codigoCaja.getText().toString();
                    String sql = "INSERT INTO F_Recepcion_puntilleria (OPERARIO,NOMBRE_OPERARIO,MESA,CODIGO_EMPAQUE,FECHA,REFERENCIA,CANTIDAD) VALUES ('"+nit_usuario+"','"+nombre_usuario+"',"+mesa+","+codigo+",'"+fechaActualString+"','"+referencia+"','"+ cantidad +"')";
                    Integer resp = 0;
                    try {
                        resp = objOperacionesDb.ejecutarInsertJjprgproduccion(sql,IngreProTerPunti.this);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (resp.equals(1)){
                        toastAcierto("Caja ingresada al sistema");
                        reiniciar();
                    }else{
                        toastError("Caja no ingresada");
                        reiniciar();
                    }
                }else{
                    toastError("Numero de documento diferente");
                }

            }
        });
    }

    public void reiniciar(){
        codigoCaja.setEnabled(true);
        codigoCaja.setText("");
        txtCreferencia.setText("");
        eCedula.setEnabled(false);
        eCedula.setText("");
        eMesa.setEnabled(false);
        eMesa.setText("");
        eCodigo.setEnabled(false);
        eCodigo.setText("");
        btnCancelarCaja.setEnabled(false);
        btnIngreTermiPunti.setEnabled(false);
        codigoCaja.requestFocus();
        yaentre = 0;
    }

    private void verificarCodigo() {
        conexion = new Conexion();

        codCaja = codigoCaja.getText().toString().trim();
        descripcion = conexion.obtenerDescripciónReferencias(IngreProTerPunti.this,codCaja);
        conversion = conexion.obtenerConversionReferencias(IngreProTerPunti.this,codCaja);
        boolean encontrado = false;

        if (!descripcion.equals("") && !conversion.equals("")){
            encontrado = true;
        }

        if (encontrado){
            txtCreferencia.setText(descripcion);

            //cargarNuevo();
            ingresoDatos();
        }else{
            toastError("Referencia no existente");
            AudioError();
            cargarNuevo();
        }

    }

    private void ingresoDatos() {
        //Se bloquea el EditText ya que el tiquete fue leido correctamente
        codigoCaja.setEnabled(false);

        //habilitamos los datos de ingreso de datos
        eMesa.setEnabled(true);
        eCedula.setEnabled(true);
        eCodigo.setEnabled(true);


        //Habilitamos el boton para cancelar e ingresar caja
        btnCancelarCaja.setEnabled(true);
        btnIngreTermiPunti.setEnabled(true);
    }

    //Metodo para ocultar el teclado virtual
    private void closeTecladoMovil() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void cargarNuevo() {
        codigoCaja.setText("");


        if (yaentre == 0){
            yaentre = 1;
        }else{
            yaentre = 0;
            codigoCaja.requestFocus();
        }
    }

    //METODO PARA CERRAR LA APLICACION
    @SuppressLint("")
    public void salir(View view){
        finishAffinity();
    }

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