package com.example.handheld.modelos;

public class CuentasModelo {

    String tipo;
    String modelo;
    String descripcion;
    String centro_fijo;
    String cta1;
    String cta2;
    String cta3;

    public CuentasModelo(String cta1, String cta2, String cta3) {
        this.cta1 = cta1;
        this.cta2 = cta2;
        this.cta3 = cta3;
    }

    public CuentasModelo(){

    }

    public String getCta1() {
        return cta1;
    }

    public void setCta1(String cta1) {
        this.cta1 = cta1;
    }

    public String getCta2() {
        return cta2;
    }

    public void setCta2(String cta2) {
        this.cta2 = cta2;
    }

    public String getCta3() {
        return cta3;
    }

    public void setCta3(String cta3) {
        this.cta3 = cta3;
    }
}
