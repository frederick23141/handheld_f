package com.example.handheld.modelos;

public class CajasReceModelo {
    String referencia;
    String Cantidad;

    public CajasReceModelo(String referencia, String cantidad) {
        this.referencia = referencia;
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

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }
}
