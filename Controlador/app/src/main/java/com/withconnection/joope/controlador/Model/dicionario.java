package com.withconnection.joope.controlador.Model;

import java.util.ArrayList;

public class dicionario {
    protected ArrayList<String> objeto;
    protected ArrayList<String> comandoLigar;
    protected ArrayList<String> comandoDesligar;
    public dicionario(){
        this.objeto = new ArrayList<>();
        this.comandoLigar = new ArrayList<>();
        this.comandoDesligar = new ArrayList<>();

        //Para Lâmpada;
        objeto.add("lâmpada");
        objeto.add("luz");
        objeto.add("lamparina");

        //Para acender a Lâmpada;
        comandoLigar.add("ligar");
        comandoLigar.add("acender");
        comandoLigar.add("ligue");
        comandoLigar.add("acenda");

        //Para Desligar a Lâmpada;
        comandoDesligar.add("desligar");
        comandoDesligar.add("desligue");
        comandoDesligar.add("apagar");
        comandoDesligar.add("apague");
    }

    public ArrayList<String> getComandoLigar() {
        return comandoLigar;
    }

    public void setComandoLigar(ArrayList<String> comandoLigar) {
        this.comandoLigar = comandoLigar;
    }

    public ArrayList<String> getComandoDesligar() {
        return comandoDesligar;
    }

    public void setComandoDesligar(ArrayList<String> comandoDesligar) {
        this.comandoDesligar = comandoDesligar;
    }

    public ArrayList<String> getObjeto() {
        return objeto;
    }

    public void setObjeto(ArrayList<String> objeto) {
        this.objeto = objeto;
    }
}
