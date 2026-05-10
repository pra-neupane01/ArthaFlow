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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account & KYC | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div>
                <h2 style="font-size:1rem;font-weight:700;">Account & KYC</h2>
                <p style="font-size:0.78rem;color:var(--text-muted);">Identity verification and account management</p>
            </div>
            <div class="navbar-user">
                <div class="navbar-avatar">
                    <% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %>
                </div>
                <div><div class="navbar-name"><%= user.getFullName() %></div><div class="navbar-role"><%= user.getRole() %></div></div>
            </div>
        </div>

        <div class="page-content">
            <div class="page-header">
                <h1>Account & KYC Verification</h1>
                <p>Complete your identity verification to activate full banking features.</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger">⚠ <%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getParameter("success") != null) { %>
                <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>

            <% if (account == null) { %>
            <!-- NO ACCOUNT — KYC FORM -->
            <div class="card" style="max-width: 620px;">
                <div class="card-header"><h3>📝 Submit KYC Application</h3></div>
                <p class="text-muted mb-3">Upload your identity documents to apply for a bank account. Our admin team will verify and issue your account number within 1-2 business days.</p>
                <form action="<%= request.getContextPath() %>/user/account" method="POST" enctype="multipart/form-data">
                    <div class="form-group">
                        <label class="form-label">Account Type</label>
                        <select name="accountType" class="form-control">
                            <option value="SAVINGS">Savings Account</option>
                            <option value="CURRENT">Current Account</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Identity Document (Citizenship / Passport)</label>
                        <input type="file" name="idDocument" accept="image/*,.pdf" required class="form-control">
                        <span class="text-muted" style="font-size:0.8rem;">Accepted: JPG, PNG, PDF (Max 10MB)</span>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Proof of Address (Utility Bill / Letter)</label>
                        <input type="file" name="addressProof" accept="image/*,.pdf" required class="form-control">
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="margin-top:0.5rem;">Submit KYC Application →</button>
                </form>
            </div>

            <% } else if ("PENDING".equals(account.getStatus())) { %>
            <!-- PENDING STATE -->
            <div class="card" style="max-width:620px;text-align:center;">
                <div style="font-size:4rem;margin-bottom:1rem;">⏳</div>
                <h2 style="color:var(--warning);">Application Under Review</h2>
                <p class="text-muted" style="margin:1rem 0 2rem;">Our administrators are verifying your KYC documents. You'll receive your account number once approved.</p>
                <div style="background:var(--bg);border-radius:var(--radius-sm);padding:1.25rem;text-align:left;display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
                    <div><span class="stat-label">Application Status</span><div class="fw-bold" style="color:var(--warning);">KYC Pending</div></div>
                    <div><span class="stat-label">Account Type</span><div class="fw-bold"><%= account.getAccountType() %></div></div>
                </div>
            </div>

            <% } else if ("ACTIVE".equals(account.getStatus())) { %>
            <!-- ACTIVE STATE -->
            <div class="stat-grid" style="max-width:700px;">
                <div class="stat-card">
                    <span class="stat-label">Account Status</span>
                    <div style="display:flex;align-items:center;gap:0.5rem;margin-top:0.4rem;">
                        <span class="badge badge-success">✓ ACTIVE</span>
                    </div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Account Number</span>
                    <div class="stat-value" style="font-size:1.25rem;">#<%= account.getAccount_number() %></div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Current Balance</span>
                    <div class="stat-value green">Rs. <%= String.format("%,.2f", account.getBalance()) %></div>
                </div>
            </div>
            <div class="card" style="max-width:700px;margin-top:1.5rem;">
                <div class="card-header"><h3>Account Details</h3></div>
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.25rem;">
                    <div><span class="stat-label">Account Type</span><div class="fw-bold mt-1"><%= account.getAccountType() %></div></div>
                    <div><span class="stat-label">KYC Status</span><div class="mt-1"><span class="badge badge-success">APPROVED</span></div></div>
                    <div><span class="stat-label">Member Since</span><div class="fw-bold mt-1"><%= account.getCreatedDate() %></div></div>
                    <div><span class="stat-label">Account ID</span><div class="fw-bold mt-1">#<%= account.getAccountId() %></div></div>
                </div>
            </div>

            <% } else { %>
            <!-- REJECTED -->
            <div class="card" style="max-width:620px;text-align:center;">
                <div style="font-size:4rem;margin-bottom:1rem;">❌</div>
                <h2 style="color:var(--danger);">Application <%= account.getStatus() %></h2>
                <p class="text-muted" style="margin-bottom:1.5rem;">Your KYC application was not approved. Please contact our branch for assistance.</p>
                <p style="font-weight:600;">📍 Sundar Haraicha 04, Dulari, Itahari, Nepal</p>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
