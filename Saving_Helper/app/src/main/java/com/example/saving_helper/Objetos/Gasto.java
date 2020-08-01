package com.example.saving_helper.Objetos;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Gasto implements Parcelable {
    private String nombre;
    private float monto;
    private Date fecha;

    public Gasto(){

    }

    public Gasto(String nombre, float monto, Date fecha) {
        this.nombre = nombre;
        this.monto = monto;
        this.fecha = fecha;
    }

    protected Gasto(Parcel in) {
        nombre = in.readString();
        monto = in.readFloat();
        String fecha = in.readString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            this.fecha = format.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeFloat(monto);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(this.fecha);
        dest.writeString(fecha);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Gasto> CREATOR = new Creator<Gasto>() {
        @Override
        public Gasto createFromParcel(Parcel in) {
            return new Gasto(in);
        }

        @Override
        public Gasto[] newArray(int size) {
            return new Gasto[size];
        }
    };

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
