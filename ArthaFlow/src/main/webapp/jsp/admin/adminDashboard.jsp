<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Admin Dashboard - ArthaFlow</title>
</head>
<body>
<div style="padding: 20px; font-family: Arial;">
    <h1>Admin Control Panel</h1>
    <hr>

    <div style="display: flex; gap: 20px; margin-top: 20px;">
        <div style="border: 2px solid #333; padding: 20px; border-radius: 10px; width: 200px;">
            <h3>Total Users</h3>
            <p style="font-size: 24px; font-weight: bold;"><%= request.getAttribute("totalUsers") %></p>
        </div>

        <div style="border: 2px solid green; padding: 20px; border-radius: 10px; width: 200px;">
            <h3>Total Deposits</h3>
            <p style="font-size: 24px; color: green;">Rs. <%= request.getAttribute("totalDeposits") %></p>
        </div>

        <div style="border: 2px solid red; padding: 20px; border-radius: 10px; width: 200px;">
            <h3>Total Withdrawals</h3>
            <p style="font-size: 24px; color: red;">Rs. <%= request.getAttribute("totalWithdrawals") %></p>
        </div>
    </div>

    <br><br>
    <nav>
        <a href="<%= request.getContextPath() %>/admin/dashboard?action=users">Manage Users</a> |
        <a href="<%= request.getContextPath() %>/admin/dashboard?action=transactions">All Transactions</a> |
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
    </nav>
</div>
</body>
</html>

