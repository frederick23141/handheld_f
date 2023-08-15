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
import com.example.handheld.modelos.DetalleTranModelo;

import java.util.List;

public class listescanerAdapter extends ArrayAdapter<DetalleTranModelo> {

    private final List<DetalleTranModelo> mlist;
    private final Context mContext;
    private final int resourceLayout;

    public listescanerAdapter(@NonNull Context context, int resource, List<DetalleTranModelo> objects) {
        super(context, resource, objects);
        this.mlist = objects;
        this.mContext = context;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view = LayoutInflater.from(mContext).inflate(resourceLayout, null);

        DetalleTranModelo modelo = mlist.get(position);

        TextView textonumero = view.findViewById(R.id.txtnumero);
        textonumero.setText(modelo.getNumero());

        TextView textotipo = view.findViewById(R.id.txtTipo);
        textotipo.setText(modelo.getTipo());

        TextView textonumtrans = view.findViewById(R.id.txtNumtrans);
        textonumtrans.setText(modelo.getNum_trans());

        TextView textocodigo = view.findViewById(R.id.txtCodigo);
        textocodigo.setText(modelo.getCodigo());

        TextView textopeso = view.findViewById(R.id.txtPeso);
        textopeso.setText(modelo.getPeso());

        TextView textonumimp = view.findViewById(R.id.txtNumimp);
        textonumimp.setText(modelo.getNum_imp());

        TextView textodetalle = view.findViewById(R.id.txtDetalle);
        textodetalle.setText(modelo.getDetalle());

        TextView textonumrollo = view.findViewById(R.id.txtNumrollo);
        textonumrollo.setText(modelo.getNum_rollo());

        TextView textoestado = view.findViewById(R.id.txtEstado);
        textoestado.setText(modelo.getEstado_muestra());

        TextView textonit = view.findViewById(R.id.txtNit);
        textonit.setText(modelo.getNit_prov());

        TextView textocost = view.findViewById(R.id.txtCost);
        textocost.setText(modelo.getCosto_unit());

        return view;
    }
}
