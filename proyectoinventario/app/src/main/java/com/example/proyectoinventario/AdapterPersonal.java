package com.example.proyectoinventario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPersonal extends BaseAdapter {

    ArrayList<InfoAdapter> lista;
    LayoutInflater inflador;


    public AdapterPersonal(Context context, ArrayList<InfoAdapter> lista) {
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

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Contenedor contenedor = null;

        if (view == null){

            view = inflador.inflate(R.layout.listview_mejorado, null);

            contenedor = new Contenedor();
            contenedor.txtID =(TextView) view.findViewById(R.id.textViewID);
            contenedor.txtName = (TextView) view.findViewById(R.id.textView69);
            contenedor.txtSuper = view.findViewById(R.id.textViewSuperAdap);
            view.setTag(contenedor);
        }
        else{
            contenedor=(Contenedor)view.getTag();
        }

        InfoAdapter infoAdapter = (InfoAdapter) getItem(position);
        contenedor.txtName.setText(infoAdapter.getNombre());
        contenedor.txtName.setTextColor(infoAdapter.getColorTextName());
        contenedor.txtID.setText(infoAdapter.getId());
        contenedor.txtID.setTextColor(infoAdapter.getColorTextID());
        contenedor.txtSuper.setText(infoAdapter.getSuperName());
        contenedor.txtSuper.setTextColor(infoAdapter.getColorSuper());
        view.setBackgroundColor(infoAdapter.getColor());
        return view;
    }


    class Contenedor {
        TextView txtName, txtID, txtSuper;
    }
}
