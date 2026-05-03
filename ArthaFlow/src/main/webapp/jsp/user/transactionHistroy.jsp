<%--
  Created by IntelliJ IDEA.
  User: Acer
  Date: 5/3/2026
  Time: 1:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.arthaflow.model.Transaction" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Transaction History - ArthaFlow</title>
</head>
<body>
<div class="container">
    <h2>Your Transactions</h2>

    <form action="<%= request.getContextPath() %>/user/search" method="POST" style="margin-bottom: 20px;">
        <select name="searchType" id="searchType" onchange="toggleSearchInputs()">
            <option value="all">View All</option>
            <option value="type">Filter by Type</option>
            <option value="date">Filter by Date</option>
        </select>

        <span id="typeInput" style="display:none;">
                <select name="transactionType">
                    <option value="DEPOSIT">DEPOSIT</option>
                    <option value="WITHDRAWAL">WITHDRAWAL</option>
                </select>
            </span>

        <span id="dateInputs" style="display:none;">
                From: <input type="date" name="fromDate">
                To: <input type="date" name="toDate">
            </span>

        <button type="submit">Filter</button>
    </form>

    <table border="1" width="100%" id="txnTable" style="border-collapse: collapse;">
        <thead>
        <tr style="background-color: #f2f2f2;">
            <th>Date</th>
            <th>Type</th>
            <th>Amount</th>
            <th>Balance After</th>
            <th>Remarks</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
            if (transactions != null && !transactions.isEmpty()) {
                for (Transaction txn : transactions) {
        %>
        <tr>
            <td><%= txn.getTransactionDate() %></td>
            <td style="color: <%= txn.getType().equals("DEPOSIT") ? "green" : "red" %>"><%= txn.getType() %></td>
            <td>Rs. <%= txn.getAmount() %></td>
            <td>Rs. <%= txn.getBalanceAfter() %></td>
            <td><%= txn.getRemarks() %></td>
        </tr>
        <%
            }
        } else {
        %>
        <tr><td colspan="5" align="center">No transactions found.</td></tr>
        <% } %>
        </tbody>
    </table>
    <br>
    <a href="<%= request.getContextPath() %>/user/dashboard">Back to Dashboard</a>
</div>
<script src="<%= request.getContextPath() %>/js/search.js"></script>
</body>
</html>
