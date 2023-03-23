package com.example.handheld.modelos;

public class RolloterminadoModelo {
    String cod_orden;
    String id_detalle;
    String id_rollo;

    public RolloterminadoModelo(String cod_orden, String id_detalle, String id_rollo) {
        this.cod_orden = cod_orden;
        this.id_detalle = id_detalle;
        this.id_rollo = id_rollo;
    }

    public RolloterminadoModelo(){

    }

    public String getCod_orden() {
        return cod_orden;
    }

    public void setCod_orden(String cod_orden) {
        this.cod_orden = cod_orden;
    }

    public String getId_detalle() {
        return id_detalle;
    }

    public void setId_detalle(String id_detalle) {
        this.id_detalle = id_detalle;
    }

    public String getId_rollo() {
        return id_rollo;
    }

    public void setId_rollo(String id_rollo) {
        this.id_rollo = id_rollo;
    }
}
