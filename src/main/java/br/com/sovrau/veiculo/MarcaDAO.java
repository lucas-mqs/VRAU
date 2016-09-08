package br.com.sovrau.veiculo;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sovrau.dto.MarcaDTO;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.providers.DatabaseUtils;

/**
 * Created by Lucas on 08/05/2016.
 */
public class MarcaDAO extends DatabaseUtils {
    private static final String TABLE_MARCA = "Marca";
    private static final String ID_MARCA = "_id";

    public MarcaDAO(Context context) {
        super(context);
    }
    public Map<Long, String> getListMarcas(){
        Map<Long,String> mapMarcas = new HashMap<>();
        Cursor cursor = null;
        openDatabase();
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("select _id, nmMarca from Marca");
            cursor = db.rawQuery(sb.toString(), new String[]{});
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++){
                mapMarcas.put(cursor.getLong(0), cursor.getString(1));
                cursor.moveToNext();
            }
        } catch(Exception e){
            Log.e("GET_LIST_MARCAS", e.getMessage());
        } finally {
            if(cursor != null)
                cursor.close();
            closeDatabase();
            return mapMarcas;
        }
    }
}
