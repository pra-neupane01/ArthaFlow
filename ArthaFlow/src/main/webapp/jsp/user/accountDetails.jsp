<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 5/3/2026
  Time: 1:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.arthaflow.model.Account" %>
<html>
<head>
    <title>Account Details</title>
</head>
<body>
<h2>Account Details</h2>
<%--it shows the error message--%>
<% if (request.getAttribute("error") != null) { %>
<p style="color:red;">
    <%= request.getAttribute("error") %>
</p>
<% } %>

<%
    Account account = (Account) request.getAttribute("account");
%>
</body>
</html>
