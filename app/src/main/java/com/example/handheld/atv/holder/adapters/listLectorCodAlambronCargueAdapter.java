package com.example.handheld.atv.holder.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.handheld.R;
import com.example.handheld.modelos.LectorCodAlambronModelo;

import java.util.List;

public class listLectorCodAlambronCargueAdapter extends ArrayAdapter<LectorCodAlambronModelo> {

    private final List<LectorCodAlambronModelo> mListLectorCodAlambronModelo;
    private final Context mContextLectorCodAlambron;
    private final int resourceLayout;
    public listLectorCodAlambronCargueAdapter(@NonNull Context context, int resource, List<LectorCodAlambronModelo> objects) {
        super(context, resource, objects);
        this.mListLectorCodAlambronModelo=objects;
        this.mContextLectorCodAlambron=context;
        this.resourceLayout=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;

        if(view==null)
            view= LayoutInflater.from(mContextLectorCodAlambron).inflate(resourceLayout,null);

        LectorCodAlambronModelo modelo =mListLectorCodAlambronModelo.get(position);


        TextView textonumero = view.findViewById(R.id.txtnumero);
        textonumero.setText(modelo.getNumero_transaccion());

        TextView TextoIdRequisicion = view.findViewById(R.id.txtNumtrans);
        TextoIdRequisicion.setText(modelo.getNumero_transaccion());


        TextView textocodigo = view.findViewById(R.id.txtCodigo);
        textocodigo.setText(modelo.getCodigoalambron());

        TextView textopeso = view.findViewById(R.id.txtPeso);
        textopeso.setText(modelo.getPesoAlambron());

        TextView textonumimp = view.findViewById(R.id.txtNumimp);
        textonumimp.setText(modelo.getNum_imp());

        TextView textodetalle = view.findViewById(R.id.txtDetalle);
        textodetalle.setText(modelo.getDetalle());

        TextView textonumrollo = view.findViewById(R.id.txtNumrollo);
        textonumrollo.setText(modelo.getNum_rolloAlambron());

        TextView textoestado = view.findViewById(R.id.txtEstado);
        textoestado.setText(modelo.getEstado_muestra());

        TextView textonit = view.findViewById(R.id.txtNit);
        textonit.setText(modelo.getNit_proveedor());

        TextView textocost = view.findViewById(R.id.txtCost);
        textocost.setText(modelo.getCosto_unitario_alambron());


            return view;

    }
}
