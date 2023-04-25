package com.example.handheld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.ClasesOperativas.Ing_prod_ad;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.CentrosModelo;

import java.util.ArrayList;
import java.util.Calendar;

public class PedidoInventario extends AppCompatActivity implements View.OnClickListener {

    //SE OMITE ESTE CLASES PORQUE SE DECIDE QUE LA RECEPCION DE GALVANIZADO AHORA NO TENDRA
    //CORTE Y MOSTRARA TODOS LOS ROLLOS QUE HAY POR RECOGER


    //se declaran las variables donde estaran los datos que vienen de la anterior clase
    String nit_usuario;

    //Se declaran los elementos del layout
    TextView txtTfechaInicio, txtTFechaFin, txtTHoraInicio, txtTHoraFin;
    EditText txtFechaInicio, txtFechaFin, txtHoraInicio, txtHoraFin;
    Button btn_Iniciar_proceso;
    Spinner spinner2;

    //Se inicializa un objeto conexion
    Conexion conexion;


    private int dia,mes,ano,hora,minutos;
    Integer id = 0;
    Integer tipos = 0;
    String tp = "";
    Ing_prod_ad objIng_prod_ad = new Ing_prod_ad();

    //Se inicializan las listas para el comboBox
    ArrayList<String> listaCentros;
    ArrayList<String> listaCen;
    ArrayList<CentrosModelo> centrosLista = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_inventario);

        //Se Definen los elementos de layout
        txtTfechaInicio = findViewById(R.id.txtTfechaInicio);
        txtTFechaFin = findViewById(R.id.txtTFechaFin);
        txtTHoraInicio = findViewById(R.id.txtTHoraInicio);
        txtTHoraFin = findViewById(R.id.txtTHoraFin);

        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        txtHoraInicio = findViewById(R.id.txtHoraInicio);
        txtHoraFin = findViewById(R.id.txtHoraFin);
        btn_Iniciar_proceso = findViewById(R.id.btn_Iniciar_proceso);
        spinner2 = findViewById(R.id.spinner2);

        //Se agregamos el metodo setOnClickListener a los campos
        txtTfechaInicio.setOnClickListener(this);
        txtTFechaFin.setOnClickListener(this);
        txtTHoraInicio.setOnClickListener(this);
        txtTHoraFin.setOnClickListener(this);

        //Se inhablitan los edittext para que no se pueda ingresar la fecha por teclado
        txtFechaInicio.setEnabled(false);
        txtFechaFin.setEnabled(false);
        txtHoraInicio.setEnabled(false);
        txtHoraFin.setEnabled(false);

        //Recibimos el documento desde la class Main Activity
        nit_usuario = getIntent().getStringExtra("nit_usuario");

        //Activamos el metodo para consultar los centros
        consultarCentros();

        //Bloqueamos el spinner para que solo se vea galvanizado
        spinner2.setEnabled(false);

        //////////////////////////////////////////////////////////////////////////////////////////////
        btn_Iniciar_proceso.setOnClickListener(v -> {
            if (!spinner2.getSelectedItem().equals("Seleccione") && !txtFechaInicio.getText().toString().trim().equals("")
                    && !txtFechaFin.getText().toString().trim().equals("")&& !txtHoraInicio.getText().toString().trim().equals("")
                    && !txtHoraFin.getText().toString().trim().equals("")){
                iniciarProceso();
            }else{
                toastError("Faltan campos por llenar");
            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    private void iniciarProceso() {
        String fecha_inicio = txtFechaInicio.getText().toString() + " " + txtHoraInicio.getText().toString()+":00";
        String fecha_final = txtFechaFin.getText().toString() + " " + txtHoraFin.getText().toString()+":00";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_alert).
                setTitle("Atención").
                setMessage("Se iniciara el proceso desde la fecha_hora: " + fecha_inicio + " hasta fecha_hora: " + fecha_final).
                setPositiveButton("Aceptar", (dialog, which) -> {
                    Intent intent  = new Intent(PedidoInventario.this,EscanerInventario.class);
                    intent.putExtra("nit_usuario", nit_usuario);
                    intent.putExtra("fecha_inicio",fecha_inicio);
                    intent.putExtra("fecha_final",fecha_final);
                    startActivity(intent);
                }).setNegativeButton("Cancelar", (dialog, which) -> {

                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onClick(View v) {
        if (v==txtTfechaInicio){
            Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String m;
                String d;
                if ((month+1)<10){
                    m = "0" + (month + 1);
                }else{
                    m = String.valueOf(month+1);
                }
                if (dayOfMonth<10){
                    d = "0" + dayOfMonth;
                }else{
                    d = String.valueOf(dayOfMonth);
                }
                txtFechaInicio.setText(year+"-"+m+"-"+d);
            },ano,mes,dia);
            datePickerDialog.show();
        }
        if (v==txtTFechaFin){
            Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            ano = c.get(Calendar.YEAR);

            @SuppressLint("SetTextI18n")
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String m;
                String d;
                if ((month+1)<10){
                    m = "0" + (month + 1);
                }else{
                    m = String.valueOf(month+1);
                }
                if (dayOfMonth<10){
                    d = "0" + dayOfMonth;
                }else{
                    d = String.valueOf(dayOfMonth);
                }
                txtFechaFin.setText(year+"-"+m+"-"+d);
            },ano,mes,dia);
            datePickerDialog.show();
        }
        if (v==txtTHoraInicio){
            Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog =  new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                String h;
                String m;
                if (hourOfDay<10){
                    h = "0" + hourOfDay;
                }else{
                    h = String.valueOf(hourOfDay);
                }
                if (minute<10){
                    m = "0" + minute;
                }else{
                    m = String.valueOf(minute);
                }
                txtHoraInicio.setText(h+":"+m);
            },hora,minutos,true);
            timePickerDialog.show();
        }
        if (v==txtTHoraFin){
            Calendar c = Calendar.getInstance();
            hora = c.get(Calendar.HOUR_OF_DAY);
            minutos = c.get(Calendar.MINUTE);

            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog =  new TimePickerDialog(this, (view, hourOfDay, minute) -> {
                String h;
                String m;
                if (hourOfDay<10){
                    h = "0" + hourOfDay;
                }else{
                    h = String.valueOf(hourOfDay);
                }
                if (minute<10){
                    m = "0" + minute;
                }else{
                    m = String.valueOf(minute);
                }
                txtHoraFin.setText(h+":"+m);
            },hora,minutos,true);
            timePickerDialog.show();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    private void consultarCentros() {
        conexion = new Conexion();

        centrosLista = conexion.obtenerCentros(getApplication());
        listaCen = obtenerLista(centrosLista);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(PedidoInventario.this, android.R.layout.simple_spinner_item, listaCen);
        spinner2.setAdapter(adapter);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //Metodoque recibe una lista tipo transmodelo, la recorre y añade a otra lista tipo String
    private ArrayList<String> obtenerLista(ArrayList<CentrosModelo> tiposLista ){
        listaCentros = new ArrayList<>();

        listaCentros.add("5200--PLANTA ALAMB. GALV");
        for(int i = 0; i < tiposLista.size(); i++){
            listaCentros.add(tiposLista.get(i).getDescripcion());
        }

        return listaCentros;
    }

    /*
    private Boolean verificarInventariosPendientes() {
        Boolean resp = false;
        String sql = "SELECT id,codigo,bodega FROM J_inventario_enc c WHERE fecha_terminado is null AND c.nit =" + nit_usuario;
        ArrayList<InventarioModelo> dt = new ArrayList<>();
        dt = conexion.obtenerInven(PedidoInventario.this,sql);
        for (int x=0;x<dt.size();x++){
            id = Integer.valueOf(dt.get(x).getId());
            if(dt.get(x).getCodigo().equals("2100") || dt.get(x).getCodigo().equals("2200") || dt.get(x).getCodigo().equals("2300") || dt.get(x).getCodigo().equals("5200") || dt.get(x).getCodigo().equals("6400")){
                toastError("Tiene un inventario pendiente por cerrar");
                if(dt.get(x).getCodigo().equals("2100")){
                    tp="T";
                }
                if(dt.get(x).getCodigo().equals("2200")){
                    tp="R";
                }
                if(dt.get(x).getCodigo().equals("2300")){
                    tp="P";
                }
                if(dt.get(x).getCodigo().equals("5200")){
                    tp="G";
                }
                if(dt.get(x).getCodigo().equals("6400")){
                    tp="PU";
                }
                tipos = 0;
                //cargarRollos(id) - Organizar para ir a la otra activity
            }else{
                tipos = 1;
                toastError("Tiene un inventario pendiente por cerrar");
                //cargarInventario(id)
            }
        }
        return false;
    }

    private void cargar_centros() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_alert).
                setTitle("Atención").
                setMessage("Seguro de iniciar inventario?").
                setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bodega = "";
                        String dato = "";
                        if (spinner2.getSelectedItem().equals("2300--PUNTILLERIA ")){
                            bodega = "12";
                            tp = "P";
                            dato = "scla";
                        }
                        if (spinner2.getSelectedItem().equals("2200--HORNO RECOCIDO ")){
                            bodega = "13";
                            tp = "R";
                            dato = "srec";
                        }
                        List<Object> listSql = new ArrayList<>();
                        String sql_id = "SELECT (CASE WHEN max(id) IS NULL THEN 1 ELSE max(id)+1 END)id FROM J_inventario_enc";
                        id = conexion.obtenerIdInv(PedidoInventario.this, sql_id);
                        String sql = "INSERT INTO J_inventario_enc (id,nit,fecha,codigo,bodega) VALUES ( "+ id + ", " + nit_usuario + ",GETDATE(),'" + spinner2.getSelectedItem().toString() + "'," + bodega + ") ";
                        try {
                            listSql.add(sql);
                        }catch (Exception e){
                            Toast.makeText(PedidoInventario.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                        try {
                            //Aqui hay un error
                            listSql.add(cargar_rollos_centro(tp, dato, id));
                        }catch (Exception e){
                            Toast.makeText(PedidoInventario.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                        if (objIng_prod_ad.ExecuteSqlTransaction(listSql,"JJVPRGPRODUCCION",PedidoInventario.this)){
                            tipos = 0;
                            toastAcierto("Inventarios iniciados con exito");
                            showSuccessDialog();
                            //Pasar al otro activity y mandar el id
                        }else{
                            toastError("Error al crear la planilla de separe");
                        }

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private Object cargar_rollos_centro(String tp, String dato, Integer id) {
        List<Object> listSql = new ArrayList<>();
        String sql = "SELECT cod_orden,id_detalle,id_rollo FROM J_rollos_tref WHERE destino = '" + tp + "' AND " + dato +" Is not Null AND anulado Is Null";
        ArrayList<RolloterminadoModelo> dt = new ArrayList<>();
        dt = conexion.obtenerRollosTerm(PedidoInventario.this, sql);
        for (int i = 0; i < dt.size(); i++){
            sql = "INSERT INTO J_inventario_alambre (cod_orden,id_detalle,id_rollo,inventario) VALUES ('" + dt.get(i).getCod_orden() + "','" + dt.get(i).getId_detalle() + "','" + dt.get(i).getId_rollo() + "'," + id + ")";
            try {
                listSql.add(sql);
            }catch (Exception e){
                Toast.makeText(PedidoInventario.this, e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }
        return listSql;
    }

     */

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
}