package com.example.handheld.modelos;

public class EmpRecepcionadoCajasModelo {
    Double cantidad;
    Double promedio;
    Double costo_unitario;
    String REFERENCIA;

    public EmpRecepcionadoCajasModelo(Double cantidad, Double promedio, Double costo_unitario, String REFERENCIA) {
        this.cantidad = cantidad;
        this.promedio = promedio;
        this.costo_unitario = costo_unitario;
        this.REFERENCIA = REFERENCIA;
    }

    public EmpRecepcionadoCajasModelo(){

    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Double getCosto_unitario() {
        return costo_unitario;
    }

    public void setCosto_unitario(Double costo_unitario) {
        this.costo_unitario = costo_unitario;
    }

    public String getREFERENCIA() {
        return REFERENCIA;
    }

    public void setREFERENCIA(String REFERENCIA) {
        this.REFERENCIA = REFERENCIA;
    }
}
