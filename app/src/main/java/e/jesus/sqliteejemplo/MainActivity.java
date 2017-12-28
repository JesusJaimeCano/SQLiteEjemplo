package e.jesus.sqliteejemplo;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;

    private ListView listView;
    private ListAdapter adapter;
    private Cursor cursor;
    private Button ordenarNombre, ordenarCosto, ordenarVenta, buscarButton;
    private EditText busquedaEditText;


    public static final int BORRAR_ID = Menu.FIRST;
    public static final int ACTUALIZAR_ID = Menu.FIRST+1;
    public static final int AGREGAR = Menu.FIRST+2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this).getWritableDatabase();
        cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY nombre", null);

        ordenarCosto = findViewById(R.id.ordenarCostoBoton);
        ordenarVenta = findViewById(R.id.ordenarVentaBoton);
        ordenarNombre = findViewById(R.id.ordenarNombreBoton);
        buscarButton = findViewById(R.id.buscarButton);
        busquedaEditText = findViewById(R.id.busquedaEditText);


        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busquedaBD = busquedaEditText.getText().toString();


                cursor = db.rawQuery("SELECT * FROM producto WHERE nombre like ?", new String[]{busquedaBD + "%"} );
                ((SimpleCursorAdapter)adapter).changeCursor(cursor);
            }
        });

        ordenarCosto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY costo", null);
                ((SimpleCursorAdapter)adapter).changeCursor(cursor);
            }
        });

        ordenarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY venta", null);
                ((SimpleCursorAdapter)adapter).changeCursor(cursor);
            }
        });

        ordenarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY nombre", null);
                ((SimpleCursorAdapter)adapter).changeCursor(cursor);
            }
        });



        adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, new String[]{"nombre", "costo", "venta"}, new int[]{R.id.nombreProducto, R.id.precioCostoProducto, R.id.precioVentaProducto}, 0);
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, ACTUALIZAR_ID, Menu.NONE, "Actualizar Producto");
        menu.add(Menu.NONE, BORRAR_ID, Menu.NONE, "Eliminar Producto");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo registro =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case BORRAR_ID:

                Borrar(registro.id);

                break;
            case ACTUALIZAR_ID:
                Actualizar(registro.id);
                break;
        }

        return true;
    }

    public void Borrar(final Long rowid){
        if(rowid > 0){
            new AlertDialog.Builder(this).setTitle("¿Estas seguro de borrar el producto?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] args ={String.valueOf(rowid)};
                    db.delete("producto", "_id=?", args);
                    cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY costo", null);
                    ((SimpleCursorAdapter)adapter).changeCursor(cursor);
                }
            }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

        }
    }

    public void Actualizar(final Long rowid){
        Cursor cActualizar = db.rawQuery("SELECT nombre, costo, venta FROM producto WHERE _id=?", new String[]{String.valueOf(rowid)});
        cActualizar.moveToFirst();

        String nombre = cActualizar.getString(cActualizar.getColumnIndex("nombre"));
        String costo = cActualizar.getString(cActualizar.getColumnIndex("costo"));
        String venta = cActualizar.getString(cActualizar.getColumnIndex("venta"));

        LayoutInflater inflater = LayoutInflater.from(this);
        View actualizarView = inflater.inflate(R.layout.actualizar, null);
        final EditText nombreET = actualizarView.findViewById(R.id.nuevoNombreProducto);
        final EditText costoET = actualizarView.findViewById(R.id.nuevoCostoProducto);
        final EditText ventaET = actualizarView.findViewById(R.id.nuevoVentaProducto);
         nombreET.setText(nombre);
         costoET.setText(costo);
         ventaET.setText(venta);

        if(rowid > 0){
            new AlertDialog.Builder(this).setTitle("Actualizar Prodcuto")
                    .setView(actualizarView).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ContentValues cv = new ContentValues();
                    cv.put("nombre", nombreET.getText().toString());
                    cv.put("costo", costoET.getText().toString());
                    cv.put("venta", ventaET.getText().toString());
                    db.update("producto", cv, "_id=?", new String[]{String.valueOf(rowid)} );
                    cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY nombre", null);
                    ((SimpleCursorAdapter)adapter).changeCursor(cursor);
                }
            })
                    .show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, AGREGAR, Menu.NONE, "Agregar nuevo Producto");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case AGREGAR:

                Intent intent = new Intent(MainActivity.this, ActivityDosCrear.class);
                startActivity(intent);

                break;
        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        cursor = db.rawQuery("SELECT _id, nombre, costo, venta FROM producto ORDER BY costo", null);
        ((SimpleCursorAdapter)adapter).changeCursor(cursor);
    }
}
