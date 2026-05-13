<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Transaction" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<Transaction> transactions = (List<Transaction>) request.getAttribute("transactions");
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;
    String rangeAttr = request.getAttribute("range") != null ? (String) request.getAttribute("range") : "all";
    String fromAttr = request.getAttribute("from") != null ? (String) request.getAttribute("from") : "";
    String toAttr = request.getAttribute("to") != null ? (String) request.getAttribute("to") : "";
    String txBase = request.getContextPath() + "/user/transaction";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transaction History | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div>
                <h2 style="font-size:1rem;font-weight:700;">Transaction History</h2>
                <p style="font-size:0.78rem;color:var(--text-muted);">Full ledger of your account activity</p>
            </div>
            <div class="navbar-user">
                <div class="navbar-avatar">
                    <% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %>
                </div>
                <div><div class="navbar-name"><%= user.getFullName() %></div><div class="navbar-role"><%= user.getRole() %></div></div>
            </div>
        </div>

        <div class="page-content">
            <div class="page-header-row" style="align-items: flex-end; margin-bottom: 1.5rem;">
                <div class="page-header" style="margin-bottom: 0;">
                    <h1>All Transactions</h1>
                    <p>Your complete banking activity in Nepalese Rupees (NPR).</p>
                </div>
                <div style="display:flex;flex-direction:column;gap:0.75rem;align-items:flex-end;">
                    <div style="display:flex;gap:0.75rem;">
                        <a href="<%= request.getContextPath() %>/user/transaction?action=deposit" class="btn btn-primary btn-sm">+ Deposit</a>
                        <a href="<%= request.getContextPath() %>/user/transaction?action=withdraw" class="btn btn-outline btn-sm">- Withdraw</a>
                    </div>
                    <div style="position:relative; width: 100%; max-width: 300px;">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" style="position:absolute;left:10px;top:50%;transform:translateY(-50%);width:16px;height:16px;color:var(--text-muted);"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                        <input type="text" id="transactionSearch" placeholder="Search transactions..." style="width:100%;padding:0.4rem 0.6rem 0.4rem 2rem;border:1.5px solid var(--border);border-radius:6px;font-size:0.85rem;">
                    </div>
                </div>
            </div>

            <div class="card" style="margin-bottom:1rem;padding:0.75rem 1rem;">
                <div style="font-size:0.8rem;color:var(--text-muted);margin-bottom:0.5rem;">Filter by date</div>
                <div style="display:flex;flex-wrap:wrap;gap:0.5rem;align-items:center;">
                    <a href="<%= txBase %>?range=all" class="btn btn-sm <%= "all".equals(rangeAttr) ? "btn-primary" : "btn-outline" %>">All</a>
                    <a href="<%= txBase %>?range=15" class="btn btn-sm <%= "15".equals(rangeAttr) ? "btn-primary" : "btn-outline" %>">Last 15 days</a>
                    <a href="<%= txBase %>?range=30" class="btn btn-sm <%= "30".equals(rangeAttr) ? "btn-primary" : "btn-outline" %>">Last 30 days</a>
                    <form method="get" action="<%= txBase %>" style="display:flex;flex-wrap:wrap;gap:0.4rem;align-items:center;margin-left:0.25rem;">
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
                            <tr><th>#</th><th>Type</th><th>Amount (Rs.)</th><th>Balance After</th><th>Date</th><th>Remarks</th><th>Status</th></tr>
                        </thead>
                        <tbody>
                        <% if (transactions != null && !transactions.isEmpty()) {
                               for (Transaction t : transactions) { %>
                        <tr>
                            <td class="text-muted">#<%= t.getTransactionId() %></td>
                            <td>
                                <span class="badge <%= "DEPOSIT".equals(t.getType()) ? "badge-success" : "badge-danger" %>">
                                    <%= "DEPOSIT".equals(t.getType()) ? "↑" : "↓" %> <%= t.getType() %>
                                </span>
                            </td>
                            <td class="fw-bold <%= "DEPOSIT".equals(t.getType()) ? "text-green" : "text-danger" %>">
                                <%= "DEPOSIT".equals(t.getType()) ? "+" : "-" %>Rs. <%= String.format("%,.2f", t.getAmount()) %>
                            </td>
                            <td>Rs. <%= String.format("%,.2f", t.getBalanceAfter()) %></td>
                            <td class="text-muted" style="font-size:0.82rem;"><%= t.getTransactionDate() %></td>
                            <td class="text-muted"><%= t.getRemarks() != null ? t.getRemarks() : "—" %></td>
                            <td><span class="badge badge-success"><%= t.getStatus() %></span></td>
                        </tr>
                        <% } } else { %>
                        <tr><td colspan="7" style="text-align:center;padding:3rem;color:var(--text-muted);">No transactions recorded yet.</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    document.getElementById('transactionSearch').addEventListener('input', function(e) {
        const searchTerm = e.target.value.toLowerCase();
        const rows = document.querySelectorAll('.data-table tbody tr');
        
        rows.forEach(row => {
            // Skip the "No transactions recorded yet" row if present
            if (row.cells.length === 1) return;
            
            const textContent = row.textContent.toLowerCase();
            if (textContent.includes(searchTerm)) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    });
</script>
</body>
</html>
