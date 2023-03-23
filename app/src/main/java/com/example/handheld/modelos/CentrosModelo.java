package com.example.handheld.modelos;

public class CentrosModelo {
    String centro;
    String descripcion;

    public CentrosModelo(String centro, String descripcion) {
        this.centro = centro;
        this.descripcion = descripcion;
    }

    public CentrosModelo(){

    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
