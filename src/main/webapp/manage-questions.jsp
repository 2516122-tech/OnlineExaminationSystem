<%@ page import="java.util.*" %>

<%!
    public String escapeHTML(Object value) {

        if (value == null) return "";

        String s = value.toString();

        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>

<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("admin-login.html");
        return;
    }

    ArrayList<HashMap<String, Object>> questions =
        (ArrayList<HashMap<String, Object>>)
        request.getAttribute("questions");
%>

<!DOCTYPE html>
<html>
<head>

    <title>Manage Questions</title>

    <style>

        body {
            font-family: Arial;
            background: #f3efff;
            text-align: center;
            padding: 25px;
        }

        h1 {
            color: #663399;
        }

        .card {
            background: white;
            width: 80%;
            margin: 20px auto;
            padding: 20px;
            border-radius: 10px;
            text-align: left;
        }

        button {
            background: #663399;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
        }

        .delete {
            background: #dc3545;
        }

    </style>

</head>

<body>

    <h1>Manage Questions</h1>

    <%
        String deleted = request.getParameter("deleted");

        if ("1".equals(deleted)) {
    %>

        <p style="color:green;">
            Question deleted successfully!
        </p>

    <%
        } else if ("0".equals(deleted)) {
    %>

        <p style="color:red;">
            Question not found.
        </p>

    <%
        }

        if (questions != null && !questions.isEmpty()) {

            for (HashMap<String, Object> q : questions) {
    %>

    <div class="card">

        <h3>
            Question ID: <%= q.get("id") %>
        </h3>

        <p>
            <b>Question:</b>
            <%= escapeHTML(q.get("question")) %>
        </p>

        <p>A. <%= escapeHTML(q.get("a")) %></p>
        <p>B. <%= escapeHTML(q.get("b")) %></p>
        <p>C. <%= escapeHTML(q.get("c")) %></p>
        <p>D. <%= escapeHTML(q.get("d")) %></p>

        <p>
            <b>Correct Answer:</b>
            <%= escapeHTML(q.get("correct")) %>
        </p>

        <form action="QuestionManagementServlet"
              method="post"
              onsubmit="return confirm('Delete this question?');">

            <input type="hidden"
                   name="question_id"
                   value="<%= q.get("id") %>">

            <button class="delete" type="submit">
                Delete Question
            </button>

        </form>

    </div>

    <%
            }

        } else {
    %>

        <h3>No questions available.</h3>

    <% } %>

    <br>

    <button onclick="location.href='add-question.html'">
        Add New Question
    </button>

    <br><br>

    <button onclick="location.href='admin-dashboard.jsp'">
        Back to Dashboard
    </button>

</body>
</html>