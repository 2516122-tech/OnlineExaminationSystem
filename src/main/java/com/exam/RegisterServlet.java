package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String name = request.getParameter("student_name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (name == null || email == null || password == null ||
            name.trim().isEmpty() || email.trim().isEmpty() ||
            password.length() < 8) {

            response.getWriter().println(
                "<h3>Invalid registration details!</h3>");
            return;
        }

        String sql = "INSERT INTO exam_students " +
                "(student_name, email, password_hash) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                response.getWriter().println(
                    "<h3>Database connection failed!</h3>");
                return;
            }

            String hashedPassword = hashPassword(password);

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, name.trim());
                ps.setString(2, email.trim().toLowerCase());
                ps.setString(3, hashedPassword);

                int rows = ps.executeUpdate();

                if (rows > 0) {

                    response.getWriter().println(
                        "<h2>Registration Successful!</h2>");

                    response.getWriter().println(
                        "<a href='login.html'>Login Now</a>");
                }
            }

        } catch (SQLException e) {

            if ("23000".equals(e.getSQLState())) {
                response.getWriter().println(
                    "<h3>Email already registered!</h3>");
            } else {
                e.printStackTrace();
                response.getWriter().println(
                    "<h3>Registration failed. Please try again.</h3>");
            }

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().println(
                "<h3>Password processing failed!</h3>");
        }
    }

    private String hashPassword(String password) throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        KeySpec spec = new PBEKeySpec(
            password.toCharArray(), salt, 120000, 256);

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        return Base64.getEncoder().encodeToString(salt)
                + ":" +
                Base64.getEncoder().encodeToString(hash);
    }
}