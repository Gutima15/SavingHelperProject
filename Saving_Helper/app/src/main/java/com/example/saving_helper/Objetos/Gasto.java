package com.example.saving_helper.Objetos;

import java.util.Date;

public class Gasto {
    private String nombre;
    private float monto;
    private Date fecha;

    public Gasto(String nombre, float monto, Date fecha) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public float getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
