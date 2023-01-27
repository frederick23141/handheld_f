package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.atv.model.TreeNode;
import com.example.handheld.atv.view.AndroidTreeView;
import com.example.handheld.conexionDB.Conexion;
import com.example.handheld.modelos.PedidoModelo;
import com.example.handheld.modelos.PersonaModelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText cedula;
    ImageButton consultar;
    String nombre_usuario;
    TextView mensaje;
    Conexion conexion;
    String cd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cedula = (EditText)findViewById(R.id.txtcedula);
        consultar = (ImageButton)findViewById(R.id.btnBuscarPersona);
        mensaje = (TextView)findViewById(R.id.txtMensaje);
        conexion = new Conexion();

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar()){
                    cd = cedula.getText().toString();
                    nombre_usuario = conexion.obtenerPersona(getApplicationContext(),cd );

                    if(nombre_usuario == null){
                        //Toast.makeText(this, "Persona no encontrada", Toast.LENGTH_SHORT).show();
                        toastError("Persona no encontrada");
                    }else{
                        mensaje.setText("Bienvenido " + nombre_usuario);
                        agregarTreeview();
                        consultar.setEnabled(false);
                        cedula.setEnabled(false);
                    }

                }else{
                    toastEscribir("Por favor escribir tu cedula");
                }
            }
        });

    }

    public boolean validar(){
        Boolean retorno = true;

        String text = cedula.getText().toString();
        if(text.isEmpty()){
            retorno = false;
        }
        return retorno;
    }

    //METODO DE TOAST PERSONALIZADO : PERSONA NO ENCONTRADA
    public void toastError(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_per_no_encon, (ViewGroup) findViewById(R.id.ll_custom_toast_per_no_encon));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,0,200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    //METODO DE TOAST PERSONALIZADO : ESCRIBIR CEDULA
    public void toastEscribir(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_escribir_cedula, (ViewGroup) findViewById(R.id.ll_custom_toast_escribir_cedula));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    public void agregarTreeview(){
        //Root
        TreeNode root = TreeNode.root();

        //Parent
        //MyHolder.IconTreeItem nodeItem = new MyHolder.IconTreeItem(R.drawable.ic_arrow_drop_down, "Parent");
        //TreeNode parent = new TreeNode(nodeItem).setViewHolder(new MyHolder(getApplicationContext(), true, MyHolder.DEFAULT, MyHolder.DEFAULT));

        //Grupo"Gestion de Alambron"
        MyHolder.IconTreeItem childItem1 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Gestion de Alambron");
        TreeNode child1 = new TreeNode(childItem1).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

        //SubGrupo1"Gestion de Alambron"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem1_1 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Traslado de Bodega (1-2)");
        TreeNode subChild1_1 = new TreeNode(subChildItem1_1).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        subChild1_1.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                Intent i = new Intent(MainActivity.this,Pedido.class);
                i.putExtra("nit_usuario",cd);
                i.putExtra("bod_origen","1");
                i.putExtra("bod_destino","2");
                i.putExtra("modelo","08");

                startActivity(i);
            }
        });

        //Subgrupo2"Gestion de Alambron"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem1_2 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Traslado de Bodega (2-1)");
        TreeNode subChild1_2 = new TreeNode(subChildItem1_2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //Agregamos subgrupo1"Gestion de Alambron".
        child1.addChild(subChild1_1);

        //Agregamos subgrupo2"Gestion de Alambron".
        child1.addChild(subChild1_2);

        //Agregamos Grupo"Gestion de Alambron".
        //parent.addChildren(child1);
        root.addChild(child1);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //Grupo"Gestion de Gestion Galvanizado"
        MyHolder.IconTreeItem childItem2 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Gestion Galvanizado");
        TreeNode child2 = new TreeNode(childItem2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

        //SubGrupo1"Gestion Galvanizado"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem2_1 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Traslado de Bodega (2-11)");
        TreeNode subChild2_1 = new TreeNode(subChildItem2_1).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //SubGrupo2"Gestion Galvanizado"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem2_2 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Traslado de Bodega (11-2)");
        TreeNode subChild2_2 = new TreeNode(subChildItem2_2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //Agregamos subgrupo1"Gestion Galvanizado".
        child2.addChild(subChild2_1);

        //Agregamos subgrupo2"Gestion Galvanizado".
        child2.addChild(subChild2_2);

        //Agregamos Grupo"Gestion Galvanizado".
        //parent.addChildren(child1);
        root.addChild(child2);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //Grupo"Gestion de Gestion Puas"
        MyHolder.IconTreeItem childItem3 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Gestion Puas");
        TreeNode child3 = new TreeNode(childItem3).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

        //SubGrupo1"Gestion Puas"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem3_1 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Entrega de Materia Prima Puas");
        TreeNode subChild3_1 = new TreeNode(subChildItem3_1).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //SubGrupo2"Gestion Puas"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem3_2 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Devolver materia prima");
        TreeNode subChild3_2 = new TreeNode(subChildItem3_2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //Agregamos subgrupo1"Gestion Puas".
        child3.addChild(subChild3_1);

        //Agregamos subgrupo2"Gestion Puas".
        child3.addChild(subChild3_2);

        //Agregamos Grupo"Gestion Puas".
        //parent.addChildren(child1);
        root.addChild(child3);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //Grupo"Gestion Puntilleria"
        MyHolder.IconTreeItem childItem4 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Gestion Puntilleria");
        TreeNode child4 = new TreeNode(childItem4).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

        //SubGrupo1"Gestion Puntilleria"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem4_1 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Entrega de Materia Prima ");
        TreeNode subChild4_1 = new TreeNode(subChildItem4_1).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //SubGrupo2"Gestion Puntilleria"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem4_2 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Devoluciones Puntilleria");
        TreeNode subChild4_2 = new TreeNode(subChildItem4_2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //Agregamos subgrupo1"Gestion Puntilleria".
        child4.addChild(subChild4_1);

        //Agregamos subgrupo2"Gestion Puntilleria".
        child4.addChild(subChild4_2);

        //Agregamos Grupo"Gestion Puntilleria".
        //parent.addChildren(child1);
        root.addChild(child4);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //Grupo"Otros"
        MyHolder.IconTreeItem childItem5 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Otros");
        TreeNode child5 = new TreeNode(childItem5).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

        //Agregamos Grupo"Otros".
        //parent.addChildren(child1);
        root.addChild(child5);

        //Agregamos AndroidTreeView en la vista.
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
        ((LinearLayout) findViewById(R.id.ll_parent)).addView(tView.getView());
    }
}