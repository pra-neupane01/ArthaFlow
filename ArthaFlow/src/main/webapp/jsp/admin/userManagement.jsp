<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Account" %>
<%@ page import="com.arthaflow.model.KycDetails" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private String h(Object value) {
        if (value == null) return "";
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
<%
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"ADMIN".equals(admin.getRole())) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<User> users    = (List<User>) request.getAttribute("users");
    List<Account> accs  = (List<Account>) request.getAttribute("accounts");
    Map<Integer, Account> accMap = new HashMap<>();
    if (accs != null) for (Account a : accs) accMap.putIfAbsent(a.getUserId(), a);
    @SuppressWarnings("unchecked")
    Map<Integer, KycDetails> accountKycByAccountId = (Map<Integer, KycDetails>) request.getAttribute("accountKycByAccountId");
    if (accountKycByAccountId == null) accountKycByAccountId = new HashMap<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User & KYC Management | ArthaFlow Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="adminSidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">User & KYC Management</h2><p style="font-size:0.78rem;color:var(--text-muted);">Review, verify, and manage all users</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar" style="background:var(--danger);">A</div>
                <div><div class="navbar-name"><%= admin.getFullName() %></div><div class="navbar-role">Administrator</div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>User & KYC Management</h1><p>Verify identity documents and issue account numbers.</p></div>

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
                            <tr><th>User</th><th>Contact</th><th>KYC Documents</th><th>Account Info</th><th>Actions</th></tr>
                        </thead>
                        <tbody>
                        <%
                            if (users != null && !users.isEmpty()) {
                                for (User u : users) {
                                    if ("ADMIN".equals(u.getRole())) continue;
                                    Account acc = accMap.get(u.getUserId());
                                    KycDetails ak = (acc != null) ? accountKycByAccountId.get(acc.getAccountId()) : null;
                        %>
                        <tr>
                            <td>
                                <div class="fw-bold"><%= u.getFullName() %></div>
                                <div class="text-muted" style="font-size:0.8rem;"><%= u.getEmail() %></div>
                                <div class="text-muted" style="font-size:0.78rem;">ID #<%= u.getUserId() %></div>
                            </td>
                            <td>
                                <div style="font-size:0.85rem;"><%= u.getPhoneNumber() %></div>
                                <div class="text-muted" style="font-size:0.78rem;margin-top:2px;"><%= u.getAddress() %></div>
                            </td>
                            <td>
                                <% if (ak != null && ak.getIdDocumentPath() != null && !ak.getIdDocumentPath().isEmpty()) { %>
                                    <a href="<%= request.getContextPath() %>/<%= ak.getIdDocumentPath() %>" target="_blank" class="btn btn-outline btn-sm" style="margin-bottom:4px;display:block;max-width:140px;">📄 View ID</a>
                                    <% if (ak.getAddressProofPath() != null && !ak.getAddressProofPath().isEmpty()) { %>
                                    <a href="<%= request.getContextPath() %>/<%= ak.getAddressProofPath() %>" target="_blank" class="btn btn-ghost btn-sm" style="display:block;max-width:140px;">📍 View Address</a>
                                    <% } %>
                                <% } else { %>
                                    <span class="text-muted" style="font-size:0.82rem;">No docs uploaded</span>
                                <% } %>
                            </td>
                            <td>
                                <% if (acc != null) { %>
                                    <span class="badge <%= "ACTIVE".equals(acc.getStatus()) ? "badge-success" : "PENDING".equals(acc.getStatus()) ? "badge-pending" : "badge-danger" %>">
                                        <%= acc.getStatus() %>
                                    </span>
                                    <div style="font-size:0.82rem;margin-top:0.4rem;"><span class="text-muted">Type:</span> <%= acc.getAccountType() %></div>
                                    <div style="font-size:0.82rem;"><span class="text-muted">Num:</span> <%= acc.getAccount_number() != null ? acc.getAccount_number() : "Not Issued" %></div>
                                    <% if (ak != null && ak.getCitizenshipNumber() != null && !ak.getCitizenshipNumber().isEmpty()) { %>
                                    <div style="font-size:0.78rem;margin-top:0.35rem;color:var(--text-muted);">Citizenship: <span class="fw-bold" style="color:var(--text);"><%= ak.getCitizenshipNumber() %></span></div>
                                    <% } %>
                                    <% if (ak != null && ak.getDateOfBirth() != null) { %>
                                    <div style="font-size:0.78rem;color:var(--text-muted);">DOB: <span class="fw-bold" style="color:var(--text);"><%= ak.getDateOfBirth() %></span>
                                    <% if (ak.getGender() != null && !ak.getGender().isEmpty()) { %> · <%= ak.getGender() %><% } %></div>
                                    <% } %>
                                    <% if (ak != null && ak.getRejectionRemarks() != null && !ak.getRejectionRemarks().isBlank()) { %>
                                    <div style="font-size:0.78rem;margin-top:0.45rem;color:var(--danger);">
                                        <span class="fw-bold">Remark:</span> <%= h(ak.getRejectionRemarks()) %>
                                    </div>
                                    <% } %>
                                <% } else { %>
                                    <span class="text-muted" style="font-size:0.82rem;">No application</span>
                                <% } %>
                            </td>
                            <td style="min-width:220px;">
                                <% if (acc != null && "PENDING".equals(acc.getStatus())) { %>
                                <!-- ISSUE FORM -->
                                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="margin-bottom:6px;">
                                    <input type="hidden" name="action" value="issueAccountNumber">
                                    <input type="hidden" name="accountId" value="<%= acc.getAccountId() %>">
                                    <button type="submit" class="btn btn-primary btn-sm">✓ Issue account number</button>
                                </form>
                                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="margin-top:6px;">
                                    <input type="hidden" name="action" value="rejectAccount">
                                    <input type="hidden" name="accountId" value="<%= acc.getAccountId() %>">
                                    <textarea name="rejectionRemarks" class="form-control" rows="2" required maxlength="1000"
                                              placeholder="Reason this account is not eligible"
                                              style="font-size:0.78rem;margin-bottom:6px;min-width:200px;"></textarea>
                                    <button type="submit" class="btn btn-danger btn-sm">✗ Reject</button>
                                </form>
                                <% } %>
                                <!-- DELETE USER -->
                                <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST"
                                      onsubmit="return confirm('Delete user <%= u.getFullName() %>? This is permanent.');"
                                      style="display:inline;margin-left:4px;">
                                    <input type="hidden" name="action" value="deleteUser">
                                    <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                                    <button type="submit" class="btn btn-ghost btn-sm" style="color:var(--danger);border-color:var(--danger);">🗑 Delete</button>
                                </form>
                            </td>
                        </tr>
                        <%  } } else { %>
                        <tr><td colspan="5" style="text-align:center;padding:3rem;color:var(--text-muted);">No users found in the system.</td></tr>
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
