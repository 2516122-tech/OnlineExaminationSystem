package com.exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CreateAdmin {

    public static void main(String[] args) {

        String username = "admin";
        String password = "Admin@123";

        try {
            // Generate random salt
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            // Hash password
            KeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt, 120000, 256
            );

            byte[] hash = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec)
                .getEncoded();

            String storedPassword =
                Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(hash);

            // Store admin in database
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO exam_admins "
                       + "(username, password_hash) VALUES (?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, storedPassword);

            ps.executeUpdate();

            System.out.println("Admin Created Successfully!");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}