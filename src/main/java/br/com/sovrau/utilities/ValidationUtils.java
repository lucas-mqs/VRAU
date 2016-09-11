package br.com.sovrau.utilities;

import android.util.Patterns;

/**
 * Created by Lucas on 26/04/2016.
 */
public class ValidationUtils {
    private static final ValidationUtils INSTANCE = new ValidationUtils();

    public static ValidationUtils getInstance(){
        return INSTANCE;
    }

     private ValidationUtils(){}

    public boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }
    public boolean isValidLength(String str, int length){
        return str.length() >= length;
    }
    public boolean isEqualsAndNotNull(String a, String b){
        return !isNullOrEmpty(a) && !isNullOrEmpty(b) && a.equals(b);
    }
    public boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
