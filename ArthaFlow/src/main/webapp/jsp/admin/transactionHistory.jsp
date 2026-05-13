<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Transaction" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"ADMIN".equals(admin.getRole())) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    String rangeAttr = request.getAttribute("range") != null ? (String) request.getAttribute("range") : "all";
    String fromAttr = request.getAttribute("from") != null ? (String) request.getAttribute("from") : "";
    String toAttr = request.getAttribute("to") != null ? (String) request.getAttribute("to") : "";
    String admTx = request.getContextPath() + "/admin/dashboard?action=transactions";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction Logs | ArthaFlow Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="adminSidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">System Transaction Logs</h2><p style="font-size:0.78rem;color:var(--text-muted);">All transactions across the ArthaFlow network</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar" style="background:var(--danger);">A</div>
                <div><div class="navbar-name"><%= admin.getFullName() %></div><div class="navbar-role">Administrator</div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>Global Transaction Logs</h1><p>All deposits and withdrawals across the ArthaFlow network in NPR.</p></div>

            <div class="card" style="margin-bottom:1rem;padding:0.75rem 1rem;">
                <div style="font-size:0.8rem;color:var(--text-muted);margin-bottom:0.5rem;">Filter by date</div>
                <div style="display:flex;flex-wrap:wrap;gap:0.5rem;align-items:center;">
                    <a href="<%= admTx %>&range=all" class="btn btn-sm <%= "all".equals(rangeAttr) ? "btn-primary" : "btn-outline" %>">All</a>
                    <a href="<%= admTx %>&range=15" class="btn btn-sm <%= "15".equals(rangeAttr) ? "btn-primary" : "btn-outline" %>">Last 15 days</a>
                    <a href="<%= admTx %>&range=30" class="btn btn-sm <%= "30".equals(rangeAttr) ? "btn-primary" : "btn-outline" %>">Last 30 days</a>
                    <form method="get" action="<%= request.getContextPath() %>/admin/dashboard" style="display:flex;flex-wrap:wrap;gap:0.4rem;align-items:center;margin-left:0.25rem;">
                        <input type="hidden" name="action" value="transactions">
                        <input type="hidden" name="range" value="custom">
                        <label style="font-size:0.8rem;">From</label>
                        <input type="date" name="from" value="<%= fromAttr %>" class="form-control" style="max-width:140px;padding:0.35rem 0.5rem;font-size:0.8rem;">
                        <label style="font-size:0.8rem;">To</label>
                        <input type="date" name="to" value="<%= toAttr %>" class="form-control" style="max-width:140px;padding:0.35rem 0.5rem;font-size:0.8rem;">
                        <button type="submit" class="btn btn-outline btn-sm">Apply range</button>
                    </form>
                </div>
            </div>

            <div class="card">
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr><th>TX ID</th><th>Account</th><th>Type</th><th>Amount (Rs.)</th><th>Balance After</th><th>Date</th><th>Status</th></tr>
                        </thead>
                        <tbody>
                        <% if (transactions != null && !transactions.isEmpty()) {
                               for (Transaction t : transactions) { %>
                        <tr>
                            <td class="text-muted">#<%= t.getTransactionId() %></td>
                            <td>Acc #<%= t.getAccountId() %></td>
                            <td><span class="badge <%= "DEPOSIT".equals(t.getType()) ? "badge-success" : "badge-danger" %>"><%= t.getType() %></span></td>
                            <td class="fw-bold <%= "DEPOSIT".equals(t.getType()) ? "text-green" : "text-danger" %>">
                                Rs. <%= String.format("%,.2f", t.getAmount()) %>
                            </td>
                            <td>Rs. <%= String.format("%,.2f", t.getBalanceAfter()) %></td>
                            <td class="text-muted" style="font-size:0.82rem;"><%= t.getTransactionDate() %></td>
                            <td><span class="badge badge-success"><%= t.getStatus() %></span></td>
                        </tr>
                        <% } } else { %>
                        <tr><td colspan="7" style="text-align:center;padding:3rem;color:var(--text-muted);">No transactions recorded in the system yet.</td></tr>
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