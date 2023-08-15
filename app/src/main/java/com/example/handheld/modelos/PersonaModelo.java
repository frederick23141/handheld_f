package com.example.handheld.modelos;

public class PersonaModelo {
    String nombres;
    String nit;
    String centro;

    public PersonaModelo(String nombres, String nit, String centro) {
        this.nombres = nombres;
        this.nit = nit;
        this.centro = centro;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }
}
