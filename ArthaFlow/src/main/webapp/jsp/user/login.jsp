<%--&lt;%&ndash;--%>
<%--  Created by IntelliJ IDEA.--%>
<%--  User: neupa--%>
<%--  Date: 4/28/2026--%>
<%--  Time: 10:55 AM--%>
<%--  To change this template use File | Settings | File Templates.--%>
<%--&ndash;%&gt;--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <title>Login Page</title>--%>
<%--</head>--%>
<%--<body>--%>
<%--&lt;%&ndash;&ndash;%&gt;--%>
<%--</body>--%>
<%--</html>--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - ArthaFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

<div class="login-box">
    <h2>Login</h2>

    <form action="${pageContext.request.contextPath}/login" method="post">
        <input type="email" name="email" placeholder="Email">
        <input type="password" name="password" placeholder="Password">

        <button type="submit" class="btn">Login</button>
    </form>

    <p>Don’t have an account? <a href="register.jsp">Register</a></p>
</div>

</body>
</html>