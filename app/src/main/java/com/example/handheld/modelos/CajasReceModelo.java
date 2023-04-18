package com.example.handheld.modelos;

public class CajasReceModelo {
    String referencia;
    String descripcion;
    String Cantidad;

    public CajasReceModelo(String referencia, String descripcion, String cantidad) {
        this.referencia = referencia;
        this.descripcion = descripcion;
        Cantidad = cantidad;
    }

    public CajasReceModelo(){
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }
}
