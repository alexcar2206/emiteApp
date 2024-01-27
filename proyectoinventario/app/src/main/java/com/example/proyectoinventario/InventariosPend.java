package com.example.proyectoinventario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventariosPend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventariosPend extends Fragment {


    ListView listView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InventariosPend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventariosPend.
     */
    // TODO: Rename and change types and number of parameters
    public static InventariosPend newInstance(String param1, String param2) {
        InventariosPend fragment = new InventariosPend();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_inventarios_pend, container, false);


        listView = view.findViewById(R.id.listInvPend);



        Contract.InventariodbHelper dbHelper = new Contract.InventariodbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        Intent itInv = new Intent(getContext(), InventariosActivity.class);


        ArrayList<Inventario> inventarios = new ArrayList<>();
        ArrayList<InfoAdapter> infoAdapters = new ArrayList<>();
        AdapterPersonal adapterPersonal;


        Cursor cUser = db.rawQuery("SELECT * FROM "+Contract.Usuario.TABLE_NAME+" WHERE "+Contract.Usuario.LOGEADO+" = "+1,null);
        cUser.moveToFirst();
        @SuppressLint("Range") int idUser = cUser.getInt(cUser.getColumnIndex(Contract.Usuario.ID));



        Cursor c = db.rawQuery("SELECT * FROM "+Contract.InventarioEntry.TABLE_NAME+ " WHERE "+Contract.InventarioEntry.PENDIENTE+" = "+1+" AND "+Contract.InventarioEntry.ID_USUARIO+" = "+idUser,null);

        while (c.moveToNext()){

            @SuppressLint("Range") int id = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID));
            @SuppressLint("Range") String fecha = c.getString(c.getColumnIndex(Contract.InventarioEntry.FECHA));
            @SuppressLint("Range") String ini = c.getString(c.getColumnIndex(Contract.InventarioEntry.INICIO));
            @SuppressLint("Range") String fin = c.getString(c.getColumnIndex(Contract.InventarioEntry.FIN));
            @SuppressLint("Range") String obs = c.getString(c.getColumnIndex(Contract.InventarioEntry.OBS_FINALES));
            @SuppressLint("Range") int userId = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID_USUARIO));
            @SuppressLint("Range") int pend = c.getInt(c.getColumnIndex(Contract.InventarioEntry.PENDIENTE));
            @SuppressLint("Range") int idSuper = c.getInt(c.getColumnIndex(Contract.InventarioEntry.ID_SUPER));



            Cursor cSuper = db.rawQuery("SELECT * FROM "+Contract.SuperEntry.TABLE_NAME+" WHERE "+Contract.SuperEntry.ID+ " = "+idSuper, null);
            cSuper.moveToFirst();
            @SuppressLint("Range") String superName = cSuper.getString(cSuper.getColumnIndex(Contract.SuperEntry.NOMBRE));


            Inventario inventario = new Inventario();
            inventario.setId(id);
            inventario.setFecha(fecha);
            inventario.setIncio(ini);
            inventario.setFin(fin);
            inventario.setObsFin(obs);
            inventario.setId_super(idSuper);
            inventario.setIdUser(userId);
            inventario.setPendiente(pend);

            inventarios.add(inventario);

            InfoAdapter ia = new InfoAdapter("#" + id, fecha, superName , Color.WHITE, Color.RED, Color.RED,Color.RED);
            infoAdapters.add(ia);

        }

        adapterPersonal = new AdapterPersonal(getContext(), infoAdapters);
        listView.setAdapter(adapterPersonal);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inventario inventario = inventarios.get(position);
                itInv.putExtra("INVPEND", inventario);
                itInv.putExtra("VIEWPAGER", true);
                startActivity(itInv);
            }
        });
        return view;
    }
}