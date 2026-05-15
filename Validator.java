package com.grademanager.utils;

import java.util.regex.Pattern;

public class Validator {
    
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern ID_PATTERN = 
        Pattern.compile("^[A-Za-z0-9]{3,10}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidStudentId(String id) {
        return id != null && ID_PATTERN.matcher(id).matches();
    }
    
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 100;
    }
    
    public static boolean isValidScore(double score, double maxScore) {
        return score >= 0 && score <= maxScore;
    }
    
    public static String validateAndCleanInput(String input) {
        if (input == null) return "";
        return input.trim();
    }
}
