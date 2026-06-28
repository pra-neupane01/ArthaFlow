<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
    <script>
        window.tailwind = window.tailwind || {};
        window.tailwind.config = { corePlugins: { preflight: false } };
    </script>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
<%
    boolean registrationSuccess = Boolean.TRUE.equals(request.getAttribute("registrationSuccess"));
%>
<div class="auth-page">
    <div class="auth-left">
        <a href="<%= request.getContextPath() %>/" style="text-decoration:none;display:flex;align-items:center;gap:0.75rem;margin-bottom:3rem;">
            <div style="width:42px;height:42px;background:rgba(255,255,255,0.2);border-radius:10px;display:flex;align-items:center;justify-content:center;color:white;font-weight:800;font-size:1.2rem;">A</div>
            <span style="color:white;font-weight:800;font-size:1.2rem;">ArthaFlow</span>
        </a>
        <h2 style="font-size:2rem;font-weight:800;margin-bottom:1rem;">Start your banking journey</h2>
        <p style="opacity:0.8;line-height:1.7;margin-bottom:2rem;">Join hundreds of students and professionals from Itahari International College who already trust ArthaFlow.</p>
        <div style="display:flex;flex-direction:column;gap:0.75rem;">
            <div style="display:flex;align-items:center;gap:0.75rem;"><span aria-hidden="true">&#10003;</span><span style="opacity:0.9;">Free account opening</span></div>
            <div style="display:flex;align-items:center;gap:0.75rem;"><span aria-hidden="true">&#10003;</span><span style="opacity:0.9;">KYC-verified security</span></div>
            <div style="display:flex;align-items:center;gap:0.75rem;"><span aria-hidden="true">&#10003;</span><span style="opacity:0.9;">Credit card eligibility</span></div>
        </div>
    </div>

    <div class="auth-right" style="width:540px; overflow-y:auto;">
        <% if (registrationSuccess) { %>
            <div class="w-full rounded-lg border border-emerald-200 bg-white p-8 text-center shadow-lg shadow-emerald-900/10" role="status">
                <div class="mx-auto mb-5 flex h-16 w-16 items-center justify-center rounded-full bg-emerald-100 text-emerald-700">
                    <svg class="h-9 w-9" viewBox="0 0 24 24" fill="none" aria-hidden="true">
                        <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"></path>
                    </svg>
                </div>
                <p class="mb-2 text-sm font-semibold uppercase text-emerald-700">Registration complete</p>
                <h2 class="mb-3 text-2xl font-extrabold text-slate-900">Welcome to ArthaFlow Bank</h2>
                <p class="mx-auto mb-6 max-w-sm text-sm leading-6 text-slate-600">
                    Your bank registration was successful. You can now sign in and continue your account setup.
                </p>
                <a href="<%= request.getContextPath() %>/login" class="inline-flex w-full items-center justify-center rounded-lg bg-emerald-700 px-5 py-3 text-sm font-semibold text-white no-underline transition hover:bg-emerald-800">
                    Continue to Login
                </a>
                <a href="<%= request.getContextPath() %>/" class="mt-3 inline-flex w-full items-center justify-center rounded-lg border border-emerald-200 px-5 py-3 text-sm font-semibold text-emerald-800 no-underline transition hover:bg-emerald-50">
                    Back to Home
                </a>
            </div>
        <% } else { %>
            <h2>Create Your Account</h2>
            <p class="auth-sub">Fill in your details. We will email you a verification code before creating the account.</p>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger">Alert: <%= request.getAttribute("error") %></div>
            <% } %>

            <form action="<%= request.getContextPath() %>/register" method="POST">
                <div class="form-group">
                    <label class="form-label">Full Name (as per ID)</label>
                    <input type="text" name="fullName" class="form-control" required placeholder="Ex: Rajesh Kumar Hamal">
                </div>
                <div class="grid-2">
                    <div class="form-group">
                        <label class="form-label">Email Address</label>
                        <input type="email" name="email" class="form-control" required
                               autocomplete="email" inputmode="email"
                               pattern="[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,63}"
                               title="Use a real email address with a mail-receiving domain."
                               placeholder="you@example.com">
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
                    Send Verification Code &rarr;
                </button>
            </form>

            <div class="divider"></div>
            <p style="text-align:center;color:var(--text-muted);font-size:0.9rem;">
                Already have an account?
                <a href="<%= request.getContextPath() %>/login" style="color:var(--primary);font-weight:600;text-decoration:none;">Sign In</a>
            </p>
        <% } %>
    </div>
</div>
</body>
</html>
