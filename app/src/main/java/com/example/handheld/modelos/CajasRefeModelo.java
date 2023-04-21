package com.example.handheld.modelos;

public class CajasRefeModelo {
    String fecha;
    String referencia;
    String mesa;
    Integer cantidad;

    public CajasRefeModelo(String fecha, String referencia, String mesa, Integer cantidad) {
        this.fecha = fecha;
        this.referencia = referencia;
        this.mesa = mesa;
        this.cantidad = cantidad;
    }

    public CajasRefeModelo(){}

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
