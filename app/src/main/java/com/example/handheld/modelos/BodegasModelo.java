package com.example.handheld.modelos;

public class BodegasModelo {

    String bodega;
    String descripcion;
    String centro;
    String direccion;
    String codigo_cliente;
    String inactiva;

    public BodegasModelo(String bodega, String descripcion, String centro, String direccion, String codigo_cliente, String inactiva) {
        this.bodega = bodega;
        this.descripcion = descripcion;
        this.centro = centro;
        this.direccion = direccion;
        this.codigo_cliente = codigo_cliente;
        this.inactiva =inactiva;

    }

    public BodegasModelo(){

    }

    public String getBodega() {
        return bodega;
    }

    public void setBodega(String bodega) {
        this.bodega = bodega;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodigo_cliente() {
        return codigo_cliente;
    }

    public void setCodigo_cliente(String codigo_cliente) {
        this.codigo_cliente = codigo_cliente;
    }

    public String getInactiva() {
        return inactiva;
    }

    public void setInactiva(String inactiva) {
        this.inactiva = inactiva;
    }
}
