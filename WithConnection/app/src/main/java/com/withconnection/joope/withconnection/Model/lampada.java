package com.withconnection.joope.withconnection.Model;

public class lampada {
    private int id;
    private String lampada;
    private String estado;

    public lampada(int id, String lampada, String estado){
        this.id = id;
        this.lampada = lampada;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLampada() {
        return lampada;
    }

    public void setLampada(String lampada) {
        this.lampada = lampada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
