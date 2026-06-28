<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Account" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    Account account = (Account) request.getAttribute("account");
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transfer Funds | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">Transfer Funds</h2><p style="font-size:0.78rem;color:var(--text-muted);">Send money to another ArthaFlow account</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar"><% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %></div>
                <div><div class="navbar-name"><%= user.getFullName() %></div><div class="navbar-role"><%= user.getRole() %></div></div>
            </div>
        </div>
        <div class="page-content" style="max-width:640px;">
            <div class="page-header"><h1>Cross Account Transfer</h1><p>Move balance instantly from your account to another active ArthaFlow account.</p></div>

            <% if (request.getAttribute("kycWarning") != null) { %>
            <div class="alert alert-warning" style="display:flex;align-items:flex-start;gap:0.75rem;">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="flex-shrink:0;margin-top:1px;"><path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/></svg>
                <div>
                    <strong>Verified Account Required</strong><br>
                    <%= request.getAttribute("kycWarning") %>
                    <a href="<%= request.getContextPath() %>/user/account" style="color:inherit;font-weight:700;display:block;margin-top:0.5rem;">Open Account &amp; Submit KYC &rarr;</a>
                </div>
            </div>
            <% } %>
            <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">Alert: <%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">Success: <%= request.getAttribute("success") %></div>
            <% } %>

            <% if (account != null) { %>
            <div class="card" style="margin-bottom:1rem;padding:1rem 1.25rem;">
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
                    <div>
                        <span class="stat-label">Your Account Number</span>
                        <div style="font-size:1rem;font-weight:800;color:var(--text);"><%= account.getAccount_number() != null ? account.getAccount_number() : "Not issued" %></div>
                    </div>
                    <div>
                        <span class="stat-label">Available Balance</span>
                        <div style="font-size:1rem;font-weight:800;color:var(--primary);">Rs. <%= String.format("%,.2f", account.getBalance()) %></div>
                    </div>
                </div>
            </div>
            <% } %>

            <div class="card">
                <form action="<%= request.getContextPath() %>/user/transaction" method="POST">
                    <input type="hidden" name="action" value="transfer">
                    <div class="form-group">
                        <label class="form-label">Receiver Account Number</label>
                        <input type="text" name="receiverAccountNumber" class="form-control" required
                               placeholder="Ex: AF0000000001" autocomplete="off" style="text-transform:uppercase;">
                    </div>
                    <div class="form-group">
                        <label class="form-label">Amount (Rs.)</label>
                        <div class="input-group">
                            <span class="input-prefix">Rs.</span>
                            <input type="number" name="amount" step="1" min="1" class="form-control" required placeholder="0">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Remarks (Optional)</label>
                        <input type="text" name="description" class="form-control" placeholder="E.g., Rent, Family support, Bill split">
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="margin-top:0.5rem;">Confirm Transfer &rarr;</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
