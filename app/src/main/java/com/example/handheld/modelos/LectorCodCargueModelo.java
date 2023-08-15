package com.example.handheld.modelos;


import java.io.Serializable;

public class LectorCodCargueModelo implements Serializable {
    private String consecutivo;

    private String  numero_transaccion;

    private String Codigoalambron;

    private String PesoAlambron;

    private String Num_imp;

    private String Detalle;

    private String Num_rolloAlambron;

    private String estado_muestra;

    private String nit_proveedor;

    private String costo_unitario_alambron;

    private String numero_rollos_descargar;


    public LectorCodCargueModelo(String numero_transaccion, String codigoalambron, String pesoAlambron, String num_imp, String detalle,String consecutivo,String num_rolloAlambron,String estado_muestra,String nit_proveedor,String costo_unitario_alambron,String numero_rollos_descargar) {
        this.consecutivo = consecutivo;
        this.numero_transaccion = numero_transaccion;
        this.Codigoalambron = codigoalambron;
        this.PesoAlambron = pesoAlambron;
        this.Num_imp = num_imp;
        this.Detalle = detalle;
        this.Num_rolloAlambron = num_rolloAlambron;
        this.estado_muestra = estado_muestra;
        this.nit_proveedor = nit_proveedor;
        this.costo_unitario_alambron = costo_unitario_alambron;
        this.numero_rollos_descargar=numero_rollos_descargar;
    }

    public LectorCodCargueModelo() {
    }

    public String getNumero_transaccion() {
        return numero_transaccion;
    }

    public void setNumero_transaccion(String numero_transaccion) {
        this.numero_transaccion = numero_transaccion;
    }

    public String getNit_proveedor() {
        return nit_proveedor;
    }

    public void setNit_proveedor(String nit_proveedor) {
        this.nit_proveedor = nit_proveedor;
    }

    public String getCodigoalambron() {
        return Codigoalambron;
    }

    public void setCodigoalambron(String codigoalambron) {
        Codigoalambron = codigoalambron;
    }

    public String getPesoAlambron() {
        return PesoAlambron;
    }

    public void setPesoAlambron(String pesoAlambron) {
        PesoAlambron = pesoAlambron;
    }

    public String getNum_imp() {
        return Num_imp;
    }

    public void setNum_imp(String num_imp) {
        Num_imp = num_imp;
    }

    public String getDetalle() {
        return Detalle;
    }

    public void setDetalle(String detalle) {
        Detalle = detalle;
    }


    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
    }

    public String getNum_rolloAlambron() {
        return Num_rolloAlambron;
    }

    public void setNum_rolloAlambron(String num_rolloAlambron) {
        Num_rolloAlambron = num_rolloAlambron;
    }

    public String getEstado_muestra() {
        return estado_muestra;
    }

    public void setEstado_muestra(String estado_muestra) {
        this.estado_muestra = estado_muestra;
    }

    public String getCosto_unitario_alambron() {
        return costo_unitario_alambron;
    }

    public void setCosto_unitario_alambron(String costo_unitario_alambron) {
        this.costo_unitario_alambron = costo_unitario_alambron;
    }

    public String getNumero_rollos_descargar() {
        return numero_rollos_descargar;
    }

    public void setNumero_rollos_descargar(String numero_rollos_descargar) {
        this.numero_rollos_descargar = numero_rollos_descargar;
    }
}