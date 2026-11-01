package com.exam;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SubmitExamServlet")
public class SubmitExamServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request,
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

        int score = 0;
        int totalQuestions = 0;

        String selectSQL =
                "SELECT question_id, correct_option FROM questions";

        String insertSQL =
                "INSERT INTO exam_results " +
                "(student_id, score, total_questions) " +
                "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                response.sendError(500,
                        "Database connection failed");
                return;
            }

            con.setAutoCommit(false);

            try {

                try (PreparedStatement ps =
                        con.prepareStatement(selectSQL);
                     ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        totalQuestions++;

                        int questionId =
                                rs.getInt("question_id");

                        String correctAnswer =
                                rs.getString("correct_option");

                        String studentAnswer =
                                request.getParameter(
                                    "answer_" + questionId);

                        if (studentAnswer != null &&
                            studentAnswer.equalsIgnoreCase(
                                correctAnswer)) {

                            score++;
                        }
                    }
                }

                if (totalQuestions == 0) {
                    con.rollback();
                    response.sendError(400,
                            "No questions available");
                    return;
                }

                try (PreparedStatement ps =
                        con.prepareStatement(
                            insertSQL,
                            Statement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, studentId);
                    ps.setInt(2, score);
                    ps.setInt(3, totalQuestions);

                    ps.executeUpdate();

                    try (ResultSet keys =
                            ps.getGeneratedKeys()) {

                        if (keys.next()) {

                            int resultId = keys.getInt(1);

                            con.commit();

                            response.sendRedirect(
                                "ResultServlet?id=" + resultId);

                        } else {

                            con.rollback();

                            response.sendError(500,
                                "Result storage failed");
                        }
                    }
                }

            } catch (Exception e) {

                con.rollback();
                throw e;
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(500,
                    "Examination submission failed");
        }
    }
}