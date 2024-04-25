package com.example.doginder6.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.doginder6.Objects.Mensaje;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chat_database";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SENDER_ID = "sender_id";
    private static final String COLUMN_RECEIVER_ID = "receiver_id";
    private static final String COLUMN_MESSAGE = "message";
    private static final String security = " IF NOT EXISTS";

    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGES +"(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_SENDER_ID + " INTEGER," +
            COLUMN_RECEIVER_ID + " INTEGER," +
            COLUMN_MESSAGE + " TEXT" +
            ")";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_MESSAGES + " ADD COLUMN " + COLUMN_RECEIVER_ID + " INTEGER");
        }
    }

    public void insertMessage(int senderId, int receiverId, String message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SENDER_ID, senderId);
        values.put(COLUMN_RECEIVER_ID, receiverId);
        values.put(COLUMN_MESSAGE, message);
        db.insert(TABLE_MESSAGES, null, values);

        db.close();

        Log.d("ChatDatabaseHelper", "Message inserted");
    }

    //funcion para borrar la tabla messages
    public void deleteTableMessages(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.close();
    }

    public List<Mensaje> getAllMessages(int senderId, int receiverId) {
        List<Mensaje> mensajes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define la consulta SQL para seleccionar mensajes según los IDs del remitente y el receptor
        String query = "SELECT " + COLUMN_SENDER_ID + ", " + COLUMN_RECEIVER_ID + ", " + COLUMN_MESSAGE +
                " FROM " + TABLE_MESSAGES +
                " WHERE (" + COLUMN_SENDER_ID + " = ? AND " + COLUMN_RECEIVER_ID + " = ?) OR " +
                "(" + COLUMN_SENDER_ID + " = ? AND " + COLUMN_RECEIVER_ID + " = ?)";

        // Parámetros para la consulta SQL
        String[] selectionArgs = {String.valueOf(senderId), String.valueOf(receiverId),
                String.valueOf(receiverId), String.valueOf(senderId)};

        // Ejecuta la consulta SQL y obtiene los mensajes correspondientes
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Lee los datos del mensaje de las columnas correspondientes
                int mensajeSenderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SENDER_ID));
                int mensajeReceiverId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECEIVER_ID));
                String message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE));

                // Crea un nuevo objeto Mensaje y agrégalo a la lista
                Mensaje mensaje = new Mensaje(mensajeSenderId, mensajeReceiverId, message);
                mensajes.add(mensaje);
            } while (cursor.moveToNext());
        }

        // Cierra el cursor y la base de datos
        cursor.close();
        db.close();

        return mensajes;
    }


    public void limpiarChat(int userId1, int userId2) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Define la cláusula WHERE para eliminar los mensajes entre los dos usuarios
        String whereClause = "(" + COLUMN_SENDER_ID + " = ? AND " + COLUMN_RECEIVER_ID + " = ?) OR " +
                "(" + COLUMN_SENDER_ID + " = ? AND " + COLUMN_RECEIVER_ID + " = ?)";

        // Parámetros para la cláusula WHERE
        String[] whereArgs = {String.valueOf(userId1), String.valueOf(userId2),
                String.valueOf(userId2), String.valueOf(userId1)};

        // Elimina los mensajes que cumplan con la cláusula WHERE
        db.delete(TABLE_MESSAGES, whereClause, whereArgs);

        // Cierra la base de datos
        db.close();

        Log.d("ChatDatabaseHelper", "Chat limpiado");
    }

}


