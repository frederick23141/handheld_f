package com.example.handheld;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handheld.atv.model.TreeNode;
import com.example.handheld.atv.view.AndroidTreeView;
import com.example.handheld.conexionDB.Conexion;


public class MainActivity extends AppCompatActivity {

    //Se declaran los elementos del layout
    EditText cedula;
    ImageButton consultar;
    TextView mensaje;

    //Se declara un objeto conexion
    Conexion conexion;

    //Se declaran variables necesarias
    String nombre_usuario;
    String cd;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se inicializan los elementos del layout
        cedula = findViewById(R.id.txtcedula);
        consultar = findViewById(R.id.btnBuscarPersona);
        mensaje = findViewById(R.id.txtMensaje);

        //Se inicializa el objeto conexión
        conexion = new Conexion();

        //Se programa el boton consultar
        consultar.setOnClickListener(view -> {
            if(validar()){
                cd = cedula.getText().toString();
                nombre_usuario = conexion.obtenerPersona(MainActivity.this,cd );

                if(nombre_usuario == null){
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
        });
    }

    //Metodo que valida que el campo EditText "cedula" no este vacia
    public boolean validar(){
        boolean retorno = true;

        String text = cedula.getText().toString();
        if(text.isEmpty()){
            retorno = false;
        }
        return retorno;
    }

    //METODO DE TOAST PERSONALIZADO : PERSONA NO ENCONTRADA
    public void toastError(String msg){
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_per_no_encon,  findViewById(R.id.ll_custom_toast_per_no_encon));
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
        View view = layoutInflater.inflate(R.layout.custom_toast_escribir_cedula, findViewById(R.id.ll_custom_toast_escribir_cedula));
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

        //Al darle clic a este elemento en el treeview se abrira una nueva pantalla y se enviaran unos datos
        subChild1_1.setClickListener((node, value) -> {
            Intent i = new Intent(MainActivity.this,Pedido.class);
            i.putExtra("nit_usuario",cd);
            i.putExtra("bod_origen",1);
            i.putExtra("bod_destino",2);
            i.putExtra("modelo","08");

            startActivity(i);
        });

        //Subgrupo2"Gestion de Alambron"
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem1_2 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Traslado de Bodega (2-1)");
        TreeNode subChild1_2 = new TreeNode(subChildItem1_2).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //Agregamos subgrupo1"Gestion de Alambron".
        child1.addChild(subChild1_1);

        //Agregamos subgrupo2"Gestion de Alambron".
        //child1.addChild(subChild1_2);

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
        //child2.addChild(subChild2_1);

        //Agregamos subgrupo2"Gestion Galvanizado".
        //child2.addChild(subChild2_2);

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
        //child3.addChild(subChild3_1);

        //Agregamos subgrupo2"Gestion Puas".
        //child3.addChild(subChild3_2);

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
        //child4.addChild(subChild4_1);

        //Agregamos subgrupo2"Gestion Puntilleria".
        //child4.addChild(subChild4_2);

        //Agregamos Grupo"Gestion Puntilleria".
        //parent.addChildren(child1);
        root.addChild(child4);

        ////////////////////////////////////////////////////////////////////////////////////////////

        //Grupo"Otros"
        MyHolder.IconTreeItem childItem5 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Otros");
        TreeNode child5 = new TreeNode(childItem5).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 25));

        //SubGrupo1"Otros "
        //Enviamos el icono y el texto para el SubGrupo
        MyHolder.IconTreeItem subChildItem5_1 = new MyHolder.IconTreeItem(R.drawable.ic_folder, "Inventarios");
        TreeNode subChild5_1 = new TreeNode(subChildItem5_1).setViewHolder(new MyHolder(getApplicationContext(), false, R.layout.child, 100));

        //Al darle clic a este elemento en el treeview se abrira una nueva pantalla y se enviaran unos datos
        subChild5_1.setClickListener(new TreeNode.TreeNodeClickListener() {
            @Override
            public void onClick(TreeNode node, Object value) {
                Intent intent = new Intent(MainActivity.this, PedidoInventario.class);
                intent.putExtra("nit_usuario", cd);
                startActivity(intent);
            }
        });

        //Agregamos subgrupo1"Otros".
        child5.addChild(subChild5_1);

        //Agregamos Grupo"Otros".
        //parent.addChildren(child1);
        root.addChild(child5);

        //Agregamos AndroidTreeView en la vista.
        AndroidTreeView tView = new AndroidTreeView(getApplicationContext(), root);
        ((LinearLayout) findViewById(R.id.ll_parent)).addView(tView.getView());
    }
}