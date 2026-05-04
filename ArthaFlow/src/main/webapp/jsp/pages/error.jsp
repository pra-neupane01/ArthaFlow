<%@ page contentType="text/html;charset=UTF-8" language="java" %>
isErrorPage="true" %>
<html>
<head>
    <title>Error - ArthaFlow</title>
</head>
<body>
<div style="text-align: center; margin-top: 50px;">
    <h1 style="color: red;">Something went wrong!</h1>
    <p>
        <%
            String error = (String) request.getAttribute("error");
            out.print(error != null ? error : "An unexpected system error occurred.");
        %>
    </p>
    <a href="<%= request.getContextPath() %>/">Return to Home</a>
</div>
</body>
</html>

