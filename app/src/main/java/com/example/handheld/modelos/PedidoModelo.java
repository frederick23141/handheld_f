package com.example.handheld.modelos;

public class PedidoModelo {

    private String numero;
    private String idDetalle;
    private String fecha;
    private String codigo;
    private String pendiente;
    private String descripcion;

    public PedidoModelo(String numero, String idDetalle, String fecha, String codigo, String pendiente, String descripcion) {
        this.numero = numero;
        this.idDetalle = idDetalle;
        this.fecha = fecha;
        this.codigo = codigo;
        this.pendiente = pendiente;
        this.descripcion = descripcion;
    }

    public PedidoModelo(){

    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(String idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getPendiente() {
        return pendiente;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
