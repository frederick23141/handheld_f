package com.example.handheld.conexionDB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import com.example.handheld.modelos.CajasReceModelo;
import com.example.handheld.modelos.CentrosModelo;
import com.example.handheld.modelos.GalvRecepcionModelo;
import com.example.handheld.modelos.GalvRecepcionadoRollosModelo;
import com.example.handheld.modelos.InventarioModelo;
import com.example.handheld.modelos.PedidoModelo;
import com.example.handheld.modelos.PersonaModelo;
import com.example.handheld.modelos.RolloterminadoModelo;
import com.example.handheld.modelos.TipotransModelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Conexion {

    @SuppressLint("NewApi")
    public Connection conexionBD(String dbname, Context Context){
        Connection cnn = null;
        String ip="10.10.10.246", port="1433", username = "Practicante.sistemas", password = "+Psis.*";
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            String connectionUrl= "jdbc:jtds:sqlserver://"+ip+":"+port+";databasename="+ dbname +";User="+username+";password="+password+";";
            cnn = DriverManager.getConnection(connectionUrl);
        }catch (Exception e){
            Toast.makeText(Context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return cnn;
    }

    //Obtiene datos de una persona en la BD
    public PersonaModelo obtenerPersona(Context context, String cedula){
        PersonaModelo persona = null;

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT nombres, nit FROM V_nom_personal_Activo_con_maquila " +
                    "WHERE nit = '" + cedula + "'");
            if (rs.next()){
                persona = new PersonaModelo(rs.getString("nombres"), rs.getString("nit"));
            }else{
                persona = new PersonaModelo("", "");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return persona;
    }

    //Obtiene un dato
    public String obtenerIdAlamImport(Context context, String sql){
        String id = "";

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                id = rs.getString("id");
            }

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    public Integer obtenerIdInv(Context context, String sql){
        int id = 0;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                id = Integer.parseInt(rs.getString("id"));
            }

        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    //Obtiene un dato
    public String obtenerPesoAlamImport(Context context, String sql){
        String peso = "";

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                peso = rs.getString("peso");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return peso;
    }

    //Obtiene un dato
    public String obtenerCodigoAlamImport(Context context, String sql){
        String codigo = null;

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                codigo = rs.getString("codigo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return codigo;
    }

    //Obtiene un dato
    public String obtenerCodigo(Context context, String sql){
        String codigo = null;

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                codigo = rs.getString("codigo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return codigo;
    }

    //Obtiene un dato
    public String consultarStock(Context context,String codigo, String bodega){
        String Stock = null;

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT stock,bodega FROM v_referencias_sto_hoy WHERE codigo = '" + codigo + "' and bodega = " + bodega + " ");
            if (rs.next()){
                Stock = rs.getString("stock");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return Stock;
    }

    //Obtiene un dato
    public String obtenerConsecutivo(Context context, String sql){
        String numero = "";

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                numero = rs.getString("numero");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return numero;
    }

    //Obtiene un dato
    public String obtenerDescripcionCodigo(Context context, String sql){
        String descripcion = "";

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                descripcion = rs.getString("descripcion");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return descripcion;
    }

    //Obtiene un dato
    public String obtenerCodigoReferencias(Context context, String sql){
        String codigo = "";

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                codigo = rs.getString("codigo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return codigo;
    }

    //Obtiene un dato
    public String obtenerDescripciónReferencias(Context context, String referencia){
        String descripcion = "";

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery("select descripcion from referencias where codigo='"+ referencia +"' and ref_anulada = 'N'");
            if (rs.next()){
                descripcion = rs.getString("descripcion");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return descripcion;
    }

    //Obtiene un dato
    public String obtenerMes(Context context, String sql){
        String mes = "";

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                mes = rs.getString("mes");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return mes;
    }

    //Obtiene un dato
    public String obtenerCostoUnit(Context context, String sql){
        String costo_kilo = null;

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                costo_kilo = rs.getString("costo_kilo");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return costo_kilo;
    }

    //Obtiene un dato
    public Double obtenerIvaReferencia(Context context, String cod){
        Double iva = null;

        try {
            Statement st = conexionBD("JJVDMSCIERREAGOSTO", context).createStatement();
            ResultSet rs = st.executeQuery("select porcentaje_iva from referencias where codigo = '"+ cod +"'");
            if (rs.next()){
                iva = rs.getDouble("porcentaje_iva");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return iva;
    }

    //Obtiene un dato
    public String obtenerCantidadPedido(Context context, String sql){
        String cantidad = null;

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                cantidad = rs.getString("pendiente");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return cantidad;
    }

    //Obtiene un dato
    public String obtenerNumTranAlamImport(Context context, String sql){
        String numImport = "";

        try {
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()){
                numImport = rs.getString("num_importacion");
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return numImport;
    }

    //Obtiene un dato
    public boolean existeCodigo(Context context, String codigo){
        String Pcodigo;
        boolean resp = false;

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
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

    //Obtiene un dato
    public boolean existeTipoTransaccion(Context context, String tipoSpinner){
        String tipo;
        boolean resp = false;

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
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
            Statement st = conexionBD("CORSAN", context).createStatement();
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

    public ArrayList<InventarioModelo> obtenerInven(Context context, String sql){
        ArrayList<InventarioModelo> inventarios = new ArrayList<>();
        InventarioModelo Inventario;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                Inventario = new InventarioModelo();
                Inventario.setId(rs.getString("id"));
                Inventario.setCodigo(rs.getString("codigo"));
                Inventario.setBodega(rs.getString("bodega"));
                inventarios.add(Inventario);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return inventarios;
    }

    public ArrayList<RolloterminadoModelo> obtenerRollosTerm(Context context, String sql){
        ArrayList<RolloterminadoModelo> terminados = new ArrayList<>();
        RolloterminadoModelo Terminado;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                Terminado = new RolloterminadoModelo();
                Terminado.setCod_orden(rs.getString("cod_orden"));
                Terminado.setId_detalle(rs.getString("id_detalle"));
                Terminado.setId_rollo(rs.getString("id_rollo"));
                terminados.add(Terminado);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return terminados;
    }

    public ArrayList<CentrosModelo> obtenerCentros(Context context){
        ArrayList<CentrosModelo> centros = new ArrayList<>();
        CentrosModelo Centro;

        try {
            Statement st = conexionBD("CORSAN", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT centro,(CONVERT(varchar, centro) + '--' + descripcion) AS descripcion FROM centros WHERE centro IN (2100,2200,2300,5200,6400)");
            while (rs.next()){
                Centro = new CentrosModelo();
                Centro.setCentro(rs.getString("centro"));
                Centro.setDescripcion(rs.getString("descripcion"));
                centros.add(Centro);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return centros;
    }

    public boolean eliminarTiqueteUnico(Context context, String num_importacion, String num_rollo, String nit_proveedor, String detalle){
        boolean resp = false;
        Connection connection = conexionBD("PRGPRODUCCION", context);
        try {
            if (connection != null){
                PreparedStatement stm = conexionBD("PRGPRODUCCION", context).prepareStatement("DELETE FROM  J_alambron_importacion_det_rollos WHERE num_importacion =" + num_importacion + " AND numero_rollo = " + num_rollo + " AND nit_proveedor=" + nit_proveedor + " AND id_solicitud_det =" + detalle);
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
            Statement st = conexionBD("PRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT E.numero,D.id_detalle,E.fecha,D.codigo,(D.cantidad - (SELECT COUNT(numero) FROM J_salida_alambron_transaccion  WHERE numero = D.numero AND id_detalle = D.id_detalle))As pendiente,R.descripcion \n" +
                    "                               FROM J_salida_alambron_enc E ,J_salida_alambron_det D, CORSAN.dbo.referencias R \n" +
                    "                                  WHERE E.anulado is null AND  R.codigo = D.codigo AND D.numero = E.numero AND (D.cantidad - (SELECT COUNT(numero) FROM J_salida_alambron_transaccion  WHERE numero = D.numero AND id_detalle = D.id_detalle)) > 0 AND (e.devolver = 'N' OR e.devolver IS NULL ) \n" +
                    "                                    ORDER BY E.fecha");
            while (rs.next()){
                modelo = new PedidoModelo();
                modelo.setNumero(Integer.valueOf(rs.getString("numero")));
                modelo.setIdDetalle(Integer.valueOf(rs.getString("id_detalle")));
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

    public List<GalvRecepcionModelo> obtenerGalvTerminado(Context context, String fecha_inicio, String fecha_final){
        List<GalvRecepcionModelo> galvTerminado = new ArrayList<>();
        GalvRecepcionModelo modelo;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery("SELECT R.nro_orden,R.consecutivo_rollo as nro_rollo,S.final_galv,ref.descripcion,R.peso  \n" +
                                                "FROM D_rollo_galvanizado_f R, D_orden_pro_galv_enc S,CORSAN.dbo.referencias ref,CORSAN.dbo.V_nom_personal_Activo_con_maquila ter \n" +
                                                "where R.nro_orden = S.consecutivo_orden_G And ref.codigo = S.final_galv and ter.nit=R.nit_operario AND R.fecha_hora >= '"+ fecha_inicio +"' AND  R.fecha_hora  <= '"+ fecha_final +"' and R.no_conforme is null and R.anular is null and R.recepcionado is null and S.final_galv LIKE '33G%'\n" +
                                                "order by ref.descripcion");
            while (rs.next()){
                modelo = new GalvRecepcionModelo();
                modelo.setNro_orden(rs.getString("nro_orden"));
                modelo.setNro_rollo(rs.getString("nro_rollo"));
                modelo.setReferencia(rs.getString("final_galv"));
                modelo.setDescripcion(rs.getString("descripcion"));
                modelo.setPeso(String.valueOf(rs.getInt("peso")));
                modelo.setColor("RED");
                galvTerminado.add(modelo);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return galvTerminado;
    }

    public List<CajasReceModelo> obtenerCajasRecepcionar(Context context, String sql){
        List<CajasReceModelo> cajasRecep = new ArrayList<>();
        CajasReceModelo modelo;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                modelo = new CajasReceModelo();
                modelo.setReferencia(rs.getString("REFERENCIA"));
                modelo.setCantidad(rs.getString("cantidad"));
                cajasRecep.add(modelo);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return cajasRecep;
    }

    public List<GalvRecepcionadoRollosModelo> galvRefeRecepcionados(Context context, String fecha_recepcion, String month, String year){
        List<GalvRecepcionadoRollosModelo> refeRecepcionados = new ArrayList<>();
        GalvRecepcionadoRollosModelo modelo;

        try {
            Statement st = conexionBD("JJVPRGPRODUCCION", context).createStatement();
            ResultSet rs = st.executeQuery("select sum(R.peso) as peso, (SELECT p.promedio from corsan.dbo.v_promedio p where codigo = O.final_galv and P.ano = "+ year +" and P.mes = "+ month +")  as promedio, (select costo_unitario from CORSAN.dbo.referencias R where codigo = O.final_galv) as costo_unitario , O.final_galv " +
                    "from D_rollo_galvanizado_f R inner join D_orden_pro_galv_enc O on O.consecutivo_orden_G = R.nro_orden " +
                    "where R.recepcionado is not null and R.fecha_recepcion = '"+ fecha_recepcion +"' and R.no_conforme is null and O.final_galv like '33%' " +
                    "group by O.final_galv");
            while (rs.next()){
                modelo = new GalvRecepcionadoRollosModelo();
                modelo.setPeso(rs.getDouble("peso"));
                modelo.setPromedio(rs.getDouble("promedio"));
                modelo.setCosto_unitario(rs.getDouble("costo_unitario"));
                modelo.setReferencia(rs.getString("final_galv"));
                refeRecepcionados.add(modelo);
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return refeRecepcionados;
    }


}
