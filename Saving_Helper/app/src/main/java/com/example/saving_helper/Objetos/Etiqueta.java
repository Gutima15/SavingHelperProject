package com.example.saving_helper.Objetos;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Etiqueta implements Parcelable {
    private String nombre;
    private String color;
    private ArrayList<Gasto> gastos;


    public Etiqueta(){
        gastos = new ArrayList<Gasto>();
    }
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

    protected Etiqueta(Parcel in) {
        nombre = in.readString();
        color = in.readString();
        gastos = in.createTypedArrayList(Gasto.CREATOR);
    }

    public static final Creator<Etiqueta> CREATOR = new Creator<Etiqueta>() {
        @Override
        public Etiqueta createFromParcel(Parcel in) {
            return new Etiqueta(in);
        }

        @Override
        public Etiqueta[] newArray(int size) {
            return new Etiqueta[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(color);
        parcel.writeTypedList(gastos);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Etiqueta etiqueta = (Etiqueta) obj;
        if(this.nombre.equals(etiqueta.getNombre())){
            return true;
        }
        return false;
    }
}
