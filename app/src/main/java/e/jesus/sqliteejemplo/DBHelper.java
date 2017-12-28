package e.jesus.sqliteejemplo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jesus on 20/12/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "productos";
    public static final int DATABASE_VERSION = 3;

    public DBHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tabla = "CREATE TABLE producto(_id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, costo INTEGER, venta INTEGER)";
        db.execSQL(tabla);

        ContentValues cv = new ContentValues();
        cv.put("nombre", "Tenis");
        cv.put("costo", 1500);
        cv.put("venta", 2000);
        db.insert("producto", "nombre", cv);

        cv.put("nombre", "Celular");
        cv.put("costo", 6000);
        cv.put("venta", 7000);
        db.insert("producto", "nombre", cv);

        cv.put("nombre", "Balon");
        cv.put("costo", 300);
        cv.put("venta", 500);
        db.insert("producto", "nombre", cv);

        cv.put("nombre", "Donas");
        cv.put("costo", 25);
        cv.put("venta", 30);
        db.insert("producto", "nombre", cv);

        cv.put("nombre", "Perro");
        cv.put("costo", 10);
        cv.put("venta", 20);
        db.insert("producto", "nombre", cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS producto");
        onCreate(db);
    }
}
