package com.example.da1_e_max.utils;

public class StringUtil {

    public static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isGoodField(String input) {
        return input != null && !input.isEmpty() && input.length() >= 6;
    }

    public static boolean isEmpty(String input) {
        return input == null || input.isEmpty() || ("").equals(input.trim());
    }

    public static String getDoubleNumber(int number) {
        if (number <10) {
            return "0" + number;
        } else return "" + number;
    }




//public static boolean isValidName(String name) {
//    // Example: Valid if the name contains only letters and spaces
//    return name.matches("[a-zA-Z ]+");
//}
//
//    public static boolean isValidPhoneNumber(String phoneNumber) {
//        // Example: Valid if the phone number contains only digits and has a length between 10 and 15
//        return phoneNumber.matches("\\d{10,15}");
//    }
//
//    public static boolean isValidEmail(String email) {
//        // Example: Valid if the email matches a simple pattern for demonstration
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
}
