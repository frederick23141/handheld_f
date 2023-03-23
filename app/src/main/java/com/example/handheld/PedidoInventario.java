package com.example.handheld;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
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

import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.CentrosModelo;
import com.example.handheld.modelos.InventarioModelo;
import com.example.handheld.modelos.RolloterminadoModelo;
import com.example.handheld.modelos.TipotransModelo;

import java.util.ArrayList;
import java.util.List;

public class PedidoInventario extends AppCompatActivity {

    String nit_usuario;

    //Se declaran los elementos del layout
    EditText txtReferencia, txtBodega;
    Button btn_comenzar;
    Spinner spinner2;

    //Se inicializa un objeto conexion
    Conexion conexion;

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
        txtBodega = findViewById(R.id.txtBodega);
        txtReferencia = findViewById(R.id.txtReferencia);
        btn_comenzar = findViewById(R.id.btn_comenzar);
        spinner2 = findViewById(R.id.spinner2);

        //Recibimos el documento desde la class Main Activity
        nit_usuario = getIntent().getStringExtra("nit_usuario");

        //Activamos el metodo para consultar los centros
        consultarCentros();

        verificarInventariosPendientes();

        btn_comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spinner2.getSelectedItem().equals("Seleccione")){
                    cargar_centros();
                }else{

                }
            }
        });

    }


    private void consultarCentros() {
        conexion = new Conexion();

        centrosLista = conexion.obtenerCentros(getApplication());
        listaCen = obtenerLista(centrosLista);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(PedidoInventario.this, android.R.layout.simple_spinner_item, listaCen);
        spinner2.setAdapter(adapter);
    }

    //Metodoque recibe una lista tipo transmodelo, la recorre y añade a otra lista tipo String
    private ArrayList<String> obtenerLista(ArrayList<CentrosModelo> tiposLista ){
        listaCentros = new ArrayList<>();

        listaCentros.add("Seleccione");
        for(int i = 0; i < tiposLista.size(); i++){
            listaCentros.add(tiposLista.get(i).getDescripcion());
        }


        return listaCentros;
    }

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
                txtReferencia.setText(dt.get(x).getCodigo());
                txtBodega.setText(dt.get(x).getBodega());
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

    //METODO DE TOAST PERSONALIZADO : ACIERTO
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

    private void showSuccessDialog(){
        ConstraintLayout successContraintLayout = findViewById(R.id.successConstraintLayout);
        View view = LayoutInflater.from(PedidoInventario.this).inflate(R.layout.success_dialog, successContraintLayout);
        Button successDone = view.findViewById(R.id.successDone);

        AlertDialog.Builder builder = new AlertDialog.Builder(PedidoInventario.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        successDone.findViewById(R.id.successDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(PedidoInventario.this, "Aceptar", Toast.LENGTH_SHORT).show();

            }
        });
        if (alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

    }
}