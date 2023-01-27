package com.example.handheld;

import static android.view.KeyEvent.KEYCODE_ENTER;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handheld.adapters.listpedidoAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.databinding.ActivityEscanerBinding;
import com.example.handheld.modelos.TipotransModelo;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Escaner extends AppCompatActivity {

    ActivityEscanerBinding binding;
    ArrayList<String> listaTipos;
    ArrayList<String> listaTp;
    ArrayList<TipotransModelo> tiposLista = new ArrayList<TipotransModelo>();
    Spinner spinner;
    Conexion conexion;
    Gestion_alambronLn obj_gestion_alambronLn = new Gestion_alambronLn();
    EditText etCodigo;
    TextView lblCodigo;
    TextView lblDescripcion;
    Button btnprueba;
    String pNumero;
    String pfecha;
    String pcodigo;
    String pPendiente;
    String pDescripcion;
    String nit_usuario, bod_origen, bod_destino, modelo;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null){
            Toast.makeText(this, "CANCELADO", Toast.LENGTH_SHORT).show();
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
        btnprueba = findViewById(R.id.btnprueba);

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

        btnprueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leer_nuevo();
            }
        });

        consultarTipos();

        binding.btnLeerCodigo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                escanear();
            }
        });
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
        String consecutivo = etCodigo.getText().toString();
        if (validarCodigoBarras(consecutivo)){
            String nit_proveedor = obj_gestion_alambronLn.extraerDatoCodigoBarras("proveedor", consecutivo);
            String num_importacion = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_importacion", consecutivo);
            String id_detalle = obj_gestion_alambronLn.extraerDatoCodigoBarras("detalle", consecutivo);
            String numero_rollo = obj_gestion_alambronLn.extraerDatoCodigoBarras("num_rollo", consecutivo);
            if(validarRolloRegistrado(num_importacion,numero_rollo,nit_proveedor,id_detalle)) {
                String sql_codigo = "SELECT d.codigo FROM J_alambron_solicitud_det d WHERE d.num_importacion =" + num_importacion + " AND d.nit_proveedor =" + nit_proveedor + "  AND d.id_det =" + id_detalle;
                String sql_peso = "SELECT peso FROM J_alambron_importacion_det_rollos WHERE peso IS NOT NULL AND num_importacion =" + num_importacion + " AND numero_rollo = " + numero_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + id_detalle;
                String peso = conexion.obtenerPesoAlamImport(getApplicationContext(), sql_peso);
                String codigo = conexion.obtenerCodigoAlamImport(getApplicationContext(), sql_codigo);
                if(codigo.equals(pcodigo)){
                    if(validarRolloConTransaccion(num_importacion,numero_rollo,nit_proveedor,id_detalle)){
                        Toast.makeText(Escaner.this,"Este rollo esta muy bien" , Toast.LENGTH_SHORT).show();
                    }else{
                        nit_proveedor= "999999999";
                        if (nit_proveedor.equals("999999999")){
                            //Creamos el mensaje que se mostrara con la pregunta
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setIcon(R.mipmap.ic_launcher).
                                    setTitle("¿Desactivar?").
                                    setMessage("¿Desea desactivar este tiquete único?").
                                    setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(Escaner.this, "Se elimino", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this,"Ya se le hizo una salida al rollo, Rollo con salida" , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "El código de alambrón no pertenece al pedido", Toast.LENGTH_SHORT).show();
                    leer_nuevo();
                }
            }else{
                Toast.makeText(this,"El codigo de barras no se encuentra asignado", Toast.LENGTH_SHORT).show();
            };
        }
    }


    public void consultarTipos(){
        conexion = new Conexion();

        tiposLista = conexion.obtenerTipos(getApplication());
        listaTp = obtenerLista(tiposLista);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTp);
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
            String id = conexion.obtenerIdAlamImport(getApplicationContext(), sql);
            if (id.isEmpty()){
                Toast.makeText(this, "Intente leerlo nuevamente,Problemas con el tiquete", Toast.LENGTH_SHORT).show();
                leer_nuevo();

            }else{
                resp = true;
            }
        }
        return resp;
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

    private Boolean validarRolloConTransaccion(String num_importacion, String num_rollo, String nit_proveedor, String id_detalle){
        boolean resp = false;
        String sql = "SELECT num_importacion FROM J_alambron_importacion_det_rollos WHERE num_transaccion_salida IS NOT NULL AND num_importacion =" + num_importacion + " AND numero_rollo = " + num_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + id_detalle;
        String id = conexion.obtenerNumTranAlamImport(getApplicationContext(), sql);
        if (id.isEmpty()){
            resp = true;
        }

        //Trabajo para el transalado de bodega 2 a 1
        /*if (bod_origen.equals("2") && bod_destino.equals("1")){
            if (resp == true){
                resp == false;
            }else{
                resp == true;
            }
        }*/

        return resp;
    }

    @SuppressLint("SetTextI18n")
    private void leer_nuevo(){
        lblCodigo.setText("LEA CODIGO");
        lblDescripcion.setText("LEA CODIGO");
        etCodigo.setText("");

    }

}