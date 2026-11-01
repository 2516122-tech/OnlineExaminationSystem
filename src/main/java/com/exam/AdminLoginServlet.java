package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String sql = "SELECT admin_id, password_hash "
                   + "FROM exam_admins WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String stored = rs.getString("password_hash");

                String[] parts = stored.split(":");

                byte[] salt = Base64.getDecoder()
                                    .decode(parts[0]);

                byte[] originalHash = Base64.getDecoder()
                                             .decode(parts[1]);

                KeySpec spec = new PBEKeySpec(
                    password.toCharArray(), salt, 120000, 256
                );

                byte[] enteredHash = SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

                if (java.security.MessageDigest.isEqual(
                        originalHash, enteredHash)) {

                    HttpSession session = request.getSession();

                    session.setAttribute(
                        "adminId", rs.getInt("admin_id")
                    );

                    session.setAttribute("adminName", username);

                    response.sendRedirect("admin-dashboard.jsp");

                } else {

                    response.sendRedirect(
                        "admin-login.html?error=1"
                    );
                }

            } else {

                response.sendRedirect(
                    "admin-login.html?error=1"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.getWriter().println(
                "Admin login failed. Please try again."
            );
        }
    }
}