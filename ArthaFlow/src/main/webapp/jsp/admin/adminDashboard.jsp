<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Transaction" %>
<%@ page import="java.util.List" %>
<%
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"ADMIN".equals(admin.getRole())) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<Transaction> allTx = (List<Transaction>) request.getAttribute("transactions");
    Object totalDep = request.getAttribute("totalDeposits");
    Object totalWd  = request.getAttribute("totalWithdrawals");
    List<User> allUsers = (List<User>) request.getAttribute("users");
    int userCount = allUsers != null ? (int) allUsers.stream().filter(u -> !"ADMIN".equals(u.getRole())).count() : 0;
    int txCount   = allTx != null ? allTx.size() : 0;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="adminSidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">Admin Dashboard</h2><p style="font-size:0.78rem;color:var(--text-muted);">ArthaFlow Network Overview</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar" style="background:var(--danger);">A</div>
                <div><div class="navbar-name"><%= admin.getFullName() %></div><div class="navbar-role">Administrator</div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>System Overview</h1><p>Real-time monitoring of the ArthaFlow banking network.</p></div>

            <!-- KPI CARDS -->
            <div class="stat-grid">
                <div class="stat-card">
                    <span class="stat-label">Total Users</span>
                    <div class="stat-value green"><%= userCount %></div>
                    <div class="stat-sub">Registered customers</div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Total System Deposits</span>
                    <div class="stat-value green">Rs. <%= totalDep != null ? String.format("%,.2f", ((Number)totalDep).doubleValue()) : "0.00" %></div>
                    <div class="stat-sub">All time</div>
                </div>
                <div class="stat-card danger">
                    <span class="stat-label">Total System Withdrawals</span>
                    <div class="stat-value red">Rs. <%= totalWd != null ? String.format("%,.2f", ((Number)totalWd).doubleValue()) : "0.00" %></div>
                    <div class="stat-sub">All time</div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Total Transactions</span>
                    <div class="stat-value"><%= txCount %></div>
                    <div class="stat-sub">System-wide</div>
                </div>
            </div>

            <!-- QUICK ACTIONS -->
            <div class="card" style="margin-bottom:1.75rem;">
                <div class="card-header"><h3>Quick Management</h3></div>
                <div class="grid-3">
                    <a href="<%= request.getContextPath() %>/admin/dashboard?action=users" class="btn btn-primary" style="display:flex;align-items:center;gap:0.4rem;justify-content:center;">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                        Manage Users & KYC
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/dashboard?action=creditCards" class="btn btn-outline" style="display:flex;align-items:center;gap:0.4rem;justify-content:center;">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
                        Review Card Applications
                    </a>
                    <a href="<%= request.getContextPath() %>/admin/dashboard?action=transactions" class="btn btn-ghost" style="display:flex;align-items:center;gap:0.4rem;justify-content:center;">
                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg>
                        View System Logs
                    </a>
                </div>
            </div>

            <!-- RECENT TRANSACTIONS -->
            <div class="card">
                <div class="card-header">
                    <h3>Recent System Transactions</h3>
                    <a href="<%= request.getContextPath() %>/admin/dashboard?action=transactions" class="btn btn-outline btn-sm">View All</a>
                </div>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead><tr><th>ID</th><th>Account</th><th>Type</th><th>Amount (Rs.)</th><th>Date</th><th>Status</th></tr></thead>
                        <tbody>
                        <% if (allTx != null && !allTx.isEmpty()) {
                               int c = 0; for (Transaction t : allTx) { if (c++ >= 5) break; %>
                        <tr>
                            <td class="text-muted">#<%= t.getTransactionId() %></td>
                            <td>Acc #<%= t.getAccountId() %></td>
                            <td><span class="badge <%= "DEPOSIT".equals(t.getType()) ? "badge-success" : "badge-danger" %>"><%= t.getType() %></span></td>
                            <td class="fw-bold <%= "DEPOSIT".equals(t.getType()) ? "text-green" : "text-danger" %>">
                                Rs. <%= String.format("%,.2f", t.getAmount()) %>
                            </td>
                            <td class="text-muted" style="font-size:0.82rem;"><%= t.getTransactionDate() %></td>
                            <td><span class="badge badge-success"><%= t.getStatus() %></span></td>
                        </tr>
                        <% } } else { %>
                        <tr><td colspan="6" style="text-align:center;padding:2rem;color:var(--text-muted);">No transactions recorded yet.</td></tr>
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
