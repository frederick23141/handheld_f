package com.example.handheld;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.handheld.adapters.listescanerAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.databinding.ActivityEscanerBinding;
import com.example.handheld.modelos.DetalleTranModelo;
import com.example.handheld.modelos.TipotransModelo;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Escaner extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ActivityEscanerBinding binding;
    ArrayList<String> listaTipos;
    ArrayList<String> listaTp;
    ArrayList<TipotransModelo> tiposLista = new ArrayList<TipotransModelo>();
    Spinner spinner;
    Conexion conexion;

    //Herramientas para el listview
    ListView listviewEscaner;
    ListAdapter EscanerAdapter;
    List<DetalleTranModelo> ListaEscaner = new ArrayList<>();


    Gestion_alambronLn obj_gestion_alambronLn = new Gestion_alambronLn();
    ObjTraslado_bodLn objTraslado_bodLn = new ObjTraslado_bodLn();
    objOperacionesDb objOperacionesDb = new objOperacionesDb();
    Ing_prod_ad ing_prod_ad = new Ing_prod_ad();
    EditText etCodigo;
    TextView txtKilosRollo;
    TextView lblCodigo, txtIngMovimientos;
    TextView lblDescripcion, txtTransaccion;
    Button btnTransaccion, btnSalida, teclado;
    String pfecha, pcodigo, pPendiente, pDescripcion;
    Integer pNumero, pIdDetalle;
    Integer numero_transaccion;
    String nit_usuario, modelo;
    Integer bod_origen, bod_destino;
    String consecutivo;
    String nit_proveedor,num_importacion,id_detalle,numero_rollo;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null){
            toastError("CANCELADO");
        }else{
            binding.etCodigo.setText(result.getContents());
            codigoIngresado();
        }
    });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEscanerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        spinner = findViewById(R.id.spinner);
        etCodigo = findViewById(R.id.etCodigo);
        lblCodigo = findViewById(R.id.lblCodigo);
        txtIngMovimientos = findViewById(R.id.txtIngMovimientos);
        lblDescripcion = findViewById(R.id.lblDescripcion);
        txtKilosRollo = findViewById(R.id.txtKilosRollo);
        btnTransaccion = findViewById(R.id.btnTransaccion);
        txtTransaccion = findViewById(R.id.txtTransaccion);
        btnSalida = findViewById(R.id.btnSalida);
        teclado = findViewById(R.id.teclado);

        //Herramientas para el listView
        listviewEscaner = findViewById(R.id.listviewEscaner);
        listviewEscaner.setOnItemClickListener(this);

        //Recibimos los datos del pedido desde el anterior Activity
        pNumero = getIntent().getIntExtra("numero", 0);
        pIdDetalle = getIntent().getIntExtra("idDetalle", 0);
        pfecha = getIntent().getStringExtra("fecha");
        pcodigo = getIntent().getStringExtra("codigo");
        pPendiente = getIntent().getStringExtra("pendiente");
        pDescripcion = getIntent().getStringExtra("descripcion");
        //Recibimos los datos traidos desed el primer activity
        nit_usuario = getIntent().getStringExtra("nit_usuario");
        bod_origen = getIntent().getIntExtra("bod_origen", 0);
        bod_destino = getIntent().getIntExtra("bod_destino", 0);
        modelo = getIntent().getStringExtra("modelo");

        //Colocamos el titulo con la informacion
        txtTransaccion.setText(pcodigo + " - movimiento: bodega " + bod_origen + " - " + bod_destino);

        //Se establece el foco en el edit text
        etCodigo.setInputType(InputType.TYPE_NULL);
        etCodigo.requestFocus();

        consultarTipos();

        //Se programa el boton de salida de la apicación
        btnSalida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salir(v);
            }
        });

        //Se programa el boton de lectura de codigo
        binding.btnLeerCodigo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String barras = etCodigo.getText().toString();
                if (barras.equals("")){
                    escanear();
                }else{
                    codigoIngresado();
                }
            }
        });

        //Se programa para que al presionar el boton se active o desactive le teclado
        teclado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCodigo.getInputType() == InputType.TYPE_NULL){
                    etCodigo.setInputType(InputType.TYPE_CLASS_TEXT);
                }else{
                    etCodigo.setInputType(InputType.TYPE_NULL);
                }
            }
        });

        //Se programa el boton de transacción
        btnTransaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarFrm()){
                    try {
                        guardar();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //Se programa para que al presionar enter en el edit text haga el proceso
        etCodigo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(etCodigo.getText().equals("")){
                        toastError("Por favor escribir o escanear el codigo de barras");
                    }else{
                        codigoIngresado();
                    }
                    return true;
                }
                return false;
            }
        });


    }
    //METODO PARA CERRAR LA APLICACION
    @SuppressLint("")
    public void salir(View view){
        finishAffinity();;
    }

    //METODO AÑADIR ROLLOS
    public void addrollotrans(){

        EscanerAdapter = new listescanerAdapter(Escaner.this,R.layout.item_row_escaner,ListaEscaner);
        listviewEscaner.setAdapter(EscanerAdapter);
    }

    private void guardar() throws SQLException {
        String gTipo = spinner.getSelectedItem().toString();
        String gNotas = "SPIC traslado(HandHeld) usuario: " + nit_usuario;
        Double gPeso = Double.parseDouble(txtKilosRollo.getText().toString());
        String gCodigo = lblCodigo.getText().toString().trim();
        String gBodega = objTraslado_bodLn.obtenerBodegaXcodigo(gCodigo);
        Date gDFec = Calendar.getInstance().getTime();
        String gUsuario = nit_usuario;
        String gStock = conexion.consultarStock(Escaner.this,gCodigo,gBodega);
        String gConsecutivo = etCodigo.getText().toString();
        Double gNit_prov = Double.parseDouble(obj_gestion_alambronLn.extraerDatoCodigoBarras("proveedor", consecutivo));
        Double gNum_importa = Double.parseDouble(obj_gestion_alambronLn.extraerDatoCodigoBarras("num_importacion", consecutivo));
        Double gDeta = Double.parseDouble(obj_gestion_alambronLn.extraerDatoCodigoBarras("detalle", consecutivo));
        Double gNum_rollo = Double.parseDouble(obj_gestion_alambronLn.extraerDatoCodigoBarras("num_rollo", consecutivo));
        String sql_costo_unit = "SELECT d.costo_kilo FROM J_alambron_solicitud_det d WHERE d.num_importacion =" + gNum_importa + " AND d.nit_proveedor =" + gNit_prov + "  AND d.id_det =" + gDeta;
        Double gCosto_unit = Double.parseDouble(conexion.obtenerCostoUnit(Escaner.this,sql_costo_unit));

        try {
            realizar_transaccion(gCodigo, gPeso, gNit_prov, gNum_importa, gTipo, gDeta, gNum_rollo, gCosto_unit);
            etCodigo.requestFocus();
        }catch (Exception e){
            leer_nuevo();
            txtKilosRollo.setText("");
            etCodigo.setText("");
            toastError(e.getMessage());
        }


    }

    public Boolean realizar_transaccion(String gCodigo, Double gPeso, Double gNit_prov, Double gNum_importa, String gTipo, Double gDeta, Double gNum_rollo, Double gCosto_unit) throws SQLException {
        boolean resp = true;
        List<Object> listTransaccion_prod = new ArrayList<Object>();
        List<Object> listTransaccion_corsan = new ArrayList<Object>();
        String sql_rollo = "";
        String consecutivo = etCodigo.getText().toString();
        String sql_solicitud = "";
        String sql_devuelto = "";
        listTransaccion_corsan = traslado_bodega(gCodigo, gPeso, gTipo, gCosto_unit);
        sql_solicitud = "INSERT INTO J_salida_alambron_transaccion (numero,id_detalle,tipo,num_transaccion) " +
                "VALUES (" + pNumero + "," + pIdDetalle + ",'" + gTipo + "'," + numero_transaccion + ") ";
        try {
            listTransaccion_prod.add(sql_solicitud);
        }catch (Exception e){
            Toast.makeText(Escaner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (bod_origen.equals(1)  && bod_destino.equals(2)){
            sql_rollo = "UPDATE J_alambron_importacion_det_rollos SET " +
                    "num_transaccion_salida =" + numero_transaccion + " ,tipo_salida = '" + gTipo + "' " +
                    "WHERE num_importacion=" + num_importacion + " AND  id_solicitud_det =" + gDeta + " " +
                    "AND numero_rollo =" + gNum_rollo + " AND nit_proveedor =" + gNit_prov;
        }else{
            sql_rollo = "UPDATE J_alambron_importacion_det_rollos  SET num_transaccion_salida = NULL " +
                    ",tipo_salida = NULL WHERE num_importacion=" + num_importacion + " " +
                    "AND  id_solicitud_det =" + gDeta + " AND numero_rollo =" + gNum_rollo + " " +
                    "AND nit_proveedor =" + gNit_prov;

            sql_devuelto = "UPDATE J_alambron_importacion_det_rollos " +
                    "SET num_transaccion_dev =" + numero_transaccion + "" +
                    "WHERE num_importacion=" + num_importacion + " AND  id_solicitud_det =" + gDeta + " " +
                    "AND numero_rollo =" + gNum_rollo + " AND nit_proveedor =" + gNit_prov;

            objOperacionesDb.ejecutarUpdateProduccion(sql_devuelto,Escaner.this);
        }

        try {
            listTransaccion_prod.add(sql_rollo);
        }catch (Exception e){
            Toast.makeText(Escaner.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (ing_prod_ad.ExecuteSqlTransaction(listTransaccion_corsan, "JJVDMSCIERREAGOSTO", Escaner.this)){
            //Toast.makeText(Escaner.this,"Transaccion Realizada con Exito! "+ gTipo +":" + numero_transaccion, Toast.LENGTH_SHORT).show();
            if (ing_prod_ad.ExecuteSqlTransaction(listTransaccion_prod, "JJVPRGPRODUCCION", Escaner.this)){
                //Toast.makeText(Escaner.this,"Solucion, Se realizo correctamente lo de produccion ", Toast.LENGTH_SHORT).show();
                //toastAcierto("Solucion, Se realizo correctamente lo de produccion ");
                addRollo(num_importacion, consecutivo, gPeso, gNum_rollo, gDeta, gNit_prov, gTipo);
                leer_nuevo();
                contar_movimientos();
                txtKilosRollo.setText("");
                etCodigo.setText("");

                toastAcierto("Transaccion Realizada con Exito! - "+ gTipo +": " + numero_transaccion);
            }else{
                toastError("Problemas, No se realizó correctamente la transacción!");
                txtKilosRollo.setText("");
                etCodigo.setText("");
                resp = false;
            }

        }else{
            //Toast.makeText(Escaner.this,"Error al realizar la transacción!", Toast.LENGTH_SHORT).show();
            toastError("Error al realizar la transacción!");
            resp = false;
        }
        return  resp;
    }

    private void contar_movimientos() {
        int size = ListaEscaner.size();
        String sizeString = Integer.toString(size);
        txtIngMovimientos.setText(sizeString);

    }

    public void addRollo(String num_importacion, String consecutivo, Double peso, Double num_rollo, Double id_detalle, Double nit_prov, String tipo){
        DetalleTranModelo escanerModelo;

        String sql_codigo = "SELECT codigo FROM  J_alambron_solicitud_det WHERE num_importacion = " + num_importacion + " AND nit_proveedor =" + nit_prov + " AND id_det =" + id_detalle;
        String codigo = conexion.obtenerCodigo(Escaner.this, sql_codigo);
        String sql_descripcion = "SELECT descripcion  FROM  referencias WHERE codigo = '" + codigo + "'";
        String descripcion = conexion.obtenerDescripcionCodigo(Escaner.this, sql_descripcion);

        escanerModelo = new DetalleTranModelo();
        escanerModelo.setNumero(consecutivo);
        escanerModelo.setTipo(tipo);
        escanerModelo.setNum_trans(numero_transaccion.toString());
        escanerModelo.setCodigo(codigo);
        escanerModelo.setPeso(peso.toString());
        escanerModelo.setNum_imp(num_importacion);
        escanerModelo.setDetalle(id_detalle.toString());
        escanerModelo.setNum_rollo(num_rollo.toString());
        escanerModelo.setEstado_muestra("0");
        escanerModelo.setNit_prov(nit_prov.toString());
        escanerModelo.setCosto_unit("0");
        ListaEscaner.add(escanerModelo);

        addrollotrans();

    }


    private boolean validarFrm(){
        String sql_cantidad = "SELECT (D.cantidad - (SELECT COUNT(numero) FROM J_salida_alambron_transaccion  WHERE numero = D.numero AND id_detalle = D.id_detalle))As pendiente FROM J_salida_alambron_enc E ,J_salida_alambron_det D, CORSAN.dbo.referencias R WHERE E.anulado is null AND  R.codigo = D.codigo AND D.numero = E.numero  and e.numero=" + pNumero + "";
        String cantidad = conexion.obtenerCantidadPedido(Escaner.this, sql_cantidad);
        if (!lblCodigo.getText().toString().isEmpty() && !lblCodigo.getText().toString().equals("LEA CODIGO")){
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
        //listaTipos.add("Seleccione");

        for(int i = 0; i < tiposLista.size(); i++){
            listaTipos.add(tiposLista.get(i).getTipo().toString());
        }

        return listaTipos;
    }

    private void codigoIngresado(){
        consecutivo = etCodigo.getText().toString();
        //consecutivo = "444444218-4-3-168";
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
            }
        }
    }


    public void consultarTipos(){
        conexion = new Conexion();

        tiposLista = conexion.obtenerTipos(getApplication());
        listaTp = obtenerLista(tiposLista);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(Escaner.this, android.R.layout.simple_spinner_item, listaTp);
        spinner.setEnabled(false);
        spinner.setClickable(false);
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

    //METODO DE TOAST PERSONALIZADO : ACIERTO
    public void toastAcierto(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_acierto, (ViewGroup) findViewById(R.id.ll_custom_toast_acierto));
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtMens = view.findViewById(R.id.txtMensa);
        txtMens.setText(msg);

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
    private List<Object> traslado_bodega(String codigo, Double cantidad, String tipo, Double costo_unit){
        List<Object> listSql;

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = dateFormat.format(calendar.getTime());

        String usuario = nit_usuario;
        String notas = "MOVIL fecha:" + fecha + " usuario:" + usuario;
        numero_transaccion = Integer.valueOf(Obj_ordenprodLn.mover_consecutivo(tipo, Escaner.this));
        listSql = objTraslado_bodLn.listaTransaccionDatable_traslado_bodega(numero_transaccion, codigo, bod_origen, bod_destino, calendar, notas, usuario, cantidad, tipo, modelo, costo_unit,Escaner.this);
        return listSql;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}