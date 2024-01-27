package com.example.proyectoinventario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterInventario extends BaseAdapter {


    ArrayList<InfoAdapterInventario> lista;
    LayoutInflater inflador;


    public AdapterInventario(Context context, ArrayList<InfoAdapterInventario> lista) {
        this.lista = lista;
        this.inflador = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("WrongConstant")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Contenedor contenedor = null;

        if (view == null){

            view = inflador.inflate(R.layout.listview_inventario, null);

            contenedor = new Contenedor();
            contenedor.txtProd =(TextView) view.findViewById(R.id.textViewProd);
            contenedor.imageView = (ImageView) view.findViewById(R.id.imageViewCheck);
            view.setTag(contenedor);
        }
        else{
            contenedor=(Contenedor)view.getTag();
        }

        InfoAdapterInventario infoAdapter = (InfoAdapterInventario) getItem(position);
        contenedor.txtProd.setText(infoAdapter.getTextProd());
        contenedor.imageView.setImageResource(infoAdapter.getImageCheck());
        contenedor.imageView.setVisibility(infoAdapter.getVisibility());
        return view;
    }


    class Contenedor {
        TextView txtProd;
        ImageView imageView;
    }
}
