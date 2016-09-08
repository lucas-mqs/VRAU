package br.com.sovrau.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lucas on 26/04/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    protected static final String DATABASE = "VRAUDB";
    protected static final int VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, DATABASE, null, VERSION);
    }
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE if not exists Usuario(\n" +
                        "\t_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                        "\tnmUsuario TEXT,\n" +
                        "\temailUsuario TEXT,\n" +
                        "\tsenhaUsuario TEXT,\n" +
                        "\tflAtivo CHAR);");
        db.execSQL("CREATE TABLE Marca(\n" +
                "\t_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\tnmMarca TEXT);");
        db.execSQL("CREATE TABLE if not exists Moto(\n" +
                "\t_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "\tidMarca INTEGER,\n" +
                "\tidUsuario INTEGER,\n" +
                "\tnmMoto TEXT,\n" +
                "\tcilindradasMoto INTEGER,\n" +
                "\tnmModelo TEXT,\n" +
                "\todometro INTEGER,\n" +
                "\ttanque INTEGER,\n" +
                "\tanoFabricacao INTEGER,\n" +
                "\tplaca TEXT,\n" +
                "\tobs TEXT);");
        db.execSQL("INSERT INTO Marca(nmMarca) VALUES (\"Honda\"),\n" +
                   "(\"Yamaha\"),\n" +
                   "(\"Suzuki\"),\n" +
                   "(\"Kawasaki\"),\n" +
                   "(\"Harley Davidson\"),\n" +
                   "(\"BMW\"),\n" +
                   "(\"Ducati\"),\n" +
                   "(\"Triumph\"),\n" +
                   "(\"Aprilla\");");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

