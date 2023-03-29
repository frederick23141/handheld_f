package com.example.handheld.atv.holder.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.handheld.R;
import com.example.handheld.modelos.GalvRecepcionModelo;
import com.example.handheld.modelos.PedidoModelo;

import java.util.List;

public class listGalvTerminadoAdapter extends ArrayAdapter<GalvRecepcionModelo> {
    private final List<GalvRecepcionModelo> mlist;
    private final Context mContext;
    private final int resourceLayout;

    public listGalvTerminadoAdapter(@NonNull Context context, int resource, List<GalvRecepcionModelo> objects) {
        super(context, resource, objects);
        this.mlist = objects;
        this.mContext = context;
        this.resourceLayout = resource;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view = LayoutInflater.from(mContext).inflate(resourceLayout, null);

        GalvRecepcionModelo modelo = mlist.get(position);

        LinearLayout linearLayout = view.findViewById(R.id.ll_galva);

        String color = modelo.getColor();
        if (color.equals("RED")){
            linearLayout.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        }else{
            linearLayout.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }

        TextView textonro_orden = view.findViewById(R.id.txtnro_orden);
        textonro_orden.setText(modelo.getNro_orden());

        TextView textonro_rollo = view.findViewById(R.id.txtnro_rollo);
        textonro_rollo.setText(modelo.getNro_rollo());

        TextView textoreferencia = view.findViewById(R.id.txtreferencia);
        textoreferencia.setText(modelo.getReferencia());

        TextView textopeso = view.findViewById(R.id.txtpeso);
        textopeso.setText(modelo.getPeso());

        TextView texto_descripcion = view.findViewById(R.id.txt_descripcion);
        texto_descripcion.setText(modelo.getDescripcion());

        return view;
    }
}
