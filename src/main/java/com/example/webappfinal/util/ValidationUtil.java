package com.example.webappfinal.util;

import org.springframework.stereotype.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This class just checks if a password is valid using a Regex.
@Component
public class ValidationUtil {
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public boolean isValidPassword(String password) {
        Matcher passwordMatcher = PASSWORD_PATTERN.matcher(password);
        return passwordMatcher.matches();
    }
}
