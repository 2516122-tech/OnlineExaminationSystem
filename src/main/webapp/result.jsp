<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<%
if (session.getAttribute("studentId") == null) {
response.sendRedirect("login.html");
return;
}

String name = (String) request.getAttribute("name");
Integer score = (Integer) request.getAttribute("score");
Integer total = (Integer) request.getAttribute("total");
java.sql.Timestamp date =
    (java.sql.Timestamp) request.getAttribute("date");

if (name == null || score == null || total == null) {
    response.sendError(400, "Result information unavailable");
    return;
}

double percentage = total > 0
    ? (score * 100.0 / total) : 0;

%>

<!DOCTYPE html>

<html>

<head>

<title>Examination Result</title>

<style>

body {
    font-family: Arial;
    background: #f0edff;
    text-align: center;
    padding: 60px;
}

.container {
    background: white;
    max-width: 450px;
    margin: auto;
    padding: 35px;
    border-radius: 15px;
    box-shadow: 0 0 15px #ccc;
}

h1 {
    color: #6336c5;
}

.score {
    font-size: 45px;
    color: #6336c5;
    font-weight: bold;
}

.info {
    font-size: 18px;
    margin: 15px;
}

button {
    background: #6336c5;
    color: white;
    padding: 12px 25px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}

</style>

</head>

<body>

<div class="container">


<h1>🎉 Examination Result</h1>

<h3>Congratulations, <%= name %>!</h3>

<p>Your examination has been submitted successfully.</p>

<div class="score">
    <%= score %> / <%= total %>
</div>

<div class="info">
    <b>Total Questions:</b> <%= total %>
</div>

<div class="info">
    <b>Marks Obtained:</b> <%= score %>
</div>

<div class="info">
    <b>Percentage:</b>
    <%= String.format("%.2f", percentage) %>%
</div>

<div class="info">
    <b>Submitted At:</b> <%= date %>
</div>

<br>

<button onclick="location.href='dashboard.jsp'">
    Back to Dashboard
</button>

</div>

</body>
</html>
