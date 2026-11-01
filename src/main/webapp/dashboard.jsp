<%
    if (session.getAttribute("studentId") == null) {
        response.sendRedirect("login.html");
        return;
    }

    String studentName =
        (String) session.getAttribute("studentName");
%>

<!DOCTYPE html>
<html>
<head>

    <title>Student Dashboard</title>

    <style>

        body {
            font-family: Arial, sans-serif;
            background: #f3efff;
            text-align: center;
            margin: 0;
            padding: 50px;
        }

        .container {
            background: white;
            width: 450px;
            margin: 50px auto;
            padding: 35px;
            border-radius: 15px;
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
            font-size: 16px;
        }

        button {
            background: #663399;
            color: white;
            padding: 13px 25px;
            border: none;
            border-radius: 7px;
            cursor: pointer;
            font-size: 15px;
            width: 220px;
        }

        button:hover {
            background: #4b2475;
        }

    </style>

</head>

<body>

    <div class="container">

        <h1>Student Dashboard</h1>

        <h3>
            Welcome, <%= studentName %>!
        </h3>

        <p>Welcome to the Online Examination System.</p>

        <br>

        <button onclick="location.href='QuestionServlet'">
            Start Examination
        </button>

        <br><br>

        <button onclick="location.href='HistoryServlet'">
            View Examination History
        </button>

        <br><br>

<button onclick="location.href='StudentLogoutServlet'">
    Logout
</button>

    </div>

</body>
</html>