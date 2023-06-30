package com.example.handheld.ClasesOperativas;

import android.content.Context;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Ing_prod_ad {
    /*
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

    }*/
    public Boolean ExecuteSqlTransaction(List<Object> listSql , String db, Context context) {
        boolean resp = false;
        String ip="10.10.10.246", port="1433", username = "Practicante.sistemas", password = "+Psis.*";
        String connectionUrl= "jdbc:jtds:sqlserver://"+ip+":"+port+";databasename="+ db +";User="+username+";password="+password+";";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection con = DriverManager.getConnection(connectionUrl)) {
                con.setAutoCommit(false);

                try (Statement stmt = con.createStatement()) {
                    for (Object consultaSQL : listSql) {
                        stmt.executeUpdate((String) consultaSQL);
                    }
                    con.commit();
                    return true;
                } catch (SQLException ex) {
                    con.rollback();
                    throw ex;
                }
            } catch (SQLException ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        int timeoutSeconds = 60; // Establecer el tiempo de espera en 1 minuto y medio (90 segundos)
        try {
            resp = future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            Toast.makeText(context, "La transacción ha excedido el tiempo límite.", Toast.LENGTH_LONG).show();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }

        return resp;

    }
    /* Se comenta el codigo que se utilizo para hacer que se genere el error y hacer la prueba
    public Boolean ExecuteSqlTransaction(List<Object> listSql, String db, Context context) {
        boolean resp = false;
        String ip = "10.10.10.246", port = "1433", username = "Practicante.sistemas", password = "+Psis.*";
        String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + db + ";User=" + username + ";password=" + password + ";";

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(() -> {
            try (Connection con = DriverManager.getConnection(connectionUrl)) {
                con.setAutoCommit(false);

                try (Statement stmt = con.createStatement()) {
                    for (Object consultaSQL : listSql) {
                        stmt.executeUpdate((String) consultaSQL);
                        sleep(90000); // Retraso de 90 segundos entre consultas
                    }
                    con.commit();
                    return true;
                } catch (SQLException ex) {
                    con.rollback();
                    throw ex;
                }
            } catch (SQLException ex) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
        });

        int timeoutSeconds = 60; // Tiempo de espera de 60 segundos
        try {
            resp = future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            Toast.makeText(context, "La transacción ha excedido el tiempo límite.", Toast.LENGTH_LONG).show();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }

        return resp;
    }

    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

}
