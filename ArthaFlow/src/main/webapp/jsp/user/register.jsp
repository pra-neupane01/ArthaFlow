<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Register - ArthaFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body>

<header class="register-navbar">
    <div class="logo">ArthaFlow</div>

    <nav>
        <a href="${pageContext.request.contextPath}/index.jsp">Home</a>
        <a href="#">Dashboard</a>
        <a href="#">Payments</a>
        <a href="#">History</a>
        <a href="#">Support</a>
    </nav>

    <div class="nav-actions">
        <a href="${pageContext.request.contextPath}/jsp/user/login.jsp" class="login-link">Login</a>
        <a href="${pageContext.request.contextPath}/jsp/user/register.jsp" class="open-btn">Open Account</a>
    </div>
</header>

<main class="register-page">

    <section class="register-info">
        <h1>Start your financial journey with ArthaFlow.</h1>
        <p>
            Join users managing their accounts with secure digital banking
            and modern financial tools.
        </p>

        <div class="info-item">
            <span>✓</span>
            <p>Institutional-grade encryption</p>
        </div>

        <div class="info-item">
            <span>✓</span>
            <p>Real-time banking access</p>
        </div>
    </section>

    <section class="register-card">
        <h2>Create Account</h2>
        <p class="subtitle">Secure access to your ArthaFlow banking dashboard.</p>

        <div id="registerError" class="error-box" style="display:none;">
            <strong>Invalid Registration Details</strong>
            <span>Please check your email, password, and confirmation.</span>
        </div>

        <form action="${pageContext.request.contextPath}/jsp/user/userDashboard.jsp"
              method="post"
              onsubmit="return validateRegisterForm()"
              novalidate>

            <label>Full Name</label>
            <input type="text" id="fullName" name="fullName" placeholder="syaron Rai">

            <label>Email Address</label>
            <input type="email" id="registerEmail" name="email" placeholder="yourname@gmail.com">

            <label>Password</label>
            <input type="password" id="registerPassword" name="password" placeholder="......">

            <label>Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="......">

            <button type="submit">Register</button>
        </form>

        <p class="register-text">
            Already have an account?
            <a href="${pageContext.request.contextPath}/jsp/user/login.jsp">Login</a>
        </p>

        <p class="terms">
            By registering, you agree to our
            <a href="#">Terms of Service</a> and
            <a href="#">Privacy Policy</a>.
        </p>
    </section>

</main>

<footer class="register-footer">
    <div>
        <h3>ArthaFlow</h3>
        <p>© 2026 ArthaFlow | Secure Financial Management</p>
    </div>

    <div class="footer-links">
        <a href="#">Privacy Policy</a>
        <a href="#">Terms of Service</a>
        <a href="#">Security</a>
        <a href="#">Cookie Settings</a>
    </div>
</footer>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>