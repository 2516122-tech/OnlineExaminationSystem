package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.security.MessageDigest;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        String sql = "SELECT * FROM exam_students WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                response.sendError(500, "Database connection failed");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email.trim().toLowerCase());

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        String storedHash =
                                rs.getString("password_hash");

                        if (verifyPassword(password, storedHash)) {

                            HttpSession session =
                                    request.getSession();

                            session.setAttribute(
                                    "studentId",
                                    rs.getInt("student_id"));

                            session.setAttribute(
                                    "studentName",
                                    rs.getString("student_name"));

                            response.sendRedirect(
                                    request.getContextPath()
                                    + "/dashboard.jsp");

                        } else {

                            response.getWriter().println(
                                "<h3>Incorrect email or password!</h3>");
                        }

                    } else {

                        response.getWriter().println(
                            "<h3>Incorrect email or password!</h3>");
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    500, "Login processing failed");
        }
    }

    private boolean verifyPassword(
            String password, String storedHash) throws Exception {

        String[] parts = storedHash.split(":");

        if (parts.length != 2) {
            return false;
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);

        byte[] expectedHash =
                Base64.getDecoder().decode(parts[1]);

        KeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                120000,
                256);

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256");

        byte[] actualHash =
                factory.generateSecret(spec).getEncoded();

        return MessageDigest.isEqual(
                expectedHash, actualHash);
    }
}