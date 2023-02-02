package com.example.handheld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.example.handheld.conexionDB.Conexion;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ObjTraslado_bodLn {

    Obj_ordenprodLn obj_ordenprodLn = new Obj_ordenprodLn();
    Conexion conexion = new Conexion();

    public List<Object> listaTransaccionDatable_traslado_bodega(String num, String cod, String bod_orig, String bod_dest, Calendar dFec, String notas, String usuario, String cantidad, String tipo, String modelo, String costo_kilo, Context context){
        String sql = "";
        String sql_lin_salida = "";
        String sql_lin_entrada = "";
        int seq = 0;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha_hora = dateFormat.format(dFec.getTime());
        List<Object> listSql = new ArrayList<>();;
        String nit = "890900160";
        double vrTotal = Double.parseDouble(costo_kilo) * Double.parseDouble(cantidad);
        Double costo_total = Double.parseDouble(costo_kilo) * Double.parseDouble(cantidad);
        int vendedor = 0;
        String pc = Build.BRAND +"-"+ fecha_hora +"-"+ Build.MODEL +"-"+ usuario;
        String sFecha_hora = "";
        String sFecha = "";
        int swDoc = 16;
        int swDoc_lin = 0;
        Boolean inserto = false;
        double vr_unitario = Double.parseDouble(costo_kilo);

        if (obj_ordenprodLn.insertarProxMes(cod,context)){
            dFec.add(Calendar.MONTH, 1);

            //Capturamos el mes en un String
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatMoth = new SimpleDateFormat("MM");
            String month = dateFormatMoth.format(dFec.getTime());

            //Capturamos el año en un String
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
            String year = dateFormatYear.format(dFec.getTime());

            sFecha_hora = year + "-" + month + "-01";

            //Capturamos las horas
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatHour = new SimpleDateFormat("HH");
            String hour = dateFormatHour.format(dFec.getTime());

            //Capturamos los minutos
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatMinute = new SimpleDateFormat("mm");
            String minute = dateFormatMinute.format(dFec.getTime());

            //Capturamos los segundos
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatSeconds = new SimpleDateFormat("ss");
            String seconds = dateFormatSeconds.format(dFec.getTime());

            fecha_hora = "" + month + "-" + "01" + "-" + year + " " + hour + ":" + minute + ":" + seconds + "";

            sFecha = sFecha_hora;

        }else{
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatsFecha_hora = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            sFecha_hora = dateFormatsFecha_hora.format(calendar.getTime());

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatfecha_hora = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            fecha_hora = dateFormatfecha_hora.format(calendar.getTime());


            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatsFecha = new SimpleDateFormat("yyyy-MM-dd");
            sFecha = dateFormatsFecha.format(calendar.getTime());
        }

        if (existe_referencias_sto(cod, java.lang.Integer.valueOf(bod_dest), dFec, context).equals(false)){
            try {
                listSql.add(crear_referencias_sto(cod, Integer.valueOf(bod_dest), dFec));
            }catch (Exception e){
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        sql = "INSERT INTO  documentos (sw,tipo,numero,nit,fecha,vencimiento,valor_total,vendedor,valor_aplicado" +
                ",anulado,modelo,notas ,usuario,pc,fecha_hora,bodega,duracion,concepto ,centro_doc,spic) VALUES " +
                "(" + swDoc + ",'" + tipo + "'," + num + "," + nit + ",'" + sFecha + "','" + sFecha + "'," +
                "" + vrTotal + "," + vendedor + "," + 0 + ",0 ,'" + modelo + "','" + notas + "','" + usuario + "" +
                "','" + pc + "','" + sFecha_hora + "'," + bod_orig + ",15,0,0,'S') ";

        try {
            listSql.add(sql);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //'******************* ----------Se adiciona la salida

        seq = 1;
        swDoc_lin = 16;

        sql = "INSERT INTO documentos_lin(sw,tipo,numero,codigo,seq,fec,nit,cantidad,porcentaje_iva," +
                "valor_unitario,porcentaje_descuento,costo_unitario,adicional,vendedor,bodega,und," +
                "cantidad_und,cantidad_pedida,maneja_inventario,costo_unitario_sin,cantidad_dos) " +
                "VALUES(" + swDoc_lin + ",'" + tipo + "'," + num + ",'" + cod + "'," + seq + "," +
                "'" + sFecha + "'," + nit + "," + cantidad + ",16," + vr_unitario + ",0," + costo_kilo + "," +
                "'" + notas + "',0," + bod_orig + ",'UND',1,0,'S',0.0000000000000000,1) ";

        try {
            listSql.add(sql);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //'Script para ingresar a referencias_sto (STOCK)
        listSql.add(actualizarRefSto(Double.parseDouble(cantidad), Double.parseDouble(costo_kilo), cod, dFec, Integer.parseInt(bod_orig), swDoc_lin));
        listSql.add(sqlActUltEntradaUltSalida(swDoc_lin, cod));


        //'******************* ----------Se adiciona la Entrada
        vr_unitario = 0;
        seq = 2;
        swDoc_lin = 12;
        sql = "INSERT INTO documentos_lin(sw,tipo,numero,codigo,seq,fec,nit,cantidad,porcentaje_iva,valor_unitario,porcentaje_descuento ," +
                "costo_unitario,adicional,vendedor,bodega,und,cantidad_und,cantidad_pedida,maneja_inventario,costo_unitario_sin,cantidad_dos) " +
                "VALUES(" + swDoc_lin + ",'" + tipo + "'," + num + ",'" + cod + "'," + seq + ",'" + sFecha + "'," + nit + "," + cantidad +
                ",16," + vr_unitario +",0, " + costo_kilo + ",'" + notas + "',0," + bod_dest + ",'UND',1,0,'S',0.0000000000000000,1) ";

        try {
            listSql.add(sql);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        //'Script para ingresar a referencias_sto (STOCK)
        String actRef = actualizarRefSto(Double.parseDouble(cantidad), Double.parseDouble(costo_kilo), cod, dFec, Integer.parseInt(bod_dest), swDoc_lin);
        listSql.add(actRef);
        String actUlt = sqlActUltEntradaUltSalida(swDoc_lin, cod);
        listSql.add(actUlt);

        return (listSql);
    }

    public String sqlActUltEntradaUltSalida(Integer swTipo, String codigo){
        String sql = "";
        if (swTipo == 11 || swTipo == 16){
            sql = "UPDATE referencias SET fec_ultima_salida = GETDATE () WHERE codigo = '" + codigo + "'";
        }else if (swTipo == 12 || swTipo == 3){
            sql = "UPDATE referencias SET fec_ultima_entrada = GETDATE () WHERE codigo = '" + codigo + "'";
        }
        return sql;
    }



    private String actualizarRefSto(Double kilos , Double costUnit, String codigo, Calendar dFec, Integer bodega, Integer swTipo){
        String sql = "";
        //DecimalFormat df = new DecimalFormat("#0.0");
        Double pes = kilos;
        kilos = pes;

        //Capturamos el mes en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatMoth = new SimpleDateFormat("MM");
        String month = dateFormatMoth.format(dFec.getTime());

        //Capturamos el año en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        String year = dateFormatYear.format(dFec.getTime());

        //'sw 11 resta(salida) , sw 12 suma(entrada)
        if (swTipo == 11 || swTipo == 16){
            sql = "UPDATE referencias_sto SET can_sal += " + kilos + " , cos_sal +=" + costUnit * kilos + " " +
                    ", cos_otr_sal +=" + costUnit * kilos + " , can_otr_sal += " + kilos + " , nro_com = " +
                    "(CASE WHEN nro_com is null  THEN 1 ELSE nro_com + 1 END ) WHERE bodega = " + bodega + " " +
                    "AND codigo ='" + codigo + "' AND mes = " + month + " and ano = " + year + " ";
        }else if (swTipo == 12 || swTipo == 3){
            sql = "UPDATE referencias_sto SET can_ent = (CASE WHEN can_ent is null  THEN " + kilos + " " +
                    "ELSE can_ent +" + kilos + " END ), cos_ent = (CASE WHEN cos_ent is null  " +
                    "THEN " + costUnit * kilos + " ELSE cos_ent +" + costUnit * kilos + " END ), " +
                    "can_com = (CASE WHEN can_com is null  THEN " + kilos + " ELSE can_com +" + kilos + " " +
                    "END ), cos_com = (CASE WHEN cos_com is null  THEN " + costUnit * kilos + " " +
                    "ELSE cos_com +" + costUnit * kilos + " END ), nro_com = (CASE WHEN nro_com is null  " +
                    "THEN 1 ELSE nro_com + 1 END )  WHERE bodega = " + bodega + " AND codigo ='" + codigo + "' " +
                    "AND mes = " + month + " and ano = " + year + " ";
        }
        return sql;
    }

    private Boolean existe_referencias_sto(String codigo, Integer bodega, Calendar fec, Context context){
        boolean resp = false;

        //Capturamos el mes en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatMoth = new SimpleDateFormat("MM");
        String month = dateFormatMoth.format(fec.getTime());

        //Capturamos el año en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        String year = dateFormatYear.format(fec.getTime());

        String sql = "SELECT codigo FROM referencias_sto WHERE codigo = '" + codigo + "' AND mes = " + month + " AND ano = " + year + " AND bodega = " + bodega + " ";

        if (!conexion.obtenerCodigoReferencias(context, sql).equals("")){
            resp = true;
        }

        return resp;
    }

    private String crear_referencias_sto(String codigo , Integer bodega, Calendar fec){
        //Capturamos el mes en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatMoth = new SimpleDateFormat("MM");
        String mes = dateFormatMoth.format(fec.getTime());

        //Capturamos el año en un String
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        String ano = dateFormatYear.format(fec.getTime());

        String sql = "INSERT INTO referencias_sto (bodega, codigo, ano, mes, can_ini, can_ent, can_sal, cos_ini, cos_ent, cos_sal, can_vta, cos_vta, val_vta, can_dev_vta, cos_dev_vta, val_dev, can_com, cos_com, can_dev_com, cos_dev_com, can_otr_ent, cos_otr_ent, can_otr_sal, cos_otr_sal, can_tra, cos_tra, sub_cos, baj_cos, nro_vta, nro_dev_vta, nro_com, nro_dev_com, nro_ped, can_ped, cos_ini_aju, cos_ent_aju, cos_sal_aju) VALUES (" + bodega + ", '" + codigo + "', " + ano + ", " + mes + ", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)";

        return sql;
    }
}
