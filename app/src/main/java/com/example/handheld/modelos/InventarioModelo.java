package com.example.handheld.modelos;

public class InventarioModelo {
    String id;
    String codigo;
    String bodega;

    public InventarioModelo(String id, String codigo, String bodega) {
        this.id = id;
        this.codigo = codigo;
        this.bodega = bodega;
    }

    public InventarioModelo(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }
}
