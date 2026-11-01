<%@ page import="java.util.*" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    if (session.getAttribute("studentId") == null) {
        response.sendRedirect("login.html");
        return;
    }

    ArrayList<HashMap<String, Object>> history =
        (ArrayList<HashMap<String, Object>>)
        request.getAttribute("history");

    DecimalFormat df = new DecimalFormat("0.00");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Examination History</title>

    <style>
        body {
            font-family: Arial;
            background: #f3efff;
            text-align: center;
            padding: 30px;
        }

        h1 {
            color: #663399;
        }

        table {
            width: 80%;
            margin: 25px auto;
            border-collapse: collapse;
            background: white;
        }

        th, td {
            padding: 15px;
            border: 1px solid #ccc;
        }

        th {
            background: #663399;
            color: white;
        }

        tr:nth-child(even) {
            background: #eee8ff;
        }

        button {
            background: #663399;
            color: white;
            padding: 12px 25px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
    </style>
</head>

<body>

    <h1>Examination History</h1>

    <table>

        <tr>
            <th>Attempt ID</th>
            <th>Score</th>
            <th>Total Questions</th>
            <th>Percentage</th>
            <th>Submitted Date</th>
        </tr>

        <%
            if (history != null && !history.isEmpty()) {

                for (HashMap<String, Object> item : history) {

                    int score = (Integer) item.get("score");
                    int total = (Integer) item.get("total");

                    double percentage = total > 0
                        ? (score * 100.0 / total) : 0;
        %>

        <tr>
            <td><%= item.get("id") %></td>

            <td><%= score %></td>

            <td><%= total %></td>

            <td><%= df.format(percentage) %>%</td>

            <td><%= item.get("date") %></td>
        </tr>

        <%
                }

            } else {
        %>

        <tr>
            <td colspan="5">No examination attempts found.</td>
        </tr>

        <% } %>

    </table>

    <br>

    <button onclick="location.href='dashboard.jsp'">
        Back to Dashboard
    </button>

</body>
</html>