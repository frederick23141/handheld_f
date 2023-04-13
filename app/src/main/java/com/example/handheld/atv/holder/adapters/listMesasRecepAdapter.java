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
import com.example.handheld.modelos.MesasModelo;

import java.util.List;

public class listMesasRecepAdapter extends ArrayAdapter<MesasModelo> {
    private final List<MesasModelo> mlist;
    private final Context mContext;
    private final int resourceLayout;

    public listMesasRecepAdapter(@NonNull Context context, int resource, List<MesasModelo> objects) {
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

        MesasModelo modelo = mlist.get(position);

        TextView textomesa = view.findViewById(R.id.txtNMesa);
        textomesa.setText(modelo.getMesa());


        return view;
    }
}
