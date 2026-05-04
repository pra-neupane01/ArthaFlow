<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>ArthaFlow - Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Header Section -->
<header class="navbar">
    <div class="logo">ArthaFlow</div>

    <nav class="nav-links">
        <a href="#" class="active">Home</a>
        <a href="#">Personal</a>
        <a href="#">Business</a>
        <a href="#">Investment</a>
    </nav>

    <div class="nav-actions">
        <a href="${pageContext.request.contextPath}/jsp/user/login.jsp" class="login-link">Login</a>
        <a href="${pageContext.request.contextPath}/jsp/user/register.jsp" class="register-btn">Register</a>
    </div>
</header>

<!-- Hero Section -->
<section class="hero-section">
    <h1>Welcome to ArthaFlow</h1>
    <p>
        Manage your banking digitally with ease and security.
        Experience the next generation of financial control at your fingertips.
    </p>

    <div class="hero-buttons">
        <a href="${pageContext.request.contextPath}/jsp/user/login.jsp" class="primary-btn">Login</a>
        <a href="${pageContext.request.contextPath}/jsp/user/register.jsp" class="secondary-btn">Register</a>
    </div>
</section>

<!-- Feature Section -->
<section class="feature-section">
    <h2>Financial management made simple</h2>
    <p class="section-description">
        Our platform provides the tools you need to manage your financial journey securely.
    </p>

    <div class="feature-grid">
        <div class="feature-card">
            <h3>Secure Transactions</h3>
            <p>Your money and account information are handled safely.</p>
        </div>

        <div class="feature-card">
            <h3>Easy Account Management</h3>
            <p>View and manage account details through a simple dashboard.</p>
        </div>

        <div class="feature-card">
            <h3>Fast Deposits & Withdrawals</h3>
            <p>Perform banking operations quickly and easily.</p>
        </div>
    </div>
</section>

<!-- CTA Section -->
<section class="cta-section">
    <div class="cta-box">
        <h2>Ready to optimize your flow?</h2>
        <p>Start managing your banking activities with ArthaFlow.</p>
        <a href="${pageContext.request.contextPath}/jsp/user/register.jsp">Open Account</a>
    </div>
    <div class="stat-box">
        <h2>24/7</h2>
        <p>SUPPORT</p>
    </div>
</section>

<!-- Footer Section -->
<footer class="footer">
    <div>
        <h3>ArthaFlow</h3>
        <p>© 2026 ArthaFlow | All rights reserved</p>
    </div>

    <div class="footer-links">
        <a href="#">Privacy</a>
        <a href="#">Terms</a>
        <a href="#">Contact</a>
        <a href="#">Security</a>
    </div>
</footer>

</body>
</html>