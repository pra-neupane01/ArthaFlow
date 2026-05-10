<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="auth-page">
    <!-- LEFT PANEL -->
    <div class="auth-left">
        <a href="<%= request.getContextPath() %>/" style="text-decoration:none;display:flex;align-items:center;gap:0.75rem;margin-bottom:3rem;">
            <div style="width:42px;height:42px;background:rgba(255,255,255,0.2);border-radius:10px;display:flex;align-items:center;justify-content:center;color:white;font-weight:800;font-size:1.2rem;">A</div>
            <span style="color:white;font-weight:800;font-size:1.2rem;">ArthaFlow</span>
        </a>
        <h2 style="font-size:2rem;font-weight:800;margin-bottom:1rem;">Start your banking journey</h2>
        <p style="opacity:0.8;line-height:1.7;margin-bottom:2rem;">Join hundreds of students and professionals from Itahari International College who already trust ArthaFlow.</p>
        <div style="display:flex;flex-direction:column;gap:0.75rem;">
            <div style="display:flex;align-items:center;gap:0.75rem;"><span>✅</span><span style="opacity:0.9;">Free account opening</span></div>
            <div style="display:flex;align-items:center;gap:0.75rem;"><span>🔒</span><span style="opacity:0.9;">KYC-verified security</span></div>
            <div style="display:flex;align-items:center;gap:0.75rem;"><span>💳</span><span style="opacity:0.9;">Credit card eligibility</span></div>
        </div>
    </div>

    <!-- RIGHT PANEL -->
    <div class="auth-right" style="width:540px; overflow-y:auto;">
        <h2>Create Your Account</h2>
        <p class="auth-sub">Fill in your details to get started. All amounts in NPR (Rs.).</p>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">⚠ <%= request.getAttribute("error") %></div>
        <% } %>

        <form action="<%= request.getContextPath() %>/register" method="POST">
            <div class="form-group">
                <label class="form-label">Full Name (as per ID)</label>
                <input type="text" name="fullName" class="form-control" required placeholder="Ex: Rajesh Kumar Hamal">
            </div>
            <div class="grid-2">
                <div class="form-group">
                    <label class="form-label">Email Address</label>
                    <input type="email" name="email" class="form-control" required placeholder="you@example.com">
                </div>
                <div class="form-group">
                    <label class="form-label">Phone Number</label>
                    <div class="input-group">
                        <span class="input-prefix">+977</span>
                        <input type="tel" name="phoneNumber" class="form-control" required placeholder="98XXXXXXXX">
                    </div>
                </div>
            </div>
            <div class="form-group">
                <label class="form-label">Current Address</label>
                <input type="text" name="address" class="form-control" required placeholder="Ex: Sundar Haraicha 04, Dulari, Itahari">
            </div>
            <div class="form-group">
                <label class="form-label">Password</label>
                <input type="password" name="password" class="form-control" required placeholder="Min 8 chars, upper+lower+number+special">
            </div>

            <button type="submit" class="btn btn-primary btn-full" style="margin-top: 0.5rem; padding: 0.85rem;">
                Register Now →
            </button>
        </form>

        <div class="divider"></div>
        <p style="text-align:center;color:var(--text-muted);font-size:0.9rem;">
            Already have an account?
            <a href="<%= request.getContextPath() %>/login" style="color:var(--primary);font-weight:600;text-decoration:none;">Sign In</a>
        </p>
    </div>
</div>
</body>
</html>