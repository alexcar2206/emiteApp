package com.example.proyectoinventario;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Contract {


    private Contract(){}


    public static abstract class CadenaEntry implements BaseColumns {
        public static final String TABLE_NAME ="cadena";

        public static final String ID = "id";
        public static final String NOMBRE = "nombre";

    }

    public static abstract class ClusterEntry implements BaseColumns {
        public static final String TABLE_NAME ="cluster";

        public static final String ID = "id";
        public static  final String CAD_ID = "cad_id";
        public static final String NOMBRE = "nombre";

    }

    public static abstract class SuperEntry implements BaseColumns {
        public static final String TABLE_NAME ="super";

        public static final String ID = "id";
        public static final String CLUSTER_ID = "cluster_id";
        public static final String NOMBRE = "nombre";
        public static final String FRECUENCIA = "frecuencia";
        public static final String COORDX = "coord_x";
        public static final String COORSY = "coord_y";
        public static final String LOCALIZACION = "localizacion";
        public static final String DIRECCION = "direccion";
        public static final String PROVINCIA = "provincia";
        public static final String CIUDAD = "ciudad";
        public static final String COD_CLIENTE = "cod_cliente";
        public static final String PERSONA_CONTACTO = "persona_contacto";
        public static final String EMAIL = "email";
        public static final String TELEFONO = "telefono";
        public static final String VISITAS_ANO = "visitas_ano";
        public static final String OBSERVACIONES = "observaciones";
    }

    public static  abstract  class ProductosEntry implements BaseColumns{
        public static final String TABLE_NAME = "productos";

        public static final String ID = "id";
        public static final String FAMILIA_ID = "familia_id";
        public static final String SUBFAMILIA_ID = "subfamilia_id";
        public static final String NOMBRE_COMPLETO = "nombre_completo";
        public static final String COD_NOMBRE = "codigo_nombre";
        public static final String NOMBRE = "nombre";
        public static final String PVP = "pvp";
        public static final String PVP_PROMO = "pvp_promo";
        public static final String NOMBRE_COMPETENCIA = "nombre_competencia";
        public static final String PVP_COMP = "pvp_competencia";
        public static final String PVP_PROMO_COMP = "pvp_promo_competencia";
    }

    public static abstract class FamiliaEntry implements BaseColumns{
        public static final String TABLE_NAME = "familia";

        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
    }

    public static abstract class SubfamiliaEntry implements BaseColumns{
        public static final String TABLE_NAME = "subfamilia";

        public static final String ID = "id";
        public static final String ID_FAMILIA = "id_familia";
        public static final String NOMBRE = "nombre";

    }

    public static abstract class ProductoClusterEntry implements BaseColumns{
        public static final String TABLE_NAME = "producto_cluster";

        public static final String ID = "id";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String ID_CLUSTER = "id_cluster";
    }

    public static abstract class FamiliaProductoEntry implements BaseColumns{
        public  static final String TABLE_NAME = "familia_producto";

        public static final String ID = "id";
        public static final String ID_FAMILIA = "id_familia";
        public static final String ID_PRODUCTO = "id_producto";
    }

    public static abstract class FamiliaSubmailiaEntry implements BaseColumns{
        public static final String TABLE_NAME = "familia_subfamilia";

        public static final String ID = "id";
        public static final String ID_FAMILIA = "id_familia";
        public static final String ID_SUBFAMILIA = "id_subfamilia";
    }

    public static abstract class InventarioEntry implements BaseColumns{
        public static final String TABLE_NAME = "inventario";

        public static final String ID = "id";
        public static final String ID_SUPER = "id_super";
        public static final String FECHA = "fecha";
        public static final String INICIO = "inicio";
        public static final String FIN = "fin";
        public static final String PENDIENTE = "pendiente";

        public static final String ID_USUARIO = "id_usuario";

        public static final String OBS_FINALES = "observaciones_finales";
    }

    public static abstract class ProcesoInventarioEntry implements BaseColumns{
        public  static final String TABLE_NAME = "proceso_inventario";

        public static final String ID = "id";
        public static final String ID_INVENTARIO = "id_inventario";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String PRECIO_PROD = "precio_producto";
        public static final String FACING_PROD = "facing_producto";
        public static final String PROMO_PROD = "promo_producto";
        public static final String OBSERVACIONES = "observaciones";
        public static final String EXPOSICION = "exposicion";
        public static final String ROTURA = "rotura";
    }

    public static abstract class ImagenesProdcutoEntry implements BaseColumns{
        public static final String TABLE_NAME = "imagenes_producto";

        public static final String ID = "id";
        public static final String OBJETO = "objeto";
        public static final String ID_OBJETO = "id_objeto";
        public static final String ID_PRODUCTO = "id_producto";
        public static final String FOTOSFIN = "fotos_final";
        public static final String IMAGEN = "imagen";
    }

    public static abstract class OrdenSuperInventario implements BaseColumns{
        public static  final String TABLE_NAME = "orden_superInventario";

        public static final String ID = "id";
        public static final String ID_SUPER = "id_super";
        public static final String ID_PROD = "id_producto";

    }

    public static abstract class Usuario implements BaseColumns{

        public static final String TABLE_NAME = "usuario";

        public static final String ID = "id";
        public static final String EMAIL_USUARIO = "email_usuario";
        public static final String CONTRA_USUARIO = "contra_usuario";

        public static final String LOGEADO = "logeado";
    }


    private static final String[] SQL_CREATE_ENTRIES = {
            "CREATE TABLE IF NOT EXISTS " + ClusterEntry.TABLE_NAME + "(" + ClusterEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + ClusterEntry.NOMBRE + " TEXT NOT NULL," + ClusterEntry.CAD_ID + " INTEGER, FOREIGN KEY("+ClusterEntry.CAD_ID+") REFERENCES "+CadenaEntry.TABLE_NAME +"("+CadenaEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + CadenaEntry.TABLE_NAME + "(" + CadenaEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CadenaEntry.NOMBRE + " TEXT NOT NULL)",
            "CREATE TABLE IF NOT EXISTS " + SuperEntry.TABLE_NAME + "(" + SuperEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+SuperEntry.CLUSTER_ID+" INTEGER, " + SuperEntry.NOMBRE + " TEXT NOT NULL, "+SuperEntry.FRECUENCIA +" TEXT NOT NULL,"+ SuperEntry.COORDX+" REAL, "+ SuperEntry.COORSY+ " REAL, "+SuperEntry.DIRECCION+" TEXT NOT NULL, "+ SuperEntry.PROVINCIA+" TEXT NOT NULL, "+SuperEntry.CIUDAD+" TEXT ,"+SuperEntry.COD_CLIENTE+" INTEGER,"+SuperEntry.PERSONA_CONTACTO+" TEXT ,"+SuperEntry.EMAIL+" TEXT ,"+SuperEntry.TELEFONO+" INTEGER ,"+SuperEntry.VISITAS_ANO+" INTEGER,"+SuperEntry.OBSERVACIONES+" TEXT , FOREIGN KEY("+ SuperEntry.CLUSTER_ID+") REFERENCES "+ClusterEntry.TABLE_NAME+"("+ClusterEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + ProductosEntry.TABLE_NAME + "(" + ProductosEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"+ ProductosEntry.FAMILIA_ID+" INTEGER ,"+ProductosEntry.SUBFAMILIA_ID+" INTEGER ,"+ProductosEntry.NOMBRE_COMPLETO +" TEXT NOT NULL,"+ProductosEntry.COD_NOMBRE+ " TEXT, "+ProductosEntry.NOMBRE+" TEXT, "+ProductosEntry.PVP+" REAL,"+ProductosEntry.PVP_PROMO+" REAL,"+ProductosEntry.NOMBRE_COMPETENCIA +" TEXT, "+ProductosEntry.PVP_COMP+" REAL,"+ProductosEntry.PVP_PROMO_COMP+" REAL, FOREIGN KEY("+ProductosEntry.FAMILIA_ID+") REFERENCES "+FamiliaEntry.TABLE_NAME+"("+FamiliaEntry.ID+")"+", FOREIGN KEY("+ProductosEntry.SUBFAMILIA_ID+") REFERENCES "+SubfamiliaEntry.TABLE_NAME+"("+SubfamiliaEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + ProductoClusterEntry.TABLE_NAME+ "(" +ProductoClusterEntry.ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ProductoClusterEntry.ID_PRODUCTO+" INTEGER NOT NULL ,"+ ProductoClusterEntry.ID_CLUSTER+" INTEGER NOT NULL, FOREIGN KEY("+ProductoClusterEntry.ID_PRODUCTO+") REFERENCES "+ProductosEntry.TABLE_NAME+"("+ProductosEntry.ID+"), FOREIGN KEY ("+ProductoClusterEntry.ID_CLUSTER+") REFERENCES "+ClusterEntry.TABLE_NAME+" ("+ClusterEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + FamiliaEntry.TABLE_NAME+ "(" + FamiliaEntry.ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+ FamiliaEntry.NOMBRE+" TEXT NOT NULL)",
            "CREATE TABLE IF NOT EXISTS " + SubfamiliaEntry.TABLE_NAME+ "(" + SubfamiliaEntry.ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+SubfamiliaEntry.NOMBRE+" TEXT NOT NULL,"+SubfamiliaEntry.ID_FAMILIA+" INTEGER NOT NULL, FOREIGN KEY ("+SubfamiliaEntry.ID_FAMILIA+") REFERENCES "+FamiliaEntry.TABLE_NAME+"("+FamiliaEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + FamiliaProductoEntry.TABLE_NAME+ "(" + FamiliaProductoEntry.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+FamiliaProductoEntry.ID_FAMILIA+" INTEGER NOT NULL, "+FamiliaProductoEntry.ID_PRODUCTO+" INTEGER NOT NULL, FOREIGN KEY ("+FamiliaProductoEntry.ID_FAMILIA+") REFERENCES "+FamiliaEntry.TABLE_NAME+"("+FamiliaEntry.ID+"), FOREIGN KEY ("+FamiliaProductoEntry.ID_PRODUCTO+") REFERENCES "+ ProductosEntry.TABLE_NAME+" ("+ProductosEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + FamiliaSubmailiaEntry.TABLE_NAME+ "(" +FamiliaSubmailiaEntry.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"+FamiliaSubmailiaEntry.ID_FAMILIA+" INTEGER NOT NULL,"+FamiliaSubmailiaEntry.ID_SUBFAMILIA+" INTEGER NOT NULL, FOREIGN KEY ("+FamiliaSubmailiaEntry.ID_FAMILIA+") REFERENCES "+FamiliaEntry.TABLE_NAME+"("+FamiliaEntry.ID+"), FOREIGN KEY( "+FamiliaSubmailiaEntry.ID_SUBFAMILIA+") REFERENCES "+SubfamiliaEntry.TABLE_NAME+"("+SubfamiliaEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + InventarioEntry.TABLE_NAME + "(" + InventarioEntry.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+InventarioEntry.ID_SUPER+" INTEGER NOT NULL,"+InventarioEntry.FECHA+" TEXT NOT NULL, "+InventarioEntry.INICIO+" TEXT NOT NULL,"+InventarioEntry.FIN+" TEXT,"+ InventarioEntry.PENDIENTE+" TEXT NOT NULL,"+InventarioEntry.OBS_FINALES+" TEXT,"+InventarioEntry.ID_USUARIO+" INT NOT NULL, FOREIGN KEY("+ InventarioEntry.ID_SUPER+") REFERENCES "+ SuperEntry.TABLE_NAME+"("+SuperEntry.ID+"), FOREIGN KEY("+InventarioEntry.ID_USUARIO+") REFERENCES "+Usuario.TABLE_NAME+"("+Usuario.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + ProcesoInventarioEntry.TABLE_NAME+ "(" + ProcesoInventarioEntry.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ProcesoInventarioEntry.ID_INVENTARIO+" INTEGER NOT NULL,"+ ProcesoInventarioEntry.ID_PRODUCTO+" INTEGER NOT NULL, "+ProcesoInventarioEntry.PRECIO_PROD+ " REAL NOT NULL, "+ProcesoInventarioEntry.FACING_PROD+" INTEGER NOT NULL, "+ProcesoInventarioEntry.PROMO_PROD+" TEXT NOT NULL, "+ProcesoInventarioEntry.OBSERVACIONES+" TETX,"+ProcesoInventarioEntry.EXPOSICION+" TEXT NOT NULL, "+ProcesoInventarioEntry.ROTURA+" TEXT NOT NULL, FOREIGN KEY( "+ProcesoInventarioEntry.ID_INVENTARIO+") REFERENCES "+InventarioEntry.TABLE_NAME+"("+InventarioEntry.ID+"), FOREIGN KEY ("+ProcesoInventarioEntry.ID_PRODUCTO+") REFERENCES "+ProductosEntry.TABLE_NAME+"("+ProductosEntry.ID+")"+")",
            "CREATE TABLE IF NOT EXISTS " + ImagenesProdcutoEntry.TABLE_NAME + "(" + ImagenesProdcutoEntry.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ImagenesProdcutoEntry.OBJETO+" TEXT NOT NULL, "+ImagenesProdcutoEntry.ID_OBJETO+" INT NOT NULL, "+ImagenesProdcutoEntry.ID_PRODUCTO+" INTEGER, "+ImagenesProdcutoEntry.FOTOSFIN +" TEXT ,"+ImagenesProdcutoEntry.IMAGEN+" TEXT NOT NULL, FOREIGN KEY( "+ ImagenesProdcutoEntry.ID_PRODUCTO+") REFERENCES "+ProductosEntry.TABLE_NAME+"("+ProductosEntry.ID+"))",
            "CREATE TABLE IF NOT EXISTS " + OrdenSuperInventario.TABLE_NAME + "(" +OrdenSuperInventario.ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+ OrdenSuperInventario.ID_SUPER+ " INTEGER NOT NULL, "+OrdenSuperInventario.ID_PROD+" INTEGER NOT NULL, FOREIGN KEY("+OrdenSuperInventario.ID_SUPER+") REFERENCES "+SuperEntry.TABLE_NAME+"("+ SuperEntry.ID+"), FOREIGN KEY("+OrdenSuperInventario.ID_PROD+") REFERENCES "+ProductosEntry.TABLE_NAME+"("+ProductosEntry.ID+"))",
            "CREATE TABLE IF NOT EXISTS " + Usuario.TABLE_NAME+" ( "+Usuario.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+Usuario.EMAIL_USUARIO+" TEXT,"+Usuario.CONTRA_USUARIO+" TEXT,"+Usuario.LOGEADO+" TEXT )"
    };




    private static final String[] SQL_DELETE_ENTRIES ={
            "DROP TABLE IF EXISTS " + CadenaEntry.TABLE_NAME,
            "DROP TABLE IF EXISTS " + ClusterEntry.TABLE_NAME,
            "DROP TABLE IF EXISTS "+ SuperEntry.TABLE_NAME,
            " DROP TABLE IF EXISTS "+ ProductosEntry.TABLE_NAME,
            " DROP TABLE IF EXISTS "+ FamiliaEntry.TABLE_NAME,
            " DROP TABLE IF EXISTS "+SubfamiliaEntry.TABLE_NAME,
            "DROP TABLE IF EXISTS "+ ProductoClusterEntry.TABLE_NAME,
            " DROP TABLE IF EXISTS "+FamiliaSubmailiaEntry.TABLE_NAME,
            " DROP TABLE IF EXISTS "+ FamiliaProductoEntry.TABLE_NAME
    };


    public static class InventariodbHelper extends SQLiteOpenHelper{


        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Inventario.db";


        public InventariodbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            for (int i = 0; i < SQL_CREATE_ENTRIES.length; i++) {
                db.execSQL(SQL_CREATE_ENTRIES[i]);
            }



            /*
            for (int i = 0; i < SQL_DELETE_ENTRIES.length; i++) {
                db.execSQL(SQL_DELETE_ENTRIES[i]);
            }

             */
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
        }
    }



}
