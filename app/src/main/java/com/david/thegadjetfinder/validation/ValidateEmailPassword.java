package com.david.thegadjetfinder.validation;

import android.util.Patterns;

public interface ValidateEmailPassword {
    public default boolean validateEmail(String email){
        if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }else{
            return false;
        }
    }

    public default boolean validatePassword(String password){
        if(!password.isEmpty() && password.length() >=6 && password.length() <=12){
            return true;
        }else{
            return false;
        }
    }
}
