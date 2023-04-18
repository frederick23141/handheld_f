package com.example.handheld.modelos;

public class CajasRefeModelo {
    String fecha;
    String referencia;
    String mesa;

    public CajasRefeModelo(String fecha, String referencia, String mesa) {
        this.fecha = fecha;
        this.referencia = referencia;
        this.mesa = mesa;
    }

    public CajasRefeModelo(){
    }

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
}
