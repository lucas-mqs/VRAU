package br.com.sovrau.utilities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Lucas on 08/05/2016.
 */
public class CodeUtils {
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
}
