package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/QuestionManagementServlet")
public class QuestionManagementServlet extends HttpServlet {

    // Display all questions
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendRedirect("admin-login.html");
            return;
        }

        ArrayList<HashMap<String, Object>> questions =
            new ArrayList<>();

        String sql = "SELECT * FROM questions ORDER BY question_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HashMap<String, Object> q = new HashMap<>();

                q.put("id", rs.getInt("question_id"));
                q.put("question", rs.getString("question_text"));
                q.put("a", rs.getString("option_a"));
                q.put("b", rs.getString("option_b"));
                q.put("c", rs.getString("option_c"));
                q.put("d", rs.getString("option_d"));
                q.put("correct", rs.getString("correct_option"));

                questions.add(q);
            }

            request.setAttribute("questions", questions);

            request.getRequestDispatcher("manage-questions.jsp")
                   .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(500, "Unable to load questions.");
        }
    }

    // Delete a question
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendRedirect("admin-login.html");
            return;
        }

        String id = request.getParameter("question_id");

        if (id == null || !id.matches("\\d+")) {
            response.sendError(400, "Invalid question ID.");
            return;
        }

        String sql = "DELETE FROM questions WHERE question_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(id));

            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                response.sendRedirect(
                    "QuestionManagementServlet?deleted=1"
                );
            } else {
                response.sendRedirect(
                    "QuestionManagementServlet?deleted=0"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(500, "Unable to delete question.");
        }
    }
}