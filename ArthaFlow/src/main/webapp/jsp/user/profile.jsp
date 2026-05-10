<%@ page import="com.arthaflow.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">My Profile</h2><p style="font-size:0.78rem;color:var(--text-muted);">Manage your personal information</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar"><% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %></div>
                <div><div class="navbar-name"><%= user.getFullName() %></div><div class="navbar-role"><%= user.getRole() %></div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>My Profile</h1><p>Update your personal details and profile picture.</p></div>

            <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>
            <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger">⚠ <%= request.getParameter("error") %></div>
            <% } %>

            <div style="display:grid;grid-template-columns:280px 1fr;gap:1.75rem;align-items:start;">

                <!-- PROFILE PICTURE CARD -->
                <div class="card" style="text-align:center;">
                    <div style="display:flex;justify-content:center;margin-bottom:1.25rem;">
                        <div style="position:relative;display:inline-block;">
                            <div class="avatar avatar-xl">
                                <% if (picUrl != null) { %><img src="<%= picUrl %>" alt="Profile"><% } else { %><%= user.getInitials() %><% } %>
                            </div>
                        </div>
                    </div>
                    <div class="fw-bold" style="font-size:1.05rem;"><%= user.getFullName() %></div>
                    <div class="text-muted" style="font-size:0.85rem;margin-bottom:1.5rem;"><%= user.getRole() %> • Member since <%= user.getCreatedDate() != null ? user.getCreatedDate().toString().substring(0,10) : "—" %></div>

                    <!-- UPLOAD FORM -->
                    <form action="<%= request.getContextPath() %>/user/profile" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="uploadPicture">
                        <div class="form-group">
                            <label class="form-label">Change Profile Photo</label>
                            <input type="file" name="profilePicture" accept="image/*" class="form-control" id="picInput">
                        </div>
                        <button type="submit" class="btn btn-outline btn-full btn-sm">Upload Photo</button>
                    </form>
                </div>

                <!-- PERSONAL INFO CARD -->
                <div class="card">
                    <div class="card-header"><h3>Personal Information</h3></div>
                    <form action="<%= request.getContextPath() %>/user/profile" method="POST">
                        <input type="hidden" name="action" value="updateInfo">
                        <div class="grid-2">
                            <div class="form-group">
                                <label class="form-label">Full Name</label>
                                <input type="text" name="fullName" class="form-control" value="<%= user.getFullName() %>" required>
                            </div>
                            <div class="form-group">
                                <label class="form-label">Phone Number</label>
                                <input type="text" name="phoneNumber" class="form-control" value="<%= user.getPhoneNumber() %>">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Email Address</label>
                            <input type="email" name="email" class="form-control" value="<%= user.getEmail() %>" disabled style="opacity:0.6;">
                            <span class="text-muted" style="font-size:0.78rem;">Email cannot be changed.</span>
                        </div>
                        <div class="form-group">
                            <label class="form-label">Current Address</label>
                            <input type="text" name="address" class="form-control" value="<%= user.getAddress() %>">
                        </div>
                        <button type="submit" class="btn btn-primary">Save Changes →</button>
                    </form>

                    <hr class="divider">
                    <div>
                        <span class="stat-label">Account Security</span>
                        <p class="text-muted" style="font-size:0.85rem;margin-top:0.4rem;">To change your password, please contact the ArthaFlow admin at <strong>Sundar Haraicha 04, Dulari, Itahari</strong>.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
