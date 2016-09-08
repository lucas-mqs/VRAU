package br.com.sovrau.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import br.com.sovrau.dto.UsuarioDTO;
import br.com.sovrau.providers.DatabaseUtils;


/**
 * Created by Lucas on 03/05/2016.
 */
public class UserDAO extends DatabaseUtils {
    private static final String TABLE_USER = "Usuario";
    private static final String ID_USUARIO = "_id";

    public UserDAO(Context context) {
        super(context);
    }

    public long insertUser(UsuarioDTO usuarioDTO) {
        long result = -1;
        openDatabase();
        if(isUserAvaliable(usuarioDTO.getEmail())) {
            ContentValues values = new ContentValues();
            values.put("nmUsuario", usuarioDTO.getNome());
            values.put("emailUsuario", usuarioDTO.getEmail());
            //TODO: Criptografar a senha antes de inserir na base
            values.put("senhaUsuario", usuarioDTO.getSenha());
            values.put("flAtivo", "S");

            result = db.insert(TABLE_USER, null, values);
        }
        closeDatabase();
        return result;
    }

    public int updateUser(UsuarioDTO usuarioDTO) {
        openDatabase();
        ContentValues values = new ContentValues();
        values.put("nmUsuario", usuarioDTO.getNome());
        values.put("emailUsuario", usuarioDTO.getEmail());
        String args[] = new String[]{String.valueOf(usuarioDTO.getIdUSuario())};
        String FILTER = ID_USUARIO + " = ?";
        int result = db.update(TABLE_USER, values, FILTER, args);
        closeDatabase();
        return result;
    }

    public int deleteUser(long idUsuario) {
        openDatabase();
        String args[] = new String[]{String.valueOf(idUsuario)};
        String FILTER = ID_USUARIO + " = ?";
        int result = db.delete(TABLE_USER, FILTER, args);
        closeDatabase();
        return result;
    }

    public UsuarioDTO getUserById(String idUsuario) {
        UsuarioDTO usuarioDTO = null;
        Cursor cursor = null;
        openDatabase();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM Usuario WHERE _id = ?");
            cursor = db.rawQuery(sb.toString(), new String[]{"" + idUsuario});
            if (cursor.moveToFirst()) {
                usuarioDTO = new UsuarioDTO();
                usuarioDTO.setIdUSuario(idUsuario);
                usuarioDTO.setNome(cursor.getString(cursor.getColumnIndex("_id")));
                usuarioDTO.setEmail(cursor.getString(cursor.getColumnIndex("emailUsuario")));
                usuarioDTO.setSenha(cursor.getString(cursor.getColumnIndex("senhaUsuario")));
            }
        } catch (Exception e) {
            Log.d("GET_USER_BY_ID", "Erro: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        closeDatabase();
        return usuarioDTO;
    }
    public UsuarioDTO getUserByLoginESenha(String login, String senha) {
        UsuarioDTO usuarioDTO = null;
        Cursor cursor = null;
        openDatabase();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT _id, nmUsuario, emailUsuario, senhaUsuario FROM Usuario WHERE emailUsuario = ?")
                    .append(" and senhaUsuario = ?");
            cursor = db.rawQuery(sb.toString(), new String[]{login, senha});
            if (cursor.moveToFirst()) {
                usuarioDTO = new UsuarioDTO();
                usuarioDTO.setIdUSuario(cursor.getString(cursor.getColumnIndex("_id")));
                usuarioDTO.setNome(cursor.getString(cursor.getColumnIndex("nmUsuario")));
                usuarioDTO.setEmail(cursor.getString(cursor.getColumnIndex("emailUsuario")));
                usuarioDTO.setSenha(cursor.getString(cursor.getColumnIndex("senhaUsuario")));
            }
        } catch (Exception e) {
            Log.d("GET_USER_BY_LOGIN_SENHA", "Erro: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        closeDatabase();
        return usuarioDTO;
    }
    public boolean isUserAvaliable(String email){
        Cursor cursor = null;
        boolean isAvaliable = false;
        try{
            StringBuilder sb = new StringBuilder();
            sb.append("select count(*) from Usuario where emailUsuario = ?");
            cursor = db.rawQuery(sb.toString(), new String[]{email.toLowerCase()});
            cursor.moveToFirst();
            if(cursor.getInt(0)== 0)
                isAvaliable = true;
        }catch(Exception e){
            Log.e("USER_AVALIABLE", e.getMessage());
        }
        finally {
            if(cursor != null)
                cursor.close();
            return isAvaliable;
        }
    }
}

