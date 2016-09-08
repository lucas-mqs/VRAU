package br.com.sovrau.veiculo;

import android.content.ContentValues;
import android.content.Context;

import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.providers.DatabaseUtils;

/**
 * Created by Lucas on 08/05/2016.
 */
public class MotoDAO extends DatabaseUtils{
    private static final String TABLE_MOTO = "Moto";
    private static final String ID_MOTO = "_id";

    public MotoDAO(Context context) {
        super(context);
    }

    public long insertMoto(MotoDTO motoDTO){
        long result = -1;
        openDatabase();
        ContentValues valuesMoto = new ContentValues();
        valuesMoto.put("idMarca",motoDTO.getIdMarca());
        valuesMoto.put("idUsuario",motoDTO.getIdUsuario());
        valuesMoto.put("nmMoto",motoDTO.getNmMoto());
        valuesMoto.put("cilindradasMoto",motoDTO.getCilindradasMoto());
        valuesMoto.put("nmModelo",motoDTO.getNmModelo());
        valuesMoto.put("tanque",motoDTO.getTanque());
        valuesMoto.put("anoFabricacao",motoDTO.getAnoFabricacao());
        valuesMoto.put("placa",motoDTO.getPlaca());
        valuesMoto.put("obs",motoDTO.getObs());

        result = db.insert(TABLE_MOTO, null, valuesMoto);
        closeDatabase();
        return result;
    }
    public int deleteMoto(long idUsuario) {
        openDatabase();
        String args[] = new String[]{String.valueOf(idUsuario)};
        String FILTER = ID_MOTO + " = ?";
        int result = db.delete(TABLE_MOTO, FILTER, args);
        closeDatabase();
        return result;
    }
    public int updateMoto(MotoDTO motoDTO) {
        openDatabase();
        ContentValues valuesMoto = new ContentValues();
        valuesMoto.put("idMarca",motoDTO.getIdMarca());
        valuesMoto.put("idUsuario",motoDTO.getIdUsuario());
        valuesMoto.put("nmMoto",motoDTO.getNmMoto());
        valuesMoto.put("cilindradasMoto",motoDTO.getCilindradasMoto());
        valuesMoto.put("nmModelo",motoDTO.getNmModelo());
        valuesMoto.put("tanque",motoDTO.getTanque());
        valuesMoto.put("anoFabricacao",motoDTO.getAnoFabricacao());
        valuesMoto.put("placa",motoDTO.getPlaca());
        valuesMoto.put("obs",motoDTO.getObs());
        String args[] = new String[]{String.valueOf(motoDTO.getIdMoto())};
        String FILTER = ID_MOTO + " = ?";
        int result = db.update(TABLE_MOTO, valuesMoto, FILTER, args);
        closeDatabase();
        return result;
    }
}
