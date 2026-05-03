
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.arthaflow.model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Management - ArthaFlow</title>
</head>
<body>
<div style="padding: 20px; font-family: Arial;">
    <h2>User Management</h2>
    <p>List of all registered customers:</p>

    <table border="1" width="100%" style="border-collapse: collapse; text-align: left;">
        <thead style="background-color: #f2f2f2;">
        <tr>
            <th style="padding: 10px;">ID</th>
            <th style="padding: 10px;">Full Name</th>
            <th style="padding: 10px;">Email</th>
            <th style="padding: 10px;">Phone</th>
            <th style="padding: 10px;">Role</th>
            <th style="padding: 10px;">Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<User> userList = (List<User>) request.getAttribute("users");
            for (User u : userList) {
        %>
        <tr>
            <td style="padding: 10px;"><%= u.getUserId() %></td>
            <td style="padding: 10px;"><%= u.getFullName() %></td>
            <td style="padding: 10px;"><%= u.getEmail() %></td>
            <td style="padding: 10px;"><%= u.getPhoneNumber() %></td>
            <td style="padding: 10px;"><%= u.getRole() %></td>
            <td style="padding: 10px;">
                <button style="color: blue;">Edit</button> |
                <button style="color: red;" onclick="return confirm('Delete this user?')">Delete</button>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>

    <br>
    <a href="<%= request.getContextPath() %>/admin/dashboard">Back to Admin Panel</a>
</div>
</body>
</html>
