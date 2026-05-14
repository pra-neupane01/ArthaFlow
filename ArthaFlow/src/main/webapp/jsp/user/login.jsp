<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="auth-page">
    <!-- LEFT PANEL -->
    <div class="auth-left">
        <div style="margin-bottom: 3rem;">
            <a href="<%= request.getContextPath() %>/" style="text-decoration: none; display: flex; align-items: center; gap: 0.75rem; margin-bottom: 3rem;">
                <div class="logo-mark" style="width:42px;height:42px;background:rgba(255,255,255,0.2);border-radius:10px;display:flex;align-items:center;justify-content:center;color:white;font-weight:800;font-size:1.2rem;">A</div>
                <span style="color:white;font-weight:800;font-size:1.2rem;">ArthaFlow</span>
            </a>
            <h2 style="font-size: 2rem; font-weight: 800; margin-bottom: 1rem;">Welcome back to ArthaFlow</h2>
            <p style="font-size: 1rem; opacity: 0.8; line-height: 1.7;">Nepal's most trusted digital banking platform. Secure, fast, and always with you.</p>
        </div>
        <div style="background: rgba(255,255,255,0.08); border-radius: 16px; padding: 1.5rem;">
            <div style="font-size: 0.8rem; opacity: 0.7; text-transform: uppercase; letter-spacing: 0.1em; margin-bottom: 0.75rem;">Trusted By</div>
            <div style="font-weight: 600; font-size: 0.95rem;">🎓 Itahari International College</div>
            <div style="font-size: 0.85rem; opacity: 0.7; margin-top: 0.25rem;">Faculty & Students</div>
        </div>
    </div>

    <!-- RIGHT PANEL (FORM) -->
    <div class="auth-right">
        <div style="margin-bottom: 2rem;">
            <h2>Namaste! 🙏</h2>
            <p class="auth-sub">Enter your credentials to access your account.</p>
        </div>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">⚠ <%= request.getAttribute("error") %></div>
        <% } %>
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">✓ <%= request.getAttribute("success") %></div>
        <% } %>
        <% if ("1".equals(request.getParameter("timeout"))) { %>
            <div class="alert alert-danger">Session timed out due to inactivity. Please sign in again.</div>
        <% } %>

        <form action="<%= request.getContextPath() %>/login" method="POST">
            <div class="form-group">
                <label class="form-label">Email Address</label>
                <input type="email" name="email" class="form-control" required placeholder="your@email.com">
            </div>
            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required placeholder="Enter your password">
            </div>
            <button type="submit" class="btn btn-primary btn-full" style="margin-top: 0.5rem; padding: 0.85rem;">
                Login to ArthaFlow
            </button>
        </form>

        <div class="divider"></div>
        <p style="text-align: center; color: var(--text-muted); font-size: 0.9rem;">
            New to ArthaFlow?
            <a href="<%= request.getContextPath() %>/register" style="color: var(--primary); font-weight: 600; text-decoration: none;">Open a free account →</a>
        </p>
    </div>
</div>
</body>
</html>