package com.example.handheld.modelos;

public class PersonaModelo {
    String nombres;
    String nit;

    public PersonaModelo(String nombres, String nit) {
        this.nombres = nombres;
        this.nit = nit;
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
}
