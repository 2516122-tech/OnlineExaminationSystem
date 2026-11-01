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

@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("studentId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        ArrayList<HashMap<String, Object>> questions =
                new ArrayList<>();

        String sql = "SELECT question_id, question_text, " +
                "option_a, option_b, option_c, option_d " +
                "FROM questions ORDER BY question_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                HashMap<String, Object> q = new HashMap<>();

                q.put("id", rs.getInt("question_id"));
                q.put("text", rs.getString("question_text"));
                q.put("a", rs.getString("option_a"));
                q.put("b", rs.getString("option_b"));
                q.put("c", rs.getString("option_c"));
                q.put("d", rs.getString("option_d"));

                questions.add(q);
            }

            request.setAttribute("questions", questions);

            request.getRequestDispatcher("/exam.jsp")
                   .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.sendError(500, "Unable to retrieve questions");
        }
    }
}