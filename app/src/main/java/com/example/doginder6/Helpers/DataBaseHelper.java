package com.example.doginder6.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.doginder6.Objects.Usuario2;

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
    public static final String COLUMN_IMGPROFILE = "imgProfile";

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
                    COLUMN_EDADUSU + " TEXT, " +
                    COLUMN_GENEROUSU + " TEXT, " +
                    COLUMN_LATITUDUSU + " REAL, " +
                    COLUMN_LONGITUDUSU + " REAL, " +
                    COLUMN_IMGPROFILE + " TEXT);";

    public static final String TABLEMASC_CREATE =
            "CREATE TABLE " + TABLEMASC_NAME + " (" +
                    COLUMN_IDMASCO + " INTEGER PRIMARY KEY, " +
                    COLUMN_NOMMASCO + " TEXT, " +
                    COLUMN_EDADMASCO + " TEXT, " +
                    COLUMN_GENEROMASCO + " TEXT, " +
                    COLUMN_DESCRIPCIONMASCO + " TEXT, " +
                    COLUMN_FOTOMASCO + " TEXT, " +
                    COLUMN_NOMRAZA + " TEXT, " +
                    COLUMN_RELACIONMASCO + " TEXT, " +
                    COLUMN_RELACIONPERSONAS + " TEXT, " +
                    COLUMN_IDHUMANO + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_IDHUMANO + ") REFERENCES " + TABLEUSU_NAME + "(" + COLUMN_IDUSU + "));";
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

        Log.d("pruebaInsertUsu", "insertUsu: " + usuario2.toString());

        // Verificar si el usuario ya existe
        if (usuarioNoExiste(db, usuario2.idUsu)) {
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
            values.put(COLUMN_IMGPROFILE, usuario2.imgProfile);

            long result = db.insert(TABLEUSU_NAME, null, values);

            Log.d("prueba", "insertUsu: " + result);

            insertMasc(usuario2);

            db.close();


            return result;
        } else {
            Log.d("prueba", "Usuario con ID " + usuario2.idUsu + " ya existe.");
            return -1; // Indica que no se realizó la inserción
        }
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

    public Usuario2 getUsuarioCompleto(int idUsuario) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario2 usuario2 = null;
        String stringId = String.valueOf(idUsuario);

        // Consulta a la tabla USUARIO
        Cursor cursorUsuario = db.query(TABLEUSU_NAME, null, null, null, null, null, null);
        Log.d("prueba", "getUsuarioCompleto: " + cursorUsuario.getCount());

        if (cursorUsuario != null && cursorUsuario.moveToFirst()) {
            // Obtén los datos del usuario
            int idUsuIndex = cursorUsuario.getColumnIndex("idUsuario");
            int nombreUsuIndex = cursorUsuario.getColumnIndex(COLUMN_NOMUSU);
            int passIndex = cursorUsuario.getColumnIndex(COLUMN_PASSUSU);
            int mailUsuIndex = cursorUsuario.getColumnIndex(COLUMN_CORREUUSU);
            int apellidosUsuIndex = cursorUsuario.getColumnIndex(COLUMN_COGNOMUSU);
            int edadUsuIndex = cursorUsuario.getColumnIndex(COLUMN_EDADUSU);
            int generoIndex = cursorUsuario.getColumnIndex(COLUMN_GENEROUSU);
            int latitudIndex = cursorUsuario.getColumnIndex(COLUMN_LATITUDUSU);
            int longitudIndex = cursorUsuario.getColumnIndex(COLUMN_LONGITUDUSU);
            int imgProfileIndex = cursorUsuario.getColumnIndex(COLUMN_IMGPROFILE);

            if (idUsuIndex != -1 && nombreUsuIndex != -1 && passIndex != -1 &&
                    mailUsuIndex != -1 && apellidosUsuIndex != -1 && edadUsuIndex != -1 &&
                    generoIndex != -1 && latitudIndex != -1 && longitudIndex != -1) {
                int idUsu = cursorUsuario.getInt(idUsuIndex);
                String nombreUsu = cursorUsuario.getString(nombreUsuIndex);
                String pass = cursorUsuario.getString(passIndex);
                String mailUsu = cursorUsuario.getString(mailUsuIndex);
                String apellidosUsu = cursorUsuario.getString(apellidosUsuIndex);
                String edadUsu = cursorUsuario.getString(edadUsuIndex);
                String genero = cursorUsuario.getString(generoIndex);
                double latitud = cursorUsuario.getDouble(latitudIndex);
                double longitud = cursorUsuario.getDouble(longitudIndex);
                String imgProfile = cursorUsuario.getString(imgProfileIndex);

                // Consulta a la tabla MASCOTA
                Cursor cursorMascota = db.query(TABLEMASC_NAME, null, COLUMN_IDHUMANO + "=?",
                        new String[]{String.valueOf(idUsuario)}, null, null, null);

                if (cursorMascota != null && cursorMascota.moveToFirst()) {
                    // Obtén los datos de la mascota
                    int mascotaIdIndex = cursorMascota.getColumnIndex(COLUMN_IDMASCO);
                    int nombreMascotaIndex = cursorMascota.getColumnIndex(COLUMN_NOMMASCO);
                    int edadMascotaIndex = cursorMascota.getColumnIndex(COLUMN_EDADMASCO);
                    int sexoMascotaIndex = cursorMascota.getColumnIndex(COLUMN_GENEROMASCO);
                    int descripcionMascotaIndex = cursorMascota.getColumnIndex(COLUMN_DESCRIPCIONMASCO);
                    int fotoMascotaIndex = cursorMascota.getColumnIndex(COLUMN_FOTOMASCO);
                    int razaIndex = cursorMascota.getColumnIndex(COLUMN_NOMRAZA);
                    int relacionMascotasIndex = cursorMascota.getColumnIndex(COLUMN_RELACIONMASCO);
                    int relacionHumanosIndex = cursorMascota.getColumnIndex(COLUMN_RELACIONPERSONAS);
                    int idHumanoIndex = cursorMascota.getColumnIndex(COLUMN_IDHUMANO);


                    if (mascotaIdIndex != -1 && nombreMascotaIndex != -1 && edadMascotaIndex != -1 &&
                            sexoMascotaIndex != -1 && descripcionMascotaIndex != -1 && fotoMascotaIndex != -1 &&
                            razaIndex != -1 && relacionMascotasIndex != -1 && relacionHumanosIndex != -1 &&
                            idHumanoIndex != -1) {

                        int mascotaId = cursorMascota.getInt(mascotaIdIndex);
                        String nombreMascota = cursorMascota.getString(nombreMascotaIndex);
                        String edadMascota = cursorMascota.getString(edadMascotaIndex);
                        String sexoMascota = cursorMascota.getString(sexoMascotaIndex);
                        String descripcionMascota = cursorMascota.getString(descripcionMascotaIndex);
                        String fotoMascota = cursorMascota.getString(fotoMascotaIndex);
                        String raza = cursorMascota.getString(razaIndex);
                        String relacionMascotas = cursorMascota.getString(relacionMascotasIndex);
                        String relacionHumanos = cursorMascota.getString(relacionHumanosIndex);


                        int idHumano = cursorMascota.getInt(idHumanoIndex);

                        // Crea un objeto Usuario2 con los datos de ambas tablas
                        usuario2 = new Usuario2(idUsu, new Usuario2.Ubi(latitud, longitud), nombreUsu, apellidosUsu, mailUsu, pass, genero, edadUsu, mascotaId, nombreMascota, edadMascota, sexoMascota, descripcionMascota, fotoMascota, relacionHumanos, relacionMascotas, idHumano, raza, "Mediano", imgProfile);
                        Log.d("usuario", usuario2.toString());
                    }

                    // Cierra los cursores
                    if (cursorUsuario != null) {
                        cursorUsuario.close();
                    }
                    if (cursorMascota != null) {
                        cursorMascota.close();
                    }
                }
            }
        }


        // Cierra la conexión
        db.close();

        return usuario2;
    }

    private boolean usuarioNoExiste(SQLiteDatabase db, int idUsuario) {
        String query = "SELECT * FROM " + TABLEUSU_NAME + " WHERE " + COLUMN_IDUSU + " = ?";
        Log.d("pruebaQuery", query);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idUsuario)});
        boolean noExiste = (cursor.getCount() == 0);
        cursor.close();
        return noExiste;
    }

    public void borrarTodosLosDatos() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Borra todos los registros de la tabla MASCOTA
        db.delete(TABLEMASC_NAME, null, null);
        Log.d("pruebaDelete", "Mascotas borradas");

        // Borra todos los registros de la tabla USUARIO
        db.delete(TABLEUSU_NAME, null, null);
        Log.d("pruebaDelete", "Usuarios borrados");

        // Cierra la conexión
        db.close();
    }

}
