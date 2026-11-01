<%@ page import="java.util.*" %>

<%
    if (session.getAttribute("studentId") == null) {
        response.sendRedirect("login.html");
        return;
    }

    ArrayList<HashMap<String, Object>> questions =
        (ArrayList<HashMap<String, Object>>)
        request.getAttribute("questions");

    if (questions == null || questions.isEmpty()) {
        out.println("<h2>No questions available!</h2>");
        return;
    }
%>

<!DOCTYPE html>
<html>

<head>

<title>Online Examination</title>

<style>

body {
    font-family: Arial;
    background: #f0edff;
    padding: 30px;
}

.container {
    max-width: 750px;
    margin: auto;
}

.question {
    background: white;
    padding: 20px;
    margin: 20px 0;
    border-radius: 12px;
    box-shadow: 0 0 8px #ccc;
}

h1 {
    text-align: center;
    color: #6336c5;
}

label {
    display: block;
    padding: 8px;
}

button {
    background: #6336c5;
    color: white;
    border: none;
    padding: 13px 30px;
    border-radius: 6px;
    cursor: pointer;
}

</style>

</head>

<body>

<div class="container">

<h1>Online Examination</h1>

<h3>Answer the following questions</h3>

<form action="SubmitExamServlet" method="post">

<%
    int number = 1;

    for (HashMap<String, Object> q : questions) {

        int id = (Integer) q.get("id");
%>

<div class="question">

    <h3>
        Question <%= number++ %>:
        <%= q.get("text") %>
    </h3>

    <label>
        <input type="radio"
               name="answer_<%= id %>"
               value="A">
        A. <%= q.get("a") %>
    </label>

    <label>
        <input type="radio"
               name="answer_<%= id %>"
               value="B">
        B. <%= q.get("b") %>
    </label>

    <label>
        <input type="radio"
               name="answer_<%= id %>"
               value="C">
        C. <%= q.get("c") %>
    </label>

    <label>
        <input type="radio"
               name="answer_<%= id %>"
               value="D">
        D. <%= q.get("d") %>
    </label>

</div>

<%
    }
%>

<button type="submit">Submit Examination</button>

</form>

</div>

</body>
</html>