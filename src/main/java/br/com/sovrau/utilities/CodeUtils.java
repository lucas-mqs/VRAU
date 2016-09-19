package br.com.sovrau.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
    public float convertMPStoKMH(float metersPerSecond){
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
        return dt.format(new Date()).concat(toConcat);
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
}
