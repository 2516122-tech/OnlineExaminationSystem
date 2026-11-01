package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ResultServlet")
public class ResultServlet extends HttpServlet {

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

    String id = request.getParameter("id");

    if (id == null || !id.matches("\\d+")) {
        response.sendError(400, "Invalid result ID");
        return;
    }

    int resultId = Integer.parseInt(id);
    int studentId = (Integer) session.getAttribute("studentId");

    String sql = "SELECT r.score, r.total_questions, " +
            "r.submitted_at, s.student_name " +
            "FROM exam_results r " +
            "JOIN exam_students s " +
            "ON r.student_id = s.student_id " +
            "WHERE r.result_id = ? AND r.student_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, resultId);
        ps.setInt(2, studentId);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                request.setAttribute("name",
                        rs.getString("student_name"));

                request.setAttribute("score",
                        rs.getInt("score"));

                request.setAttribute("total",
                        rs.getInt("total_questions"));

                request.setAttribute("date",
                        rs.getTimestamp("submitted_at"));

                request.getRequestDispatcher("/result.jsp")
                        .forward(request, response);

            } else {
                response.sendError(404, "Result not found");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
        response.sendError(500, "Unable to retrieve result");
    }
}

}
