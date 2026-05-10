<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.CreditCard" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"ADMIN".equals(admin.getRole())) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<CreditCard> requests = (List<CreditCard>) request.getAttribute("cardRequests");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Credit Card Applications | ArthaFlow Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="adminSidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">Credit Card Applications</h2><p style="font-size:0.78rem;color:var(--text-muted);">Review and approve credit card requests</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar" style="background:var(--danger);">A</div>
                <div><div class="navbar-name"><%= admin.getFullName() %></div><div class="navbar-role">Administrator</div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>Credit Card Applications</h1><p>Manage all ArthaFlow credit card requests.</p></div>

            <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>
            <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger">⚠ <%= request.getParameter("error") %></div>
            <% } %>

            <div class="card">
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr><th>Card ID</th><th>User ID</th><th>Card Type</th><th>Applied On</th><th>Status</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                        <% if (requests != null && !requests.isEmpty()) {
                               for (CreditCard card : requests) { %>
                        <tr>
                            <td class="text-muted">#<%= card.getCardId() %></td>
                            <td>User #<%= card.getUserId() %></td>
                            <td>
                                <span style="font-weight:600;">
                                    <%= "PLATINUM".equals(card.getCardType()) ? "💎" : "🥇" %>
                                    <%= card.getCardType() %>
                                </span>
                                <div class="text-muted" style="font-size:0.78rem;">
                                    Limit: Rs. <%= "PLATINUM".equals(card.getCardType()) ? "1,00,000" : "50,000" %>
                                </div>
                            </td>
                            <td class="text-muted" style="font-size:0.82rem;"><%= card.getCreatedDate() %></td>
                            <td>
                                <span class="badge <%= "ACTIVE".equals(card.getStatus()) ? "badge-success" : "PENDING".equals(card.getStatus()) ? "badge-pending" : "badge-danger" %>">
                                    <%= card.getStatus() %>
                                </span>
                            </td>
                            <td>
                                <% if ("PENDING".equals(card.getStatus())) { %>
                                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="approveCard">
                                    <input type="hidden" name="cardId"  value="<%= card.getCardId() %>">
                                    <button type="submit" class="btn btn-primary btn-sm">✓ Approve</button>
                                </form>
                                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="display:inline;margin-left:4px;">
                                    <input type="hidden" name="action" value="rejectCard">
                                    <input type="hidden" name="cardId"  value="<%= card.getCardId() %>">
                                    <button type="submit" class="btn btn-danger btn-sm">✗ Reject</button>
                                </form>
                                <% } else { %>
                                <span class="text-muted" style="font-size:0.82rem;">—</span>
                                <% } %>
                            </td>
                        </tr>
                        <% } } else { %>
                        <tr><td colspan="6" style="text-align:center;padding:3rem;color:var(--text-muted);">No credit card applications found.</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
