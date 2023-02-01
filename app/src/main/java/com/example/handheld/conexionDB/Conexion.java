package com.example.handheld.conexionDB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.example.handheld.modelos.PedidoModelo;
import com.example.handheld.modelos.PersonaModelo;
import com.example.handheld.modelos.TipotransModelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Conexion {

    @SuppressLint("NewApi")
    public Connection conexionBD(String dbname, Context Context){
        Connection cnn = null;
        String ip="10.10.10.246", port="1433", username = "Practicante.sistemas", password = "+Psis.*";
        String databasename = dbname;
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String connectionUrl= "jdbc:jtds:sqlserver://"+ip+":"+port+";databasename="+databasename+";User="+username+";password="+password+";";
            cnn = DriverManager.getConnection(connectionUrl);
        }catch (Exception e){
            Toast.makeText(Context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cnn;
    }

    public String obtenerPersona(Context context, String cedula){
        PersonaModelo persona;
        String nombre = null;

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT nombres, nit FROM V_nom_personal_Activo_con_maquila " +
                    "WHERE nit = '" + cedula + "'");
            if (rs.next()){
                persona = new PersonaModelo(rs.getString("nombres"), rs.getString("nit"));
                nombre = persona.getNombres();
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return nombre;
    }

    public String obtenerIdAlamImport(Context context, String sql){
        String id = null;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                id = rs.getString("id");
            }

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    public String obtenerPesoAlamImport(Context context, String sql){
        String peso = "";

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                peso = rs.getString("peso");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return peso;
    }

    public String obtenerCodigoAlamImport(Context context, String sql){
        String codigo = null;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                codigo = rs.getString("codigo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return codigo;
    }

    public String obtenerConsecutivo(Context context, String sql){
        String numero = "";

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                numero = rs.getString("consecutivo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return numero;
    }

    public String obtenerCostoUnit(Context context, String sql){
        String costo_kilo = null;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                costo_kilo = rs.getString("costo_kilo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return costo_kilo;
    }

    public String obtenerDescripcionCodigo(Context context, String sql){
        String descripcion = "";

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                descripcion = rs.getString("descripcion");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return descripcion;
    }

    public String obtenerCantidadPedido(Context context, String sql){
        String cantidad = null;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                cantidad = rs.getString("pendiente");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return cantidad;
    }

    public String obtenerNumTranAlamImport(Context context, String sql){
        String numImport = "";

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                numImport = rs.getString("num_importacion");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return numImport;
    }

    public boolean existeCodigo(Context context, String codigo){
        String Pcodigo = null;
        boolean resp = false;

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT codigo FROM referencias WHERE codigo = '" + codigo + "'");
            if (rs.next()){
                Pcodigo = rs.getString("codigo");
                if (!Pcodigo.equals("")){
                    resp = true;
                }
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resp;
    }

    public boolean existeTipoTransaccion(Context context, String tipoSpinner){
        String tipo = null;
        boolean resp = false;

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT tipo FROM tipo_transacciones WHERE tipo = '" + tipoSpinner + "'");
            if (rs.next()){
                tipo = rs.getString("tipo");
                if (!tipo.equals("")){
                    resp = true;
                }
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resp;
    }

    public ArrayList<TipotransModelo> obtenerTipos(Context context){
        ArrayList<TipotransModelo> tipos = new ArrayList<>();
        TipotransModelo Tipo;

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT T.tipo,T.sw FROM  tipo_transacciones T WHERE T.tipo = 'TRB1' ");
            while (rs.next()){
                Tipo = new TipotransModelo();
                Tipo.setTipo(rs.getString("tipo"));
                Tipo.setSw(rs.getString("sw"));
                tipos.add(Tipo);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return tipos;
    }

    public boolean eliminarTiqueteUnico(Context context, String num_importacion, String num_rollo, String nit_proveedor, String detalle){
        boolean resp = false;
        Connection connection = conexionBD("JJVPRGPRODUCCION", context);
        try {
            if (connection != null){
                PreparedStatement stm = conexionBD("JJVPRGPRODUCCION", context).prepareStatement("DELETE FROM  J_alambron_importacion_det_rollos WHERE num_importacion =" + num_importacion + " AND numero_rollo = " + num_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + detalle);
                stm.executeQuery();
                resp = true;
                Toast.makeText(context, "Tiquete borrado", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resp;
    }

    public List<PedidoModelo> obtenerPedidos(Context context){
        List<PedidoModelo> pedidos = new ArrayList<>();
        PedidoModelo modelo;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT E.numero,E.fecha,D.codigo,(D.cantidad - (SELECT COUNT(numero) FROM J_salida_alambron_transaccion  WHERE numero = D.numero AND id_detalle = D.id_detalle))As pendiente,R.descripcion \n" +
                    "                               FROM J_salida_alambron_enc E ,J_salida_alambron_det D, CORSAN.dbo.referencias R \n" +
                    "                                  WHERE E.anulado is null AND  R.codigo = D.codigo AND D.numero = E.numero AND (D.cantidad - (SELECT COUNT(numero) FROM J_salida_alambron_transaccion  WHERE numero = D.numero AND id_detalle = D.id_detalle)) > 0 AND (e.devolver = 'N' OR e.devolver IS NULL ) \n" +
                    "                                    ORDER BY E.fecha");
            while (rs.next()){
                modelo = new PedidoModelo();
                modelo.setNumero(rs.getString("numero"));
                modelo.setFecha(rs.getString("fecha"));
                modelo.setCodigo(rs.getString("codigo"));
                modelo.setPendiente(rs.getString("pendiente"));
                modelo.setDescripcion(rs.getString("descripcion"));
                pedidos.add(modelo);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return pedidos;
    }


}
