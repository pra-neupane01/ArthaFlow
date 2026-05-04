<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 5/3/2026
  Time: 1:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Withdraw Funds - ArthaFlow</title>
</head>
<body>
<div class="container">
    <h2>Withdraw Money</h2>

    <%
        String error = (String) request.getAttribute("error");
        String success = (String) request.getAttribute("success");
        if (error != null) {
    %>
    <div style="color: red; border: 1px solid red; padding: 10px; margin-bottom: 10px;"><%= error %></div>
    <% } %>

    <% if (success != null) { %>
    <div style="color: green; border: 1px solid green; padding: 10px; margin-bottom: 10px;"><%= success %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/user/transaction" method="POST" onsubmit="return validateTransactionForm(this)">
        <input type="hidden" name="action" value="withdraw">

        <div class="form-group">
            <label>Amount (Rs.):</label>
            <input type="number" name="amount" step="0.01" required min="1">
        </div>

        <div class="form-group">
            <label>Description/Remarks:</label>
            <input type="text" name="description" placeholder="e.g. ATM Withdrawal">
        </div>

        <button type="submit" style="background-color: #d9534f; color: white;">Withdraw Now</button>
        <a href="<%= request.getContextPath() %>/user/dashboard">Back to Dashboard</a>
    </form>
</div>
<script src="<%= request.getContextPath() %>/js/dashboard.js"></script>
</body>
</html>
