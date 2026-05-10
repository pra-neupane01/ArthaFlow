<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Account" %>
<%@ page import="com.arthaflow.model.Transaction" %>
<%@ page import="com.arthaflow.service.AccountService" %>
<%@ page import="com.arthaflow.service.TransactionService" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    AccountService accountService = new AccountService();
    TransactionService transactionService = new TransactionService();
    Account account = accountService.getAccountDetails(user.getUserId());
    List<Transaction> transactions = transactionService.getTransactionHistory(user.getUserId());
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;

    double balance      = (account != null) ? account.getBalance() : 0.0;
    boolean isActive    = account != null && "ACTIVE".equals(account.getStatus());
    boolean isPending   = account != null && "PENDING".equals(account.getStatus());
    boolean noAccount   = account == null;
    int txCount         = (transactions != null) ? transactions.size() : 0;

    double totalIn  = 0, totalOut = 0;
    if (transactions != null) {
        for (Transaction t : transactions) {
            if ("DEPOSIT".equals(t.getType())) totalIn += t.getAmount();
            else totalOut += t.getAmount();
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
    <style>
        .welcome-banner {
            background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 60%, var(--primary-light) 100%);
            border-radius: var(--radius-lg);
            padding: 2rem 2.5rem;
            color: white;
            position: relative;
            overflow: hidden;
            margin-bottom: 1.75rem;
        }
        .welcome-banner::before {
            content: '';
            position: absolute;
            right: -40px; top: -40px;
            width: 220px; height: 220px;
            border-radius: 50%;
            background: rgba(255,255,255,0.06);
        }
        .welcome-banner::after {
            content: '';
            position: absolute;
            right: 60px; bottom: -60px;
            width: 160px; height: 160px;
            border-radius: 50%;
            background: rgba(255,255,255,0.04);
        }
        .balance-display {
            font-size: 2.5rem;
            font-weight: 800;
            letter-spacing: -0.02em;
            margin: 0.25rem 0 0.5rem;
        }
        .kyc-banner {
            background: linear-gradient(135deg, #92400e, #d97706);
            border-radius: var(--radius-md);
            padding: 1.25rem 1.75rem;
            color: white;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 1rem;
            margin-bottom: 1.75rem;
            flex-wrap: wrap;
        }
        .kyc-banner-no-account {
            background: linear-gradient(135deg, #1e40af, #2563eb);
        }
        .quick-action-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 1rem;
            margin-bottom: 1.75rem;
        }
        .qa-card {
            background: var(--white);
            border: 1.5px solid var(--border);
            border-radius: var(--radius-md);
            padding: 1.25rem;
            text-align: center;
            text-decoration: none;
            color: var(--text);
            transition: all 0.2s;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 0.6rem;
        }
        .qa-card:hover {
            border-color: var(--primary);
            box-shadow: 0 4px 16px rgba(10,124,78,0.15);
            transform: translateY(-2px);
            color: var(--primary);
        }
        .qa-icon {
            width: 48px; height: 48px;
            border-radius: 14px;
            background: var(--primary-bg);
            display: flex; align-items: center; justify-content: center;
        }
        .qa-icon svg { width: 22px; height: 22px; stroke: var(--primary); }
        .qa-label { font-size: 0.85rem; font-weight: 600; }
        .stat-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 1.25rem; margin-bottom: 1.75rem; }
        .mini-stat {
            background: var(--white);
            border: 1px solid var(--border);
            border-radius: var(--radius-md);
            padding: 1.25rem 1.5rem;
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        .mini-stat-icon {
            width: 44px; height: 44px;
            border-radius: 12px;
            display: flex; align-items: center; justify-content: center;
            flex-shrink: 0;
        }
        .mini-stat-icon.green { background: #d1fae5; }
        .mini-stat-icon.red   { background: #fee2e2; }
        .mini-stat-icon.blue  { background: #dbeafe; }
        .mini-stat-icon svg { width: 20px; height: 20px; }
        .mini-stat-icon.green svg { stroke: var(--success); }
        .mini-stat-icon.red svg   { stroke: var(--danger); }
        .mini-stat-icon.blue svg  { stroke: #2563eb; }
    </style>
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />

    <div class="main-content">
        <!-- TOP NAVBAR -->
        <div class="top-navbar">
            <div>
                <h2 style="font-size:1.05rem;font-weight:700;color:var(--text);">Good day, <%= user.getFullName().split(" ")[0] %> 👋</h2>
                <div style="display:flex;align-items:center;gap:0.5rem;font-size:0.78rem;color:var(--text-muted);">
                    <span>Here's your ArthaFlow financial summary</span>
                    <span>&bull;</span>
                    <span id="liveClock" style="font-weight:600;color:var(--primary);"></span>
                </div>
            </div>
            <div style="display:flex;align-items:center;gap:1rem;">
                <a href="<%= request.getContextPath() %>/user/transaction?action=deposit"
                   style="display:flex;align-items:center;gap:0.4rem;padding:0.55rem 1.1rem;background:var(--primary);color:white;border-radius:8px;font-size:0.85rem;font-weight:600;text-decoration:none;">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="5" x2="12" y2="19"/><polyline points="19 12 12 19 5 12"/></svg>
                    Deposit
                </a>
                <a href="<%= request.getContextPath() %>/user/transaction?action=withdraw"
                   style="display:flex;align-items:center;gap:0.4rem;padding:0.55rem 1.1rem;background:var(--bg);color:var(--text);border:1.5px solid var(--border);border-radius:8px;font-size:0.85rem;font-weight:600;text-decoration:none;">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><line x1="12" y1="19" x2="12" y2="5"/><polyline points="5 12 12 5 19 12"/></svg>
                    Withdraw
                </a>
                <div class="navbar-user">
                    <div class="navbar-avatar">
                        <% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %>
                    </div>
                </div>
            </div>
        </div>

        <div class="page-content">

            <!-- KYC WARNING BANNERS -->
            <% if (noAccount) { %>
            <div class="kyc-banner kyc-banner-no-account">
                <div>
                    <div style="font-weight:700;font-size:1rem;margin-bottom:0.25rem;">No Bank Account Found</div>
                    <div style="font-size:0.88rem;opacity:0.9;">You need to open an account and complete KYC verification before you can deposit or withdraw money.</div>
                </div>
                <a href="<%= request.getContextPath() %>/user/account" style="background:white;color:#1e40af;padding:0.6rem 1.25rem;border-radius:8px;font-weight:700;font-size:0.85rem;text-decoration:none;white-space:nowrap;">Open Account &rarr;</a>
            </div>
            <% } else if (isPending) { %>
            <div class="kyc-banner">
                <div>
                    <div style="font-weight:700;font-size:1rem;margin-bottom:0.25rem;">KYC Verification Pending</div>
                    <div style="font-size:0.88rem;opacity:0.9;">Your documents are under review. Transactions will be enabled once our admin approves your KYC and issues your account number.</div>
                </div>
                <a href="<%= request.getContextPath() %>/user/account" style="background:white;color:#92400e;padding:0.6rem 1.25rem;border-radius:8px;font-weight:700;font-size:0.85rem;text-decoration:none;white-space:nowrap;">Check Status &rarr;</a>
            </div>
            <% } %>

            <!-- WELCOME BANNER -->
            <div class="welcome-banner">
                <div style="position:relative;z-index:1;">
                    <div style="font-size:0.8rem;opacity:0.75;text-transform:uppercase;letter-spacing:0.1em;margin-bottom:0.25rem;">
                        <%= account != null ? account.getAccountType() + " Account" : "ArthaFlow Account" %>
                        <% if (account != null && account.getAccount_number() != null) { %>
                         &nbsp;·&nbsp; #<%= account.getAccount_number() %>
                        <% } %>
                    </div>
                    <div class="balance-display">Rs. <%= String.format("%,.2f", balance) %></div>
                    <div style="font-size:0.88rem;opacity:0.8;">
                        <% if (isActive) { %>
                            <span style="background:rgba(255,255,255,0.15);padding:3px 10px;border-radius:99px;">Active &amp; Verified</span>
                        <% } else if (isPending) { %>
                            <span style="background:rgba(255,255,255,0.15);padding:3px 10px;border-radius:99px;">KYC Pending Approval</span>
                        <% } else { %>
                            <span style="background:rgba(255,255,255,0.15);padding:3px 10px;border-radius:99px;">No Account — Please Apply</span>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- MINI STATS ROW -->
            <div class="stat-row">
                <div class="mini-stat">
                    <div class="mini-stat-icon green">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 6 13.5 15.5 8.5 10.5 1 18"/><polyline points="17 6 23 6 23 12"/></svg>
                    </div>
                    <div>
                        <div class="stat-label">Total Deposited</div>
                        <div style="font-size:1.15rem;font-weight:700;color:var(--success);">Rs. <%= String.format("%,.2f", totalIn) %></div>
                    </div>
                </div>
                <div class="mini-stat">
                    <div class="mini-stat-icon red">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="23 18 13.5 8.5 8.5 13.5 1 6"/><polyline points="17 18 23 18 23 12"/></svg>
                    </div>
                    <div>
                        <div class="stat-label">Total Withdrawn</div>
                        <div style="font-size:1.15rem;font-weight:700;color:var(--danger);">Rs. <%= String.format("%,.2f", totalOut) %></div>
                    </div>
                </div>
                <div class="mini-stat">
                    <div class="mini-stat-icon blue">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"/></svg>
                    </div>
                    <div>
                        <div class="stat-label">Transactions</div>
                        <div style="font-size:1.15rem;font-weight:700;color:#2563eb;"><%= txCount %> total</div>
                    </div>
                </div>
            </div>

            <!-- INCOME VS EXPENSE CHART AND QUICK ACTIONS -->
            <div style="display: grid; grid-template-columns: 1fr 2fr; gap: 1.75rem; margin-bottom: 1.75rem;">
                <div class="card" style="margin-bottom: 0;">
                    <div class="card-header">
                        <h3 style="font-size:1rem;font-weight:700;">Income vs Expense</h3>
                    </div>
                    <div style="padding: 1rem; position: relative; height: 200px;">
                        <canvas id="incomeExpenseChart"></canvas>
                    </div>
                </div>
                <div>
                    <h3 style="font-size:1rem;font-weight:700;margin-bottom:1rem;">Quick Actions</h3>
                    <div class="quick-action-grid" style="margin-bottom: 0; grid-template-columns: repeat(2, 1fr);">
                        <a href="<%= request.getContextPath() %>/user/transaction?action=deposit" class="qa-card">
                            <div class="qa-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><polyline points="19 12 12 19 5 12"/></svg>
                            </div>
                            <span class="qa-label">Deposit</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/user/transaction?action=withdraw" class="qa-card">
                            <div class="qa-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="19" x2="12" y2="5"/><polyline points="5 12 12 5 19 12"/></svg>
                            </div>
                            <span class="qa-label">Withdraw</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/user/account" class="qa-card">
                            <div class="qa-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>
                            </div>
                            <span class="qa-label">My Account</span>
                        </a>
                        <a href="<%= request.getContextPath() %>/user/creditCard" class="qa-card">
                            <div class="qa-icon">
                                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
                            </div>
                            <span class="qa-label">Credit Card</span>
                        </a>
                    </div>
                </div>
            </div>

            <!-- TRANSACTIONS TABLE -->
            <div class="card">
                <div class="card-header">
                    <h3 style="font-size:1rem;font-weight:700;">Recent Transactions</h3>
                    <a href="<%= request.getContextPath() %>/user/transaction?action=history"
                       style="font-size:0.82rem;color:var(--primary);font-weight:600;text-decoration:none;">View all &rarr;</a>
                </div>
                <% if (transactions == null || transactions.isEmpty()) { %>
                <div style="text-align:center;padding:3rem;color:var(--text-muted);">
                    <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" style="display:block;margin:0 auto 1rem;opacity:0.3;"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
                    <p style="font-weight:600;margin-bottom:0.25rem;">No transactions yet</p>
                    <p style="font-size:0.85rem;">Your banking activity will appear here after your first deposit.</p>
                </div>
                <% } else { %>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Type</th>
                                <th>Amount</th>
                                <th>Balance After</th>
                                <th>Remarks</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            int cnt = 0;
                            for (Transaction t : transactions) {
                                if (cnt++ >= 7) break;
                        %>
                        <tr>
                            <td>
                                <div style="display:flex;align-items:center;gap:0.5rem;">
                                    <div style="width:32px;height:32px;border-radius:8px;background:<%= "DEPOSIT".equals(t.getType()) ? "#d1fae5" : "#fee2e2" %>;display:flex;align-items:center;justify-content:center;">
                                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="<%= "DEPOSIT".equals(t.getType()) ? "#059669" : "#dc2626" %>" stroke-width="2.5">
                                            <% if ("DEPOSIT".equals(t.getType())) { %>
                                            <line x1="12" y1="5" x2="12" y2="19"/><polyline points="19 12 12 19 5 12"/>
                                            <% } else { %>
                                            <line x1="12" y1="19" x2="12" y2="5"/><polyline points="5 12 12 5 19 12"/>
                                            <% } %>
                                        </svg>
                                    </div>
                                    <span style="font-weight:600;font-size:0.85rem;color:<%= "DEPOSIT".equals(t.getType()) ? "var(--success)" : "var(--danger)" %>">
                                        <%= t.getType() %>
                                    </span>
                                </div>
                            </td>
                            <td style="font-weight:700;color:<%= "DEPOSIT".equals(t.getType()) ? "var(--success)" : "var(--danger)" %>">
                                <%= "DEPOSIT".equals(t.getType()) ? "+" : "-" %>Rs. <%= String.format("%,.2f", t.getAmount()) %>
                            </td>
                            <td>Rs. <%= String.format("%,.2f", t.getBalanceAfter()) %></td>
                            <td class="text-muted" style="font-size:0.83rem;"><%= t.getRemarks() != null && !t.getRemarks().isEmpty() ? t.getRemarks() : "—" %></td>
                            <td class="text-muted" style="font-size:0.8rem;"><%= t.getTransactionDate().toString().substring(0, 16) %></td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
                <% } %>
            </div>
        </div><!-- end page-content -->
    </div><!-- end main-content -->
</div><!-- end page-wrapper -->

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    // Live Clock
    function updateClock() {
        const now = new Date();
        const options = { weekday: 'short', year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' };
        document.getElementById('liveClock').textContent = now.toLocaleDateString('en-US', options);
    }
    setInterval(updateClock, 1000);
    updateClock();

    // Income vs Expense Chart
    const ctx = document.getElementById('incomeExpenseChart').getContext('2d');
    const income = <%= totalIn %>;
    const expense = <%= totalOut %>;
    
    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Deposited (Income)', 'Withdrawn (Expense)'],
            datasets: [{
                data: [income, expense],
                backgroundColor: ['#10b981', '#ef4444'],
                borderWidth: 0,
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '70%',
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { boxWidth: 12, font: { size: 11, family: "'Inter', sans-serif" } }
                }
            }
        }
    });
</script>
</body>
</html>