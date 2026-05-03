<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 5/3/2026
  Time: 1:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Deposit Money </title>
</head>
<body>
<h2>Deposit Money </h2>

<!-- Success Message -->
<% if (request.getAttribute("success") != null) { %>
<p style="color:green;">
    <%= request.getAttribute("success") %>
</p>
<% } %>


<% if (request.getAttribute("error") != null) { %>
<p style="color:red;">
    <%= request.getAttribute("error") %>
</p>
<% } %>

<form action="<%= request.getContextPath() %>/TransactionServlet" method="post">


    <input type="hidden" name="action" value="deposit"/>

    <label>Amount:</label><br>
    <input type="number" name="amount" required/><br><br>

    <label>Description:</label><br>
    <input type="text" name="description"/><br><br>

    <button type="submit">Deposit</button>

</form>

</body>
</html>


