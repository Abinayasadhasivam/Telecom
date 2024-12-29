package utils;

public class TokenManager {
    private static String token;

    public static String getToken() {
        return token;
    }

    public static void setToken(String tokenValue) {
        token = tokenValue;
    }
}