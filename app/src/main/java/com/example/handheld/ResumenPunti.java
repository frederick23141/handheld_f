package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.atv.holder.adapters.listCajasRecepAdapter;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.CajasReceModelo;
import com.example.handheld.modelos.GalvRecepcionModelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ResumenPunti extends AppCompatActivity implements AdapterView.OnItemClickListener {

    EditText eTfecha;
    Spinner spiTurnos;
    TextView txtFechaTurno;
    Button btnCargarCajas;

    //Se declaran los elementos necesarios para el list view
    ListView listviewResumenPunti;
    List<CajasReceModelo> ListaCajasRecep= new ArrayList<>();
    ListAdapter listCajasRecepAdapter;

    //Se declara un objeto conexion
    Conexion conexion;

    private int dia,mes,ano;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_punti);

        spiTurnos = findViewById(R.id.spiTurnos);
        eTfecha = findViewById(R.id.eTfecha);
        txtFechaTurno = findViewById(R.id.txtFechaTurno);
        btnCargarCajas = findViewById(R.id.btnCargarCajas);

        listviewResumenPunti = findViewById(R.id.listviewResumenPunti);
        listviewResumenPunti.setOnItemClickListener(ResumenPunti.this); //Determinamos a que elemento va dirigido el OnItemClick

        //Se inhablitan los edittext para que no se pueda ingresar la fecha por teclado
        eTfecha.setEnabled(false);

        llenarturnos();

        txtFechaTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);

                @SuppressLint("SetTextI18n")
                DatePickerDialog datePickerDialog = new DatePickerDialog(ResumenPunti.this, (view, year, month, dayOfMonth) -> {
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
                    eTfecha.setText(year+"-"+m+"-"+d);
                },ano,mes,dia);
                datePickerDialog.show();
            }
        });

        btnCargarCajas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spiTurnos.getSelectedItem().toString().equals("Seleccione") || eTfecha.getText().toString().equals("")){
                    toastError("Faltan campos por llenar");
                }else{
                    String fecha = eTfecha.getText().toString();
                    String sql;
                    if(spiTurnos.getSelectedItem().toString().equals("Turno 1")){
                        conexion = new Conexion();
                        sql = "select REFERENCIA, count(REFERENCIA) as cantidad from F_Recepcion_puntilleria where FECHA >='"+fecha+" 06:00:00' and FECHA <= '"+fecha+" 14:00:00'  group by REFERENCIA order by REFERENCIA";
                        ListaCajasRecep = conexion.obtenerCajasRecepcionar(ResumenPunti.this, sql);
                        listCajasRecepAdapter = new listCajasRecepAdapter(ResumenPunti.this,R.layout.item_row_respunti,ListaCajasRecep);
                        listviewResumenPunti.setAdapter(listCajasRecepAdapter);
                    }else{
                        conexion = new Conexion();
                        sql = "select REFERENCIA, count(REFERENCIA) as cantidad from F_Recepcion_puntilleria where FECHA >='"+fecha+" 14:00:00' and FECHA <= '"+fecha+" 22:00:00'  group by REFERENCIA order by REFERENCIA";
                        ListaCajasRecep = conexion.obtenerCajasRecepcionar(ResumenPunti.this, sql);
                        listCajasRecepAdapter = new listCajasRecepAdapter(ResumenPunti.this,R.layout.item_row_respunti,ListaCajasRecep);
                        listviewResumenPunti.setAdapter(listCajasRecepAdapter);
                    }
                }
            }
        });
    }

    private void llenarturnos() {
        ArrayList<Object> turnos = new ArrayList<>();
        turnos.add("Seleccione");
        turnos.add("Turno 1");
        turnos.add("Turno 2");
        ArrayAdapter<String> adapter = new ArrayAdapter(ResumenPunti.this, android.R.layout.simple_spinner_dropdown_item, turnos);
        spiTurnos.setAdapter(adapter);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}