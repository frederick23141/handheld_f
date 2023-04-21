package com.example.handheld;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Ing_prod_ad {
    public Boolean ExecuteSqlTransaction(List<Object> listSql , String db, Context context) {
        boolean resp = false;
        String ip="10.10.10.246", port="1433", username = "Practicante.sistemas", password = "+Psis.*";
        String connectionUrl= "jdbc:jtds:sqlserver://"+ip+":"+port+";databasename="+ db +";User="+username+";password="+password+";";

        try (Connection con = DriverManager.getConnection(connectionUrl)) {
            con.setAutoCommit(false);

            try (Statement stmt = con.createStatement()) {
                for (Object consultaSQL : listSql) {
                    stmt.executeUpdate((String) consultaSQL);
                }
                con.commit();
                resp = true;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return resp;
    }

}
