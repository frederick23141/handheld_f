package com.example.handheld;

import android.content.Context;
import android.widget.Toast;

import com.example.handheld.conexionDB.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class objOperacionesDb {
    public Integer ejecutarUpdate(String Sql, Context context) throws SQLException {
        int resp = 0;
        Connection cnn = new Conexion().conexionBD("JJVDMSCIERREAGOSTO",context);
        try {
            if (cnn != null){
                PreparedStatement stm = cnn.prepareStatement(Sql);
                stm.executeUpdate();
                resp = 1;
                Toast.makeText(context, "REGISTRO MODIFICADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        assert cnn != null;
        cnn.close();
        return resp;
    }
    public Integer ejecutarUpdateProduccion(String Sql, Context context) throws SQLException {
        int resp = 0;
        Connection cnn = new Conexion().conexionBD("JJVDMSCIERREAGOSTO",context);
        try {
            if (cnn != null){
                PreparedStatement stm = cnn.prepareStatement(Sql);
                stm.executeUpdate();
                resp = 1;
                //Toast.makeText(context, "REGISTRO MODIFICADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        assert cnn != null;
        cnn.close();
        return resp;
    }
}
