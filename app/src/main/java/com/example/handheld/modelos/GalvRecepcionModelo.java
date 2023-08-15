package com.example.handheld.modelos;

public class GalvRecepcionModelo {
    String nro_orden;
    String nro_rollo;
    String referencia;
    String descripcion;
    String peso;
    String color;

    public GalvRecepcionModelo(String nro_orden, String nro_rollo, String referencia, String descripcion, String peso, String color) {
        this.nro_orden = nro_orden;
        this.nro_rollo = nro_rollo;
        this.referencia = referencia;
        this.descripcion = descripcion;
        this.peso = peso;
        this.color = color;
    }

    public GalvRecepcionModelo(){

    }

    public String getNro_orden() {
        return nro_orden;
    }

    public void setNro_orden(String nro_orden) {
        this.nro_orden = nro_orden;
    }

    public String getNro_rollo() {
        return nro_rollo;
    }

    public void setNro_rollo(String nro_rollo) {
        this.nro_rollo = nro_rollo;
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

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
