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

@WebServlet("/HistoryServlet")
public class HistoryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("studentId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int studentId =
            (Integer) session.getAttribute("studentId");

        ArrayList<HashMap<String, Object>> history =
            new ArrayList<>();

        String sql = "SELECT result_id, score, total_questions, submitted_at "
                   + "FROM exam_results "
                   + "WHERE student_id = ? "
                   + "ORDER BY submitted_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HashMap<String, Object> result = new HashMap<>();

                result.put("id", rs.getInt("result_id"));
                result.put("score", rs.getInt("score"));
                result.put("total", rs.getInt("total_questions"));
                result.put("date", rs.getTimestamp("submitted_at"));

                history.add(result);
            }

            request.setAttribute("history", history);

            request.getRequestDispatcher("history.jsp")
                   .forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().println(
                "Unable to load examination history."
            );
        }
    }
}