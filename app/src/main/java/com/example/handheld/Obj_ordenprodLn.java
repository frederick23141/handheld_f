package com.example.handheld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.conexionDB.Conexion;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Obj_ordenprodLn {
    static Conexion conexion = new Conexion();
    static objOperacionesDb objOperacionesDb = new objOperacionesDb();
    public static String mover_consecutivo(String tipo, Context context) {
        String numeroString = "";
        int numero = 0;
        try {
            String sqlMaxNumero = "SELECT CASE WHEN (MAX (siguiente)) is null THEN 0 ELSE MAX (siguiente) END as numero  FROM consecutivos WHERE  tipo = '" + tipo + "'";
            numeroString = conexion.obtenerConsecutivo(context, sqlMaxNumero);
            numero = Integer.parseInt(numeroString);
            numero += 1;
            String sql = "UPDATE consecutivos SET siguiente = " + Integer.toString(numero) + " WHERE tipo = '" + tipo + "'";
            objOperacionesDb.ejecutarUpdate(sql, context);

        } catch (Exception e) {
            Toast.makeText(context, "a", Toast.LENGTH_SHORT).show();
        }
        return Integer.toString(numero);
    }

    //Se verifica si se hizo cierre en el mes actual para ingresar produccion conm fecha del mes proximo
    public Boolean insertarProxMes(String codigo, Context context ){
        //Calcula la fecha actual
        Calendar calendar = Calendar.getInstance();
        //Agregamos un mes a la fecha
        calendar.add(Calendar.MONTH, 1);

        //Capturamos el mes en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatMoth = new SimpleDateFormat("MM");
        String month = dateFormatMoth.format(calendar.getTime());

        //Capturamos el año en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        String year = dateFormatYear.format(calendar.getTime());


        String sql = "SELECT mes FROM referencias_sto  WHERE codigo ='" + codigo + "' AND mes = " + month + " and ano = " + year + "";

        if(!conexion.obtenerMes(context, sql).equals("")){
            return true;
        }else{
            return false;
        }

    }
}
