<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify Email | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
    <script>
        window.tailwind = window.tailwind || {};
        window.tailwind.config = { corePlugins: { preflight: false } };
    </script>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
<div class="auth-page">
    <div class="auth-left">
        <a href="<%= request.getContextPath() %>/" style="text-decoration:none;display:flex;align-items:center;gap:0.75rem;margin-bottom:3rem;">
            <div style="width:42px;height:42px;background:rgba(255,255,255,0.2);border-radius:10px;display:flex;align-items:center;justify-content:center;color:white;font-weight:800;font-size:1.2rem;">A</div>
            <span style="color:white;font-weight:800;font-size:1.2rem;">ArthaFlow</span>
        </a>
        <h2 style="font-size:2rem;font-weight:800;margin-bottom:1rem;">Verify your email</h2>
        <p style="opacity:0.8;line-height:1.7;margin-bottom:2rem;">We sent a one-time code to confirm that this inbox belongs to you before opening your bank account.</p>
        <div style="display:flex;flex-direction:column;gap:0.75rem;">
            <div style="display:flex;align-items:center;gap:0.75rem;"><span aria-hidden="true">&#10003;</span><span style="opacity:0.9;">Code expires in 10 minutes</span></div>
            <div style="display:flex;align-items:center;gap:0.75rem;"><span aria-hidden="true">&#10003;</span><span style="opacity:0.9;">Account created only after verification</span></div>
        </div>
    </div>

    <div class="auth-right" style="width:540px;">
        <div class="w-full rounded-lg border border-emerald-200 bg-white p-8 shadow-lg shadow-emerald-900/10">
            <div class="mb-6">
                <p class="mb-2 text-sm font-semibold uppercase text-emerald-700">Email verification</p>
                <h2 class="mb-2 text-2xl font-extrabold text-slate-900">Enter your 6-digit code</h2>
                <p class="text-sm leading-6 text-slate-600">
                    Code sent to <span class="font-semibold text-slate-900"><%= request.getAttribute("email") %></span>.
                </p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm font-medium text-red-700">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            <% if (request.getAttribute("success") != null) { %>
                <div class="mb-4 rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm font-medium text-emerald-800">
                    <%= request.getAttribute("success") %>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/verify-email" method="POST">
                <label class="mb-2 block text-sm font-semibold text-slate-800" for="otp">Verification Code</label>
                <input id="otp" name="otp" type="text" inputmode="numeric" autocomplete="one-time-code"
                       pattern="[0-9]{6}" maxlength="6" required
                       class="mb-5 w-full rounded-lg border border-emerald-200 px-4 py-3 text-center text-2xl font-bold tracking-widest text-slate-900 outline-none transition focus:border-emerald-700 focus:ring-4 focus:ring-emerald-100"
                       placeholder="000000">
                <button type="submit" class="inline-flex w-full items-center justify-center rounded-lg bg-emerald-700 px-5 py-3 text-sm font-semibold text-white transition hover:bg-emerald-800">
                    Verify and Create Account
                </button>
            </form>

            <form action="<%= request.getContextPath() %>/verify-email" method="POST" class="mt-3">
                <input type="hidden" name="action" value="resend">
                <button type="submit" class="inline-flex w-full items-center justify-center rounded-lg border border-emerald-200 px-5 py-3 text-sm font-semibold text-emerald-800 transition hover:bg-emerald-50">
                    Resend Code
                </button>
            </form>

            <p class="mt-5 text-center text-sm text-slate-500">
                Wrong email?
                <a href="<%= request.getContextPath() %>/register?restart=1" class="font-semibold text-emerald-800 no-underline">Start again</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>
