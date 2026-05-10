<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ArthaFlow | Nepal's Trusted Digital Bank</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body class="landing-body">

<!-- NAVBAR -->
<nav class="landing-nav">
    <a href="<%= request.getContextPath() %>/" class="landing-logo">
        <div class="logo-mark">A</div>
        <span class="logo-name">ArthaFlow</span>
    </a>
    <div class="landing-nav-links">
        <a href="#features">Services</a>
        <a href="#trust">About</a>
        <a href="#contact">Contact</a>
    </div>
    <div class="landing-nav-actions">
        <a href="<%= request.getContextPath() %>/login" class="btn btn-ghost btn-sm">Login</a>
        <a href="<%= request.getContextPath() %>/register" class="btn btn-primary btn-sm">Open Account</a>
    </div>
</nav>

<!-- HERO -->
<section class="hero-section">
    <div class="hero-content">
        <div class="hero-badge">🏦 Nepal's Trusted Digital Bank</div>
        <h1>Banking that flows<br>with <span>Nepal's future</span></h1>
        <p>Secure, fast, and modern banking for all Nepali citizens. Tailored agricultural plans for farmers, advanced tools for business owners, and trusted by thousands across the nation.</p>
        <div class="hero-actions">
            <a href="<%= request.getContextPath() %>/register" class="btn btn-primary btn-lg">Open Free Account</a>
            <a href="<%= request.getContextPath() %>/login" class="btn btn-outline btn-lg">Sign In</a>
        </div>
    </div>
    <div class="hero-visual">
        <div style="background: linear-gradient(135deg,#063e27,#0a7c4e); border-radius: 20px; padding: 2rem; width: 340px; box-shadow: 0 24px 64px rgba(6,62,39,0.25);">
            <div style="color: rgba(255,255,255,0.6); font-size: 0.75rem; margin-bottom: 1rem; text-transform: uppercase; letter-spacing: 0.1em;">ArthaFlow Account</div>
            <div style="color: white; font-size: 1.8rem; font-weight: 800; margin-bottom: 0.25rem;">Rs. 1,25,000</div>
            <div style="color: rgba(255,255,255,0.6); font-size: 0.85rem; margin-bottom: 2rem;">Available Balance</div>
            <div style="background: rgba(255,255,255,0.08); border-radius: 12px; padding: 1rem; margin-bottom: 0.75rem; display: flex; justify-content: space-between; align-items: center;">
                <div><div style="color: rgba(255,255,255,0.6); font-size: 0.7rem;">Deposit</div><div style="color: white; font-weight: 600;">+Rs. 50,000</div></div>
                <div style="background: #16a364; color: white; padding: 4px 10px; border-radius: 99px; font-size: 0.7rem; font-weight: 700;">SUCCESS</div>
            </div>
            <div style="background: rgba(255,255,255,0.08); border-radius: 12px; padding: 1rem; display: flex; justify-content: space-between; align-items: center;">
                <div><div style="color: rgba(255,255,255,0.6); font-size: 0.7rem;">Withdrawal</div><div style="color: white; font-weight: 600;">-Rs. 8,000</div></div>
                <div style="background: rgba(255,255,255,0.15); color: white; padding: 4px 10px; border-radius: 99px; font-size: 0.7rem; font-weight: 700;">SUCCESS</div>
            </div>
        </div>
    </div>
</section>

<!-- TRUST STRIP -->
<div class="trust-strip" id="trust">
    <div class="trust-item"><span class="ti-icon">✅</span> Trusted by Nepali Citizens</div>
    <div class="trust-item"><span class="ti-icon">🌾</span> Empowering Farmers & Businesses</div>
    <div class="trust-item"><span class="ti-icon">🔒</span> ACID-Compliant Transactions</div>
    <div class="trust-item"><span class="ti-icon">🏅</span> Admin-Verified Accounts</div>
</div>

<!-- FEATURES -->
<section class="features-section" id="features">
    <div class="section-header">
        <h2>Everything you need in one bank</h2>
        <p>ArthaFlow brings world-class banking to the people of Nepal.</p>
    </div>
    <div class="features-grid">
        <div class="feature-card">
            <div class="feature-icon">💳</div>
            <h3>Smart Accounts</h3>
            <p>Savings and Current accounts with full KYC verification. Admin-issued account numbers ensure authenticity.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">⚡</div>
            <h3>Instant Transactions</h3>
            <p>Deposit and withdraw funds instantly with ACID-compliant transaction guarantees. Every rupee is safe.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">🏦</div>
            <h3>ArthaFlow Credit Card</h3>
            <p>Apply for Gold or Platinum credit cards. Admin review ensures responsible lending in NPR.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3>Full Transaction History</h3>
            <p>View, search, and filter your complete transaction history. Stay in full control of your finances.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">🛡️</div>
            <h3>KYC Security</h3>
            <p>Upload your Citizenship or Passport. Our admin team manually verifies every document before activation.</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">👤</div>
            <h3>Personal Profile</h3>
            <p>Update your profile, address, and upload your profile picture. Your identity, your control.</p>
        </div>
    </div>
</section>

<!-- FOOTER -->
<footer class="landing-footer" id="contact">
    <div class="footer-grid">
        <div class="footer-brand">
            <div class="logo-name">ArthaFlow Bank Ltd.</div>
            <p>Nepal's premier digital banking platform. Dedicated to serving farmers, business owners, and every citizen across the nation.</p>
        </div>
        <div class="footer-col">
            <h4>Services</h4>
            <a href="#">Savings Account</a>
            <a href="#">Current Account</a>
            <a href="#">Credit Cards</a>
            <a href="#">Transactions</a>
        </div>
        <div class="footer-col">
            <h4>Contact</h4>
            <a href="#">📍 Sundar Haraicha 04, Dulari, Itahari, Sunsari, Nepal</a>
            <a href="#">📞 +977-025-XXXXXX</a>
            <a href="#">✉ info@arthaflow.com.np</a>
        </div>
    </div>
    <div class="footer-bottom">
        <span>© 2026 ArthaFlow Bank Ltd. All Rights Reserved.</span>
        <span>Regulated for the people of Nepal 🇳🇵</span>
    </div>
</footer>

</body>
</html>