package com.armhansa.app.grandwarp.validate;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class PatternValidation {

    public static boolean isNameValid(String name, Context context) {
        String expression = "[A-Z0-9ก-ฮ]{2,20}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        if(!matcher.matches()) Toast.makeText(context, "Name is invalid! (2 - 20 chars).", Toast.LENGTH_LONG).show();
        return matcher.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber, Context context) {
        String expression = "0[0-9]{9}";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.matches())
            Toast.makeText(context, "PhoneNumber is invalid! (10 number ex. 0999999999).", Toast.LENGTH_LONG).show();
        return matcher.matches();
    }

    public static boolean isTimeValid(String time, Context context) {
        String expression = "[0-2][0-9].[0-5][0-9]";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(time);
        if(!matcher.matches()) {
            Toast.makeText(context, "Time is invalid! (ex. 15.00).", Toast.LENGTH_LONG).show();
            return false;
        } else {
            String times[] = time.split(".");
            int hour = parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);

            if (hour >= 0 && hour < 24
                    && minute >= 0 && minute < 60) {
                return true;
            } else {
                Toast.makeText(context, "Time is invalid! (ex. 00.00 - 23.59)", Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    public static boolean isEmailValid(String email, Context context) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
