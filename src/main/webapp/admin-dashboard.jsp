<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect("admin-login.html");
        return;
    }

    String adminName =
        (String) session.getAttribute("adminName");
%>

<!DOCTYPE html>
<html>

<head>

    <title>Admin Dashboard</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            background: #f3efff;
            text-align: center;
            padding-top: 80px;
        }

        .container {
            background: white;
            width: 400px;
            margin: auto;
            padding: 35px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }

        h1 {
            color: #663399;
        }

        h3 {
            color: #444;
        }

        p {
            color: #555;
        }

        .success {
            color: green;
            font-weight: bold;
        }

        button {
            background: #663399;
            color: white;
            padding: 14px 25px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 16px;
            width: 220px;
        }

        button:hover {
            background: #512080;
        }

    </style>

</head>

<body>

    <div class="container">

        <h1>Admin Dashboard</h1>

        <h3>Welcome, <%= adminName %>!</h3>

        <p>Manage your examination questions here.</p>

        <%
            if ("1".equals(request.getParameter("added"))) {
        %>

            <p class="success">
                Question Added Successfully!
            </p>

        <%
            }
        %>

        <br>

        <button onclick="location.href='add-question.html'">
            Add Questions
        </button>

        <br><br>

        <button onclick="location.href='QuestionManagementServlet'">
            Manage Questions
        </button>
        <br><br>

<button onclick="location.href='AdminLogoutServlet'">
    Logout
</button>

    </div>

</body>

</html>