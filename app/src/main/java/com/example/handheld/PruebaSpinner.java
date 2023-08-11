package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PruebaSpinner extends AppCompatActivity {

    Spinner spinner;
    ArrayList<String> listaTrefiRechazos;
    ArrayList<String> listaRechazos;
    TextView txtTraccion;
    EditText editTraccion;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_spinner);
        spinner = findViewById(R.id.spinnerRechazo);
        txtTraccion = findViewById(R.id.editTraccion);
        editTraccion = findViewById(R.id.editTraccion);

        listaTrefiRechazos = llenarlistaspinner();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter( PruebaSpinner.this, android.R.layout.simple_spinner_item, listaTrefiRechazos);
        spinner.setAdapter(adapter);


    }

    private ArrayList<String> llenarlistaspinner(){
        listaRechazos = new ArrayList<>();

        listaRechazos.add("Seleccione motivo rechazo");
        listaRechazos.add("Piel de naranja");
        listaRechazos.add("Baja/Alta tracción");
        listaRechazos.add("Poroso");
        listaRechazos.add("Daño por montacarga");
        listaRechazos.add("Rayado");
        listaRechazos.add("Tallado");
        listaRechazos.add("Oxidación");

        return listaRechazos;
    }

}