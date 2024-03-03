package com.mid_term.springecommerce.Utils;

import com.mid_term.springecommerce.DTO.StaffAndShipperDTO;
import com.mid_term.springecommerce.Models.Entity.Cart;
import com.mid_term.springecommerce.Models.Entity.StaffAndShipper;
import com.mid_term.springecommerce.Models.Entity.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Random;

public class Utils {
    public static User userLogin;
    public static StaffAndShipperDTO staffAndShipperLogin;

    public static Cart cart;
    public static int totalProductInCart = 0;
    public static String userNameLogin = "";
    public static Long idUserLogin = 3L;
    public static boolean isLogin = false;
    static SecureRandom random = new SecureRandom();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }
    public static String GetUserNameByEmail(String email) {
        int index = email.indexOf("@");
        return email.substring(0,index);
    }

    //random token
    public static String GenerateRandomToken(int length) {
        StringBuilder token = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(randomIndex));
        }
        return token.toString();
    }

    //hash password
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Verify a password
    public static boolean verifyPassword(String password, String hashedPassword) {
        // Check if the provided password matches the hashed password
        return BCrypt.checkpw(password, hashedPassword);
    }

    //generate barcode
    public static int generateSixDigitNumber() {
        // Create a random number between 100000 and 999999 (inclusive)
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }
}