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
import com.example.handheld.modelos.CajasReceModelo;

import java.util.List;

public class listCajasRecepAdapter extends ArrayAdapter<CajasReceModelo> {

    private final List<CajasReceModelo> mlist;
    private final Context mContext;
    private final int resourceLayout;

    public listCajasRecepAdapter(@NonNull Context context, int resource, List<CajasReceModelo> objects) {
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

        CajasReceModelo modelo = mlist.get(position);

        TextView textorefe = view.findViewById(R.id.txtReferencias);
        textorefe.setText(modelo.getReferencia());

        TextView textodes = view.findViewById(R.id.txtDescripRefe);
        textodes.setText(modelo.getDescripcion());

        TextView textogenerico = view.findViewById(R.id.txt_tncajas);
        textogenerico.setText(modelo.getGenerico());

        TextView textocant = view.findViewById(R.id.txtNCajas);
        textocant.setText(modelo.getCantidad());


        return view;
    }
}
