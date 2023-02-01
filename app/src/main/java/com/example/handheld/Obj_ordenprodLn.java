package com.example.handheld;

import android.content.Context;
import android.widget.Toast;

import com.example.handheld.conexionDB.Conexion;

public class Obj_ordenprodLn {
    static Conexion conexion;
    public static String mover_consecutivo(String tipo, Context context){
        String numero = "";
        try {
            String sqlMaxNumero  = "SELECT CASE WHEN (MAX (siguiente)) is null THEN 0 ELSE MAX (siguiente) END as numero  FROM consecutivos WHERE  tipo = '" + tipo + "'";
            numero = conexion.obtenerConsecutivo(context, sqlMaxNumero);
        }catch (Exception e){
            Toast.makeText(context, "a", Toast.LENGTH_SHORT).show();
        }


        return  numero;

    }
}
