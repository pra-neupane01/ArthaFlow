
<%@ page import="com.arthaflow.model.User" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Management - ArthaFlow</title>
</head>
<body>
<div  class= " container" style="padding: 20px; font-family: Arial;">
    <h2>User Management</h2>

    <table border="1" width="100%" style="border-collapse: collapse;">
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
            if(users!= null && !users.isEmpty()){
                for (User u : users){
            }
        %>
        <tr>
            <td style="padding: 10px;"><%= u.getUserId() %></td>
            <td style="padding: 10px;"><%= u.getFullName() %></td>
            <td style="padding: 10px;"><%= u.getEmail() %></td>
            <td style="padding: 10px;"><%= u.getPhoneNumber() %></td>
            <td style="padding: 10px;"><%= u.getRole() %></td>
            <td>
                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="display:inline;">
                    <input type="hidden" name="action" value="deleteUser">
                    <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                    <button type="submit" onclick="return confirm('Are you sure?')">Delete</button>
                </form>
            </td>
        </tr>
        <%
            }
            } else {
        %>
        <tr><td colspan="6" align="center">No users found in database.</td></tr>
        <% } %>
        </tbody>
    </table>
    <br>
    <a href="<%= request.getContextPath() %>/admin/dashboard">Back to Dashboard</a>
</div>
</body>
</html>
