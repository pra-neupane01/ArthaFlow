<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - ArthaFlow</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>

<div class="login-page">

    <div class="brand">
        <h1>ArthaFlow</h1>
        <p>Secure Digital Banking Access</p>
    </div>

    <div class="login-card">
        <h2>Welcome back</h2>
        <p class="subtitle">Please enter your credentials to continue</p>

        <div id="loginError" class="error-box" style="display:none;">
            <strong>Invalid Email or Password</strong>
            <span>Please check your details and try again.</span>
        </div>

        <form action="${pageContext.request.contextPath}/login"
              method="post"
              onsubmit="return validateLoginForm()"
              novalidate>

            <label>Email Address</label>
            <input type="email" id="email" name="email" placeholder="yourname@gmail.com">

            <div class="password-row">
                <label>Password</label>
                <a href="#">Forgot Password?</a>
            </div>

            <input type="password" id="password" name="password" placeholder="••••••••">

            <div class="remember-row">
                <input type="checkbox" id="remember">
                <label for="remember">Stay logged in for 30 days</label>
            </div>

            <button type="submit">Login to Dashboard →</button>
        </form>

        <p class="register-text">
            New to ArthaFlow?
            <a href="${pageContext.request.contextPath}/jsp/user/register.jsp">Open Account</a>
        </p>
    </div>

    <p class="security-text"> AES-256 Bank-Grade Encryption</p>

</div>

<script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>