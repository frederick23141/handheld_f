package com.example.handheld.modelos;

public class DetalleTranModelo {

    String numero;
    String tipo;
    String num_trans;
    String codigo;
    String peso;
    String num_imp;
    String detalle;
    String num_rollo;
    String estado_muestra;
    String nit_prov;
    String costo_unit;

    public DetalleTranModelo(String numero, String tipo, String num_trans, String codigo, String peso, String num_imp, String detalle, String num_rollo, String estado_muestra, String nit_prov, String costo_unit) {
        this.numero = numero;
        this.tipo = tipo;
        this.num_trans = num_trans;
        this.codigo = codigo;
        this.peso = peso;
        this.num_imp = num_imp;
        this.detalle = detalle;
        this.num_rollo = num_rollo;
        this.estado_muestra = estado_muestra;
        this.nit_prov = nit_prov;
        this.costo_unit = costo_unit;
    }

    public DetalleTranModelo(){

    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNum_trans() {
        return num_trans;
    }

    public void setNum_trans(String num_trans) {
        this.num_trans = num_trans;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getNum_imp() {
        return num_imp;
    }

    public void setNum_imp(String num_imp) {
        this.num_imp = num_imp;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getNum_rollo() {
        return num_rollo;
    }

    public void setNum_rollo(String num_rollo) {
        this.num_rollo = num_rollo;
    }

    public String getEstado_muestra() {
        return estado_muestra;
    }

    public void setEstado_muestra(String estado_muestra) {
        this.estado_muestra = estado_muestra;
    }

    public String getNit_prov() {
        return nit_prov;
    }

    public void setNit_prov(String nit_prov) {
        this.nit_prov = nit_prov;
    }

    public String getCosto_unit() {
        return costo_unit;
    }

    public void setCosto_unit(String costo_unit) {
        this.costo_unit = costo_unit;
    }
}
