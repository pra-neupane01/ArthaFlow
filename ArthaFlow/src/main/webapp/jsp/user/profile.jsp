<%@ page import="com.arthaflow.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Profile - ArthaFlow</title>
</head>
<body>
<%
    User user = (User) session.getAttribute("user");
    if(user != null) {
%>
<div class="container">
    <h2>My Profile</h2>
    <div class="profile-card" style="border: 1px solid #ddd; padding: 20px;">
        <p><strong>Full Name:</strong> <%= user.getFullName() %></p>
        <p><strong>Email Address:</strong> <%= user.getEmail() %></p>
        <p><strong>Phone Number:</strong> <%= user.getPhoneNumber() %></p>
        <p><strong>Address:</strong> <%= user.getAddress() %></p>
        <p><strong>Role:</strong> <%= user.getRole() %></p>
        <p><strong>Joined:</strong> <%= user.getCreatedDate() %></p>
    </div>
    <br>
    <a href="<%= request.getContextPath() %>/user/dashboard">Back to Dashboard</a>
</div>
<% } %>
</body>
</html>

