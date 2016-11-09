package br.com.sovrau.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.sovrau.constants.Constants;
import br.com.sovrau.dto.AlertaDTO;
import br.com.sovrau.dto.MotoDTO;
import br.com.sovrau.dto.PercursoDTO;


/**
 * Created by Lucas on 08/05/2016.
 */
public class CodeUtils {
    public static String PREF = "br.com.sovrau.PREF";

    private static final CodeUtils INSTANCE = new CodeUtils();

    public static CodeUtils getInstance(){
        return INSTANCE;
    }

    private CodeUtils(){}

    public String formatDateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
    public Date formatStringToDate(String str) throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.parse(str);
    }
    //Converte a velocidade de metros/segundo para quilometros/hora
    public double convertMPStoKMH(double metersPerSecond){
        return (metersPerSecond*3600) / 1000;
    }

    public boolean isGPSProviderEnabled(LocationManager locationManager){
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            return false;
        }
    }
    public String getGenericID(String toConcat){
        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
        return dt.format(new Date()).concat(toConcat.replace(" ", ""));
    }
    public void saveSP(Context context, String key, String value ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }
    public String getSP(Context context, String key ){
        SharedPreferences sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String token = sp.getString(key, "");
        return( token );
    }
    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lon1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lon2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB-lonA)) +
                (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        double dist = ang *6371;
        return dist;
    }
    public String getTimeFormatted(long millis){
        return String.format("%02d min, %02d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
    public List<MotoDTO> parseMapToListMoto(Map<String, Object> mapMotos) {
        List<MotoDTO> listMotos = new ArrayList<>();
        for(Map.Entry<String, Object> entry : mapMotos.entrySet()){
            listMotos.add(parseMapToMotoDTO((HashMap)entry.getValue()));
        }
        return listMotos;
    }
    private MotoDTO parseMapToMotoDTO(Map mapMotos) {
        MotoDTO moto = new MotoDTO();
        //moto.setMonitorarFreios(mapMotos.get(Constants.MONITORAR_FREIOS));
        moto.setOdometro(Long.valueOf(mapMotos.get(Constants.ODOMETRO).toString()));
        moto.setCilindradasMoto(Integer.valueOf((mapMotos).get(Constants.CILINDRADAS).toString()));
        //moto.setLocalCelular(mapMotos.get(Constants.LOCAL_CELULAR).toString());
        //moto.setMonitorarCombustivel(mapMotos.get(Constants.MONITORAR_COMBUSTIVEL).toString());
        moto.setNmModelo(mapMotos.get(Constants.MODELO).toString());
        moto.setNmMarca(mapMotos.get(Constants.MARCA).toString());
        moto.setNmMoto(mapMotos.get(Constants.NOME).toString());
        //moto.setMonitorarOleo(mapMotos.get(Constants.MONITORAR_OLEO).toString());
        moto.setAnoFabricacao(Integer.valueOf(mapMotos.get(Constants.ANO).toString()));
        //moto.setMonitorarLiquido(mapMotos.get(Constants.MONITORAR_LIQUIDO).toString());
        moto.setIdMoto(mapMotos.get(Constants.ID).toString());
        moto.setObs(mapMotos.get(Constants.OBS).toString());
        moto.setTanque(Integer.valueOf(mapMotos.get(Constants.TANQUE).toString()));
        //moto.setMonitorarCxDirecao(mapMotos.get(Constants.MONITORAR_CX_DIRECAO).toString());
        //moto.setMonitorarPneus(mapMotos.get(Constants.MONITORAR_PNEUS).toString());
        //moto.setMonitorarPastilhas(mapMotos.get(Constants.MONITORAR_PASTILHAS).toString());
        moto.setPlaca(mapMotos.get(Constants.PLACA).toString());

        return moto;
    }
    public List<PercursoDTO> parseMapToListPercurso(Map<String, Object> mapPercursos) {
        List<PercursoDTO> listPercursos = new ArrayList<>();
        for(Map.Entry<String, Object> entry : mapPercursos.entrySet()){
            listPercursos.add(parseMapToPercursoDTO((HashMap)entry.getValue()));
        }
        return listPercursos;
    }
    private PercursoDTO parseMapToPercursoDTO(Map mapPercurso) {
        PercursoDTO percurso = new PercursoDTO();
        percurso.setDetectarFimPercurso(Boolean.parseBoolean(mapPercurso.get(Constants.DETECTAR_FIM_PERCURSO).toString().trim()));
        percurso.setEnderecoFinal(mapPercurso.get(Constants.FINAL_PERCURSO).toString());
        percurso.setEnderecoInicial(mapPercurso.get(Constants.INICIO_PERCURSO).toString());
        percurso.setId(mapPercurso.get(Constants.ID).toString());
        percurso.setMotivo(mapPercurso.get(Constants.MOTIVO).toString());
        percurso.setMedirAuto(Boolean.parseBoolean(mapPercurso.get(Constants.MEDIR_AUTO).toString()));
        percurso.setOdometroInicial(Long.valueOf(mapPercurso.get(Constants.ODOMETRO_INICIAL).toString().trim()));
        percurso.setTipoPercurso(mapPercurso.get(Constants.TIPO_PERCURSO).toString());

        return percurso;
    }
    public List<AlertaDTO> parseMapToListAlerta(Map<String,Object> mapAlertas) {
        List<AlertaDTO> listAlertas = new ArrayList<>();
        for(Map.Entry<String, Object> entry : mapAlertas.entrySet()){
            listAlertas.add(parseMapToAlertaDTO((HashMap)entry.getValue()));
        }
        return listAlertas;
    }
    private AlertaDTO parseMapToAlertaDTO(Map mapAlerta) {
        AlertaDTO alerta = new AlertaDTO();
        alerta.setIdAlerta(mapAlerta.get(Constants.ID).toString());
        alerta.setPorcentagemAlerta(Double.valueOf(mapAlerta.get(Constants.PERCENTUAL_AERTA).toString()));
        alerta.setPorcentagemTotal(Double.parseDouble(mapAlerta.get(Constants.PERCENTUAL_ATUAL).toString()));
        alerta.setTipoAlerta(Integer.valueOf(mapAlerta.get(Constants.TIPO_ALERTA).toString()));
        alerta.setQtdeKmFalta(Long.valueOf(mapAlerta.get(Constants.KM_FALTANTES).toString()));
        alerta.setQtdeKmRodado(Long.valueOf(mapAlerta.get(Constants.KM_RODADOS).toString()));

        return alerta;
    }
}
