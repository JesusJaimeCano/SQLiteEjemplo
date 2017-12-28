package e.jesus.sqliteejemplo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Jesus on 20/12/2017.
 */

public class ActivityDosCrear extends AppCompatActivity {

    EditText crearNombreProducto, crearCostoProducto, crearVentaProducto;
    SQLiteDatabase db;
    Button agregarRegistroButton;
    private Cursor cursor;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dos_crear_registro);

        crearNombreProducto = findViewById(R.id.crearNombreProducto);
        crearCostoProducto = findViewById(R.id.crearCostoProducto);
        crearVentaProducto = findViewById(R.id.crearVentaProducto);
        agregarRegistroButton = findViewById(R.id.agregarRegistroBoton);

        db = new DBHelper(this).getWritableDatabase();
        cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY nombre", null);

        agregarRegistroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cvNuevo = new ContentValues();
                cvNuevo.put("nombre", crearNombreProducto.getText().toString());
                cvNuevo.put("costo", crearCostoProducto.getText().toString());
                cvNuevo.put("venta", crearVentaProducto.getText().toString());
                db.insert("producto", "nombre",cvNuevo);
                finish();
            }
        });






    }
}
