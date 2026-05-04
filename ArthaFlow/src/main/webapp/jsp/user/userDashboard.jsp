<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Dashboard - ArthaFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
</head>
<body>

<div class="dashboard-layout">

    <!-- SIDEBAR -->
    <aside class="sidebar">
        <div class="brand">
            <h2>ArthaFlow</h2>
            <p>Digital Banking</p>
        </div>

        <nav class="side-nav">
            <a href="#" class="active">Dashboard</a>
            <a href="accountDetails.jsp">Account Details</a>
            <a href="deposit.jsp">Deposit</a>
            <a href="withdraw.jsp">Withdraw</a>
            <a href="transactionHistory.jsp">Transactions</a>
            <a href="profile.jsp">Profile</a>
        </nav>

        <div class="user-box">
            <h4>Seron Rai</h4>
            <p>Premium Member</p>
        </div>
    </aside>

    <!-- MAIN CONTENT -->
    <main class="main-content">

        <!-- TOPBAR -->
        <header class="topbar">
            <h1>Dashboard</h1>

            <div class="top-actions">
                <input type="text" placeholder="Search transactions...">
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Logout</a>
            </div>
        </header>

        <!-- BALANCE + ACTIONS -->
        <section class="overview-grid">

            <div class="balance-card">
                <p>CURRENT BALANCE</p>
                <h2>Rs. 248,190.42</h2>

                <div class="balance-info">
                    <span>Fully Secured</span>
                    <span>Active Account</span>
                </div>
            </div>

            <div class="quick-actions">
                <a href="deposit.jsp" class="action-card">
                    <h3>Deposit</h3>
                    <p>Add money to your account</p>
                </a>

                <a href="withdraw.jsp" class="action-card">
                    <h3>Withdraw</h3>
                    <p>Transfer money safely</p>
                </a>
            </div>

        </section>

        <!-- TRANSACTIONS -->
        <section class="content-grid">

            <div class="transactions-card">
                <div class="section-header">
                    <h2>Recent Transactions</h2>
                    <a href="transactionHistory.jsp">View All</a>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>Recipient / Sender</th>
                        <th>Type</th>
                        <th>Date</th>
                        <th>Status</th>
                        <th>Amount</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                        <td>Deposit Account</td>
                        <td>Deposit</td>
                        <td>May 03, 2026</td>
                        <td><span class="success">Completed</span></td>
                        <td class="green">+ Rs. 5,000</td>
                    </tr>

                    <tr>
                        <td>ATM Withdrawal</td>
                        <td>Withdraw</td>
                        <td>May 02, 2026</td>
                        <td><span class="pending">Pending</span></td>
                        <td class="red">- Rs. 2,000</td>
                    </tr>

                    <tr>
                        <td>Bank Transfer</td>
                        <td>Transfer</td>
                        <td>May 01, 2026</td>
                        <td><span class="success">Completed</span></td>
                        <td class="red">- Rs. 1,200</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="summary-card">
                <h2>Account Summary</h2>

                <div class="summary-item">
                    <span>Savings</span>
                    <strong>72%</strong>
                </div>

                <div class="summary-item">
                    <span>Withdrawn</span>
                    <strong>18%</strong>
                </div>

                <div class="summary-item">
                    <span>Reserved</span>
                    <strong>10%</strong>
                </div>
            </div>

        </section>

    </main>

</div>

</body>
</html>