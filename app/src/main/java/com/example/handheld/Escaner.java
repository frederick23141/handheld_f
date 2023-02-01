package com.example.handheld;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.databinding.ActivityEscanerBinding;
import com.example.handheld.modelos.TipotransModelo;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Escaner extends AppCompatActivity {

    ActivityEscanerBinding binding;
    ArrayList<String> listaTipos;
    ArrayList<String> listaTp;
    ArrayList<TipotransModelo> tiposLista = new ArrayList<TipotransModelo>();
    Spinner spinner;
    Conexion conexion;
    Gestion_alambronLn obj_gestion_alambronLn = new Gestion_alambronLn();
    EditText etCodigo;
    TextView txtKilosRollo;
    TextView lblCodigo;
    TextView lblDescripcion;
    Button btnTransaccion;
    String pNumero;
    String pfecha;
    String pcodigo;
    String pPendiente;
    String pDescripcion;
    String numero_transaccion;
    String nit_usuario, bod_origen, bod_destino, modelo;
    String consecutivo;
    Button btnPrueba;
    String nit_proveedor,num_importacion,id_detalle,numero_rollo;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null){
            Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show();
            //codigoIngresado();
        }else{
            binding.etCodigo.setText(result.getContents());
            codigoIngresado();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEscanerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinner = findViewById(R.id.spinner);
        etCodigo = findViewById(R.id.etCodigo);
        lblCodigo = findViewById(R.id.lblCodigo);
        lblDescripcion = findViewById(R.id.lblDescripcion);
        txtKilosRollo = findViewById(R.id.txtKilosRollo);
        btnTransaccion = findViewById(R.id.btnTransaccion);
        btnPrueba = findViewById(R.id.btnPrueba);

        //Recibimos los datos del pedido desde el anterior Activity
        pNumero = getIntent().getStringExtra("numero");
        pfecha = getIntent().getStringExtra("fecha");
        pcodigo = getIntent().getStringExtra("codigo");
        pPendiente = getIntent().getStringExtra("pendiente");
        pDescripcion = getIntent().getStringExtra("descripcion");
        //Recibimos los datos traidos desed el primer activity
        nit_usuario = getIntent().getStringExtra("nit_usuario");
        bod_origen = getIntent().getStringExtra("bod_origen");
        bod_destino = getIntent().getStringExtra("bod_destino");
        modelo = getIntent().getStringExtra("modelo");


        consultarTipos();

        binding.btnLeerCodigo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                escanear();
            }
        });

        btnTransaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarFrm()){
                    guardar();
                    Toast.makeText(Escaner.this,"Todo esta bien", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numero_transaccion = Obj_ordenprodLn.mover_consecutivo("TRB1", Escaner.this);
            }
        });

    }

    private void guardar(){
        String gTipo = spinner.getSelectedItem().toString();
        String gNotas = "SPIC traslado(HandHeld) usuario: " + nit_usuario;
        String gPeso = txtKilosRollo.getText().toString();
        String gCodigo = lblCodigo.getText().toString().trim();
        String gBodega;
        Date gDFec = Calendar.getInstance().getTime();
        String gUsuario = nit_usuario;
        String gStock;
        String gConsecutivo = etCodigo.getText().toString();
        String gNit_prov = obj_gestion_alambronLn.extraerDatoCodigoBarras("proveedor", consecutivo);
        String gNum_importa = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_importacion", consecutivo);
        String gDeta = obj_gestion_alambronLn.extraerDatoCodigoBarras("detalle", consecutivo);
        String gNum_rollo = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_rollo", consecutivo);
        String sql_costo_unit = "SELECT d.costo_kilo FROM J_alambron_solicitud_det d WHERE d.num_importacion =" + gNum_importa + " AND d.nit_proveedor =" + gNit_prov + "  AND d.id_det =" + gDeta;
        String gCosto_unit = conexion.obtenerCostoUnit(Escaner.this,sql_costo_unit);

        realizar_transaccion(gCodigo, gPeso, gNit_prov, gNum_importa, gTipo, gDeta, gNum_rollo, gCosto_unit);

    }

    public Boolean realizar_transaccion(String gCodigo, String gPeso, String gNit_prov, String gNum_importa, String gTipo, String gDeta, String gNum_rollo, String gCosto_unit){
        Boolean resp = true;
        List<Object> listTransaccion_prod = new ArrayList<Object>();
        List<Object> listTransaccion_corsan = new ArrayList<Object>();
        String sql_rollo = "";
        String consecutivo = etCodigo.getText().toString();
        String sql_solicitud = "";
        String sql_devuelto = "";
        listTransaccion_corsan = traslado_bodega(gCodigo, gPeso, gTipo, gCosto_unit);
        //sql_solicitud = "INSERT INTO J_salida_alambron_transaccion(numero,id_detalle,tipo,num_transaccion) VALUES (" + pNumero + "," + num_sol_detalle + ",'" + tipo + "'," + numero_transaccion + ") ";
        return  resp;
    }

    private boolean validarFrm(){
        String sql_cantidad = "SELECT (D.cantidad - (SELECT COUNT(numero) FROM J_salida_alambron_transaccion  WHERE numero = D.numero AND id_detalle = D.id_detalle))As pendiente FROM J_salida_alambron_enc E ,J_salida_alambron_det D, CORSAN.dbo.referencias R WHERE E.anulado is null AND  R.codigo = D.codigo AND D.numero = E.numero  and e.numero=" + pNumero + "";
        String cantidad = conexion.obtenerCantidadPedido(Escaner.this, sql_cantidad);
        if (!lblCodigo.getText().toString().isEmpty()){
            if (!txtKilosRollo.getText().toString().isEmpty()){
                if (!spinner.getSelectedItem().equals("Seleccione")){
                    if (conexion.existeCodigo(Escaner.this, lblCodigo.getText().toString())){
                        if (Double.parseDouble(txtKilosRollo.getText().toString()) > 0) {
                            if (!cantidad.equals("0")){
                                if (conexion.existeTipoTransaccion(Escaner.this,spinner.getSelectedItem().toString())){
                                    if (!etCodigo.getText().equals("")){
                                        if (validarCodigoBarras(etCodigo.getText().toString())){
                                            return true;
                                        }else{
                                            toastError("Verifique, El código de barras no se encuentra asignado!");
                                        }
                                    }else{
                                        toastError("Verifique, No se leyo ningun código de barras!");
                                    }
                                }else{
                                    toastError("Verifique, No existe el tipo de transacción!");
                                }
                            }else{
                                toastError("Verifique, No se puede leer más alambron en este pedido!");
                            }
                        }else{
                            toastError("Verifique, Los kilos no pueden ser negativos ó iguales a (0)");
                        }
                    }else{
                        toastError("Verifique, falta el CODIGO no existe");
                    }
                }else{
                    toastError("Verifique, falta el TIPO de transacción");
                }
            }else{
                toastError("Verifique, faltan los KILOS");
            }
        }else{
            toastError("Verifique, falta el CÓDIGO");
        }
        return false;
    }


    private ArrayList<String> obtenerLista(ArrayList<TipotransModelo> tiposLista ){
        listaTipos = new ArrayList<String>();
        listaTipos.add("Seleccione");

        for(int i = 0; i < tiposLista.size(); i++){
            listaTipos.add(tiposLista.get(i).getTipo().toString());
        }

        return listaTipos;
    }

    private void codigoIngresado(){
        //consecutivo = etCodigo.getText().toString();
        consecutivo = "444444218-4-3-168";
        if (validarCodigoBarras(consecutivo)){
            nit_proveedor = obj_gestion_alambronLn.extraerDatoCodigoBarras("proveedor", consecutivo);
            num_importacion = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_importacion", consecutivo);
            id_detalle = obj_gestion_alambronLn.extraerDatoCodigoBarras("detalle", consecutivo);
            numero_rollo = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_rollo", consecutivo);
            if(validarRolloRegistrado(num_importacion,numero_rollo,nit_proveedor,id_detalle)) {
                String sql_codigo = "SELECT d.codigo FROM J_alambron_solicitud_det d WHERE d.num_importacion =" + num_importacion + " AND d.nit_proveedor =" + nit_proveedor + "  AND d.id_det =" + id_detalle;
                String sql_peso = "SELECT peso FROM J_alambron_importacion_det_rollos WHERE peso IS NOT NULL AND num_importacion =" + num_importacion + " AND numero_rollo = " + numero_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + id_detalle;
                String peso = conexion.obtenerPesoAlamImport(getApplicationContext(), sql_peso);
                String codigo = conexion.obtenerCodigoAlamImport(getApplicationContext(), sql_codigo);
                if(codigo.equals(pcodigo)){
                    Boolean valid;
                    valid = validarRolloConTransaccion(num_importacion,numero_rollo,nit_proveedor,id_detalle);
                    if(valid.equals(true)){
                        //Toast.makeText(Escaner.this,"Rollovalidado", Toast.LENGTH_SHORT).show();
                        lblCodigo.setText(codigo);
                        String sql_descripcion = "SELECT descripcion FROM referencias WHERE  codigo = '" + codigo + "'";
                        lblDescripcion.setText(conexion.obtenerDescripcionCodigo(Escaner.this,sql_descripcion));
                        txtKilosRollo.setText(peso);
                    }else{
                        //nit_proveedor= "999999999";
                        if (nit_proveedor.equals("999999999")){
                            //Creamos el mensaje que se mostrara con la pregunta
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setIcon(R.mipmap.ic_error_mimap).
                                    setTitle("¿Desactivar?").
                                    setMessage("¿Desea desactivar este tiquete único?").
                                    setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            boolean resp = false;
                                            resp = conexion.eliminarTiqueteUnico(Escaner.this, num_importacion, numero_rollo, nit_proveedor, id_detalle);
                                            if (resp){
                                                Toast.makeText(Escaner.this, "El rollo se desactivo en forma correcta!", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(Escaner.this, "!Error al desactivar el rollo", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }).
                                    setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(Escaner.this, "SE cancelo la eliminacion", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        toastError("Ya se le hizo una salida al rollo");
                        //Toast.makeText(this,"Ya se le hizo una salida al rollo, Rollo con salida" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    toastError("El código de alambrón no pertenece al pedido");
                    //Toast.makeText(this, "El código de alambrón no pertenece al pedido", Toast.LENGTH_SHORT).show();
                    leer_nuevo();
                }
            }else{
                toastError("El codigo de barras no se encuentra asignado");
                //Toast.makeText(this,"El codigo de barras no se encuentra asignado", Toast.LENGTH_SHORT).show();
            };
        }
    }


    public void consultarTipos(){
        conexion = new Conexion();

        tiposLista = conexion.obtenerTipos(getApplication());
        listaTp = obtenerLista(tiposLista);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(Escaner.this, android.R.layout.simple_spinner_item, listaTp);
        spinner.setAdapter(adapter);
    }

    public void escanear() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
        options.setPrompt("ESCANEAR CODIGO");
        options.setCameraId(0);
        options.setOrientationLocked(false);
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureActivityPortraint.class);
        options.setBarcodeImageEnabled(false);

        barcodeLauncher.launch(options);
    }

    private boolean validarCodigoBarras(String consecutivo){
        boolean resp = false;

        String nit_proveedor = obj_gestion_alambronLn.extraerDatoCodigoBarras("proveedor", consecutivo);
        String num_importacion = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_importacion", consecutivo);
        String id_detalle = obj_gestion_alambronLn.extraerDatoCodigoBarras("detalle", consecutivo);
        String numero_rollo = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_rollo", consecutivo);

        if (!num_importacion.isEmpty() && !numero_rollo.isEmpty() && !id_detalle.isEmpty() && !nit_proveedor.isEmpty()) {
            String sql = "SELECT id FROM J_alambron_importacion_det_rollos WHERE num_importacion =" + num_importacion + " AND numero_rollo = " + numero_rollo + " AND nit_proveedor = " + nit_proveedor + " AND id_solicitud_det = " + id_detalle;
            String id = conexion.obtenerIdAlamImport(Escaner.this, sql);
            if (id.isEmpty()){
                //Toast.makeText(this, "Intente leerlo nuevamente,Problemas con el tiquete", Toast.LENGTH_SHORT).show();
                toastError("Intente leerlo nuevamente,Problemas con el tiquete");
                leer_nuevo();

            }else{
                resp = true;
            }
        }else{
            toastError("Intente leerlo nuevamente,Problemas con el tiquete");
        }
        return resp;
    }

    //METODO DE TOAST PERSONALIZADO : ERROR
    public void toastError(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_per_no_encon, (ViewGroup) findViewById(R.id.ll_custom_toast_per_no_encon));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    private boolean validarRolloRegistrado(String num_importacion, String num_rollo, String nit_proveedor, String id_detalle){
        boolean resp = false;
        String sql = "SELECT peso FROM J_alambron_importacion_det_rollos WHERE peso IS NOT NULL AND num_importacion =" + num_importacion + " AND numero_rollo = " + num_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + id_detalle;
        String peso = conexion.obtenerPesoAlamImport(getApplicationContext(), sql);
        if (!peso.isEmpty()){
            resp = true;
        }
        return resp;
    }

    private boolean validarRolloConTransaccion(String num_importacion, String num_rollo, String nit_proveedor, String id_detalle){
        boolean respuesta = false;
        try {
            String sql = "SELECT num_importacion FROM J_alambron_importacion_det_rollos WHERE num_transaccion_salida IS NOT NULL AND num_importacion =" + num_importacion + " AND numero_rollo = " + num_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + id_detalle;
            String id = conexion.obtenerNumTranAlamImport(getApplicationContext(), sql);
            if (id.isEmpty()){
                respuesta = true;
            }
        }catch (Exception e){
            Toast.makeText(Escaner.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


        //Trabajo para el translator de bodega 2 a 1
        /*if (bod_origen.equals("2") && bod_destino.equals("1")){
            if (resp == true){
                resp == false;
            }else{
                resp == true;
            }
        }*/

        return respuesta;
    }

    @SuppressLint("SetTextI18n")
    private void leer_nuevo(){
        lblCodigo.setText("LEA CODIGO");
        lblDescripcion.setText("LEA CODIGO");
        etCodigo.setText("");

    }

    //Solo para 'TRB1' modelo 08 traslado de la 1 a la 2
    // Solo para 'TRB1' modelo 12 traslado de la 2 a la 1
    private List<Object> traslado_bodega(String Codigo, String cantidad, String tipo, String costo_unit){
        List<Object> listSql = new ArrayList<Object>();
        Date fecha = new Date(Calendar.getInstance().getTimeInMillis());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        String fechaTexto = formatter.format(fecha);
        String usuario = nit_usuario;
        String notas = "SPIC fecha:" + fechaTexto + " usuario:" + usuario;
        numero_transaccion = Obj_ordenprodLn.mover_consecutivo(tipo, getApplicationContext());
        return listSql;
    }

}