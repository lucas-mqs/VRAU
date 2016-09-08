package br.com.sovrau.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Lucas on 03/05/2016.
 */
public class DatabaseUtils {
        protected SQLiteDatabase db;
        private Context context;

        public DatabaseUtils(Context context) {
            this.context = context;
        }

        protected void openDatabase() {
            try {
                DatabaseHelper var_SqliteHelper = new DatabaseHelper(context, DatabaseHelper.DATABASE, null, DatabaseHelper.VERSION);
                this.db = var_SqliteHelper.getWritableDatabase();
            } catch (Exception e) {
                Log.d("OPEN_DB", "Erro: " + e.getMessage());

            }
        }

        protected void closeDatabase() {
            if (this.db != null) {
                this.db.close();
            }
        }
}