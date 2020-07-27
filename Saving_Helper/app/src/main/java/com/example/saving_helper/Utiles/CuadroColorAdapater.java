package com.example.saving_helper.Utiles;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.saving_helper.R;

public class CuadroColorAdapater extends BaseAdapter {
    private Context mContext;
    private String[] colores;

    public CuadroColorAdapater(Context mContext, String[] colores) {
        this.mContext = mContext;
        this.colores = colores;
    }

    @Override
    public int getCount() {
        return colores.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        String color = colores[i];

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(R.layout.layout_columna_color,null);

        ImageView imageView = convertView.findViewById(R.id.image_ColorCuadro);

        imageView.setColorFilter(Color.parseColor(color));



        return convertView;
    }
}
