package com.example.handheld.modelos;

public class TipotransModelo {
    String tipo;
    String sw;

    public TipotransModelo(String tipo, String sw) {
        this.tipo = tipo;
        this.sw = sw;
    }

    public TipotransModelo(){

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }
}
