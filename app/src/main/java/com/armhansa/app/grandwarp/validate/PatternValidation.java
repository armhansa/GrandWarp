package com.armhansa.app.grandwarp.validate;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternValidation {

    public static boolean isNameValid(String name, Context context) {
        String expression = "[A-Z0-9]{2,12}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()) Toast.makeText(context, "Name is invalid! (2 - 12 chars).", Toast.LENGTH_LONG).show();
        return matcher.matches();
    }

    public static boolean isPhoneNumber(String phoneNumber, Context context) {
        String expression = "0[0-9]{9}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.matches())
            Toast.makeText(context, "PhoneNumber is invalid! (10 number ex. 0999999999).", Toast.LENGTH_LONG).show();
        return matcher.matches();
    }

    public static boolean isEmailValid(String email, Context context) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
