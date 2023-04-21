package com.example.handheld.modelos;

public class MesasModelo {
    String Mesa;
    String cantidad;

    public MesasModelo(String mesa, String cantidad) {
        Mesa = mesa;
        this.cantidad = cantidad;
    }

    public MesasModelo() {

    }

    public String getMesa() {
        return Mesa;
    }

    public void setMesa(String mesa) {
        Mesa = mesa;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
