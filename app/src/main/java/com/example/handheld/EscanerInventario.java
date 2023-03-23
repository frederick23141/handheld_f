package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLException;

public class EscanerInventario extends AppCompatActivity {

    objOperacionesDb objOperacionesDb = new objOperacionesDb();

    Button btnTransaInve;

    Integer id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaner_inventario);

        btnTransaInve = findViewById(R.id.btnTransaInve);

        btnTransaInve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id>0){
                    try {
                        terminar_inventario();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void terminar_inventario() throws SQLException {
        String sql = "UPDATE J_inventario_enc SET fecha_terminado = GETDATE() WHERE id =" + id;
        if (objOperacionesDb.ejecutarUpdateDbProduccion(sql,EscanerInventario.this)>0){
            Toast.makeText(this, "Holaaa", Toast.LENGTH_SHORT).show();
        }
    }
}