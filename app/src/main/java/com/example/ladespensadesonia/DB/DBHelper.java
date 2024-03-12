package com.example.ladespensadesonia.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "despensa.db";
    public static final String TABLE_USUARIOS = "t_usuarios";
    public static final String TABLE_ARTICULOS = "t_articulos";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_USUARIOS+"(" +
                "nombreUsuario TEXT PRIMARY KEY NOT NULL," +
                "contrasenia TEXT NOT NULL);");

        db.execSQL("CREATE TABLE "+TABLE_ARTICULOS+"(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "propietario TEXT NOT NULL," +
                "nombreProducto TEXT NOT NULL," +
                "cantAct DECIMAL(8,2)," +
                "cantMin DECIMAL(8,2)," +
                "fechaUltCompra DATE," +
                "fechaCaducidadTop DATE)");

        db.execSQL("INSERT INTO "+TABLE_USUARIOS+"(" +
                "nombreUsuario, contrasenia) VALUES(" +
                "'sonia.angulo', 'sonia123')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_ARTICULOS);
        onCreate(db);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase dbLogin = this.getReadableDatabase();
        Cursor cursor = dbLogin.query(TABLE_USUARIOS,
                new String[]{"nombreUsuario"},
                "nombreUsuario = ? AND contrasenia = ?",
                new String[]{username,password},
                null, null, null);
        boolean loginCorrecto = cursor.moveToFirst();
        cursor.close();
        return loginCorrecto;
    }
}
