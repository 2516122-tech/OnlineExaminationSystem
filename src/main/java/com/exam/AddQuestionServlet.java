package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AddQuestionServlet")
public class AddQuestionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check admin login
        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendRedirect("admin-login.html");
            return;
        }

        String question = request.getParameter("question_text");
        String a = request.getParameter("option_a");
        String b = request.getParameter("option_b");
        String c = request.getParameter("option_c");
        String d = request.getParameter("option_d");

        String correct = request.getParameter("correct_option");

        if (question == null || a == null || b == null ||
            c == null || d == null || correct == null ||
            question.trim().isEmpty() ||
            a.trim().isEmpty() || b.trim().isEmpty() ||
            c.trim().isEmpty() || d.trim().isEmpty() ||
            !correct.matches("[A-D]")) {

            response.sendError(400, "Invalid question details.");
            return;
        }

        String sql = "INSERT INTO questions "
                   + "(question_text, option_a, option_b, "
                   + "option_c, option_d, correct_option) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, question.trim());
            ps.setString(2, a.trim());
            ps.setString(3, b.trim());
            ps.setString(4, c.trim());
            ps.setString(5, d.trim());
            ps.setString(6, correct);

            ps.executeUpdate();

            response.sendRedirect("admin-dashboard.jsp?added=1");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                500, "Unable to add question. Check the server console."
            );
        }
    }
}