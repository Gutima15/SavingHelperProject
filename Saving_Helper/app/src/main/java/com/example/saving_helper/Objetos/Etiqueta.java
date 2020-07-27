package com.example.saving_helper.Objetos;

import java.util.ArrayList;

public class Etiqueta {
    private String nombre;
    private String color;
    private ArrayList<Gasto> gastos;

    public Etiqueta(String nombre, String color) {
        this.nombre = nombre;
        this.color = color;
        this.gastos = new ArrayList<Gasto>();
    }

    public Etiqueta(String nombre, String color, ArrayList<Gasto> gastos) {
        this.nombre = nombre;
        this.color = color;
        this.gastos = gastos;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<Gasto> getGastos() {
        return gastos;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setGastos(ArrayList<Gasto> gastos) {
        this.gastos = gastos;
    }
}
