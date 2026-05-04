<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 5/3/2026
  Time: 1:31 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.arthaflow.model.Account" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Account Details - ArthaFlow</title>
</head>
<body>
<div class="container">
    <h2>My Account Information</h2>

    <%
        Account account = (Account) request.getAttribute("account");
        if (account != null) {
    %>
    <div class="card" style="border: 1px solid #ccc; padding: 15px;">
        <p><strong>Account Number:</strong> <%= account.getAccount_number() %></p>
        <p><strong>Account Type:</strong> <%= account.getAccountType() %></p>
        <p><strong>Current Balance:</strong> Rs. <%= account.getBalance() %></p>
        <p><strong>Status:</strong> <%= account.getStatus() %></p>
        <p><strong>Created On:</strong> <%= account.getCreatedDate() %></p>
    </div>
    <% } else { %>
    <div class="alert" style="background: #fff3cd; padding: 15px;">
        <p>No active account found.</p>
        <form action="<%= request.getContextPath() %>/user/account" method="POST">
            <label>Select Account Type to Create:</label>
            <select name="accountType">
                <option value="SAVINGS">Savings Account</option>
                <option value="CURRENT">Current Account</option>
            </select>
            <button type="submit">Create Account</button>
        </form>
    </div>
    <% } %>
    <br>
    <a href="<%= request.getContextPath() %>/user/dashboard">Back to Dashboard</a>
</div>
</body>
</html>


