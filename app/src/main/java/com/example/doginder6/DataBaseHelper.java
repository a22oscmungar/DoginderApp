package com.example.doginder6;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MiPerfil";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLEUSU_NAME = "DatosDeUsuario";
    public static final String TABLEMASC_NAME = "DatosDeMascota";
    public static final String COLUMN_IDUSU = "idUsuario";
    public static final String COLUMN_NOMUSU = "nombre";
    public static final String COLUMN_PASSUSU = "pass";
    public static final String COLUMN_CORREUUSU = "correu";
    public static final String COLUMN_COGNOMUSU = "cognom";
    public static final String COLUMN_EDADUSU = "edad";
    public static final String COLUMN_GENEROUSU = "genero";
    public static final String COLUMN_LATITUDUSU = "latitud";
    public static final String COLUMN_LONGITUDUSU = "longitud";
    public static final String COLUMN_IDMASCO = "idMascota";
    public static final String COLUMN_NOMMASCO = "nombre";
    public static final String COLUMN_EDADMASCO = "edad";
    public static final String COLUMN_GENEROMASCO = "genero";
    public static final String COLUMN_DESCRIPCIONMASCO = "descripcion";
    public static final String COLUMN_FOTOMASCO = "foto";
    public static final String COLUMN_NOMRAZA = "raza";
    public static final String COLUMN_RELACIONMASCO = "relacionMasco";
    public static final String COLUMN_RELACIONPERSONAS = "relacionPerso";
    public static final String COLUMN_IDHUMANO = "idHumano";

    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static final String TABLEUSU_CREATE =
            "CREATE TABLE " + TABLEUSU_NAME + " (" +
                    COLUMN_IDUSU + " INTEGER PRIMARY KEY, " +
                    COLUMN_NOMUSU + " TEXT, " +
                    COLUMN_PASSUSU + " TEXT, " +
                    COLUMN_CORREUUSU + " TEXT, " +
                    COLUMN_COGNOMUSU + " TEXT, " +
                    COLUMN_EDADUSU + " INTEGER, " +
                    COLUMN_GENEROUSU + " TEXT, " +
                    COLUMN_LATITUDUSU + " REAL, " +
                    COLUMN_LONGITUDUSU + " REAL);";

    public static final String TABLEMASC_CREATE =
            "CREATE TABLE " + TABLEMASC_NAME + " (" +
                    COLUMN_IDMASCO + " INTEGER PRIMARY KEY, " +
                    COLUMN_NOMMASCO + " TEXT, " +
                    COLUMN_EDADMASCO + " INTEGER, " +
                    COLUMN_GENEROMASCO + " TEXT, " +
                    COLUMN_DESCRIPCIONMASCO + " TEXT, " +
                    COLUMN_FOTOMASCO + " TEXT, " +
                    COLUMN_NOMRAZA + " TEXT, " +
                    COLUMN_RELACIONMASCO + " TEXT, " +
                    COLUMN_RELACIONPERSONAS + " TEXT," +
                    COLUMN_IDHUMANO + " INTEGER);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLEUSU_CREATE);
        db.execSQL(TABLEMASC_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLEUSU_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLEMASC_NAME);
        onCreate(db);
    }

    public long insertUsu(Usuario2 usuario2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_IDUSU, usuario2.idUsu);
        values.put(COLUMN_NOMUSU, usuario2.nombreUsu);
        values.put(COLUMN_PASSUSU, usuario2.pass);
        values.put(COLUMN_CORREUUSU, usuario2.mailUsu);
        values.put(COLUMN_COGNOMUSU, usuario2.apellidosUsu);
        values.put(COLUMN_EDADUSU, usuario2.edadUsu);
        values.put(COLUMN_GENEROUSU, usuario2.genero);
        values.put(COLUMN_LATITUDUSU, usuario2.ubiUsu.x);
        values.put(COLUMN_LONGITUDUSU, usuario2.ubiUsu.y);

        long result = db.insert(TABLEUSU_NAME, null, values);

        db.close();

        Log.d("prueba", "insertUsu: "+ result);

        return result;
    }

    public long insertMasc(Usuario2 usuario2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_IDMASCO, usuario2.mascotaId);
        values.put(COLUMN_NOMMASCO, usuario2.nombre);
        values.put(COLUMN_EDADMASCO, usuario2.edad);
        values.put(COLUMN_GENEROMASCO, usuario2.sexo);
        values.put(COLUMN_DESCRIPCIONMASCO, usuario2.descripcion);
        values.put(COLUMN_FOTOMASCO, usuario2.foto);
        values.put(COLUMN_NOMRAZA, usuario2.raza);
        values.put(COLUMN_RELACIONMASCO, usuario2.relacionMascotas);
        values.put(COLUMN_RELACIONPERSONAS, usuario2.relacionHumanos);
        values.put(COLUMN_IDHUMANO, usuario2.idHumano);

        long result = db.insert(TABLEMASC_NAME, null, values);

        Log.d("prueba" , "insertMasc: "+ result);

        db.close();

        return result;
    }
}
