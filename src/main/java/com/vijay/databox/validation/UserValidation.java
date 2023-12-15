package com.vijay.databox.validation;

import java.util.regex.Pattern;

public class UserValidation {
    public static boolean userLength(String name) {
        return name.length() >= 3 && name.length() < 15;
    }
    public static boolean email(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && Pattern.matches(emailRegex, email);
    }
    public static boolean password(String pass) {
        int len = pass.length();
        return len >= 4 && len <= 20;
    }
}
