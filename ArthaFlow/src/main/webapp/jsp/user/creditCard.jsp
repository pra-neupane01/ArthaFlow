<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.CreditCard" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    CreditCard card = (CreditCard) request.getAttribute("creditCard");
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Credit Card | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">Credit Card</h2><p style="font-size:0.78rem;color:var(--text-muted);">Manage your ArthaFlow credit card</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar"><% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %></div>
                <div><div class="navbar-name"><%= user.getFullName() %></div><div class="navbar-role"><%= user.getRole() %></div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>Credit Card Management</h1><p>Apply for or manage your ArthaFlow credit card.</p></div>

            <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>

            <div style="display:flex;gap:2rem;flex-wrap:wrap;align-items:flex-start;">

                <!-- CARD VISUAL -->
                <div>
                    <% if (card != null && "ACTIVE".equals(card.getStatus())) { %>
                    <%-- REAL ACTIVE CARD --%>
                    <div class="cc-visual">
                        <div class="cc-logo">ArthaFlow</div>
                        <div class="cc-chip"></div>
                        <div class="cc-number"><%= card.getCardNumber() != null ? card.getCardNumber() : "**** **** **** ****" %></div>
                        <div class="cc-details">
                            <div><label>Card Holder</label><span><%= user.getFullName().toUpperCase() %></span></div>
                            <div><label>Expires</label><span><%= card.getExpiryDate() %></span></div>
                            <div><label>Type</label><span><%= card.getCardType() %></span></div>
                        </div>
                    </div>
                    <% } else { %>
                    <%-- DEMO / PLACEHOLDER CARD --%>
                    <div class="cc-visual demo">
                        <div class="cc-logo" style="opacity:0.5;">ArthaFlow</div>
                        <div class="cc-chip"></div>
                        <div class="cc-number">•••• •••• •••• ••••</div>
                        <div class="cc-details">
                            <div><label>Card Holder</label><span><%= user.getFullName().toUpperCase() %></span></div>
                            <div><label>Expires</label><span>MM/YY</span></div>
                            <div><label>Type</label><span>PENDING</span></div>
                        </div>
                    </div>
                    <div style="text-align:center;margin-top:0.75rem;font-size:0.78rem;color:var(--text-muted);">
                        <%= card == null ? "No card yet — apply below" : "Application under review" %>
                    </div>
                    <% } %>
                </div>

                <!-- CARD DETAILS / ACTIONS -->
                <div style="flex:1;min-width:280px;">
                    <% if (card == null) { %>
                    <!-- APPLY FORM -->
                    <div class="card">
                        <div class="card-header"><h3>Apply for Credit Card</h3></div>
                        <p class="text-muted mb-3">Choose your card type and submit an application. Reviewed by our admin team within 1-2 business days.</p>
                        <form action="<%= request.getContextPath() %>/user/creditCard" method="POST">
                            <input type="hidden" name="action" value="apply">
                            <div class="form-group">
                                <label class="form-label">Card Type</label>
                                <select name="cardType" class="form-control">
                                    <option value="GOLD">🥇 Gold — Limit: Rs. 50,000</option>
                                    <option value="PLATINUM">💎 Platinum — Limit: Rs. 1,00,000</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary btn-full">Submit Application →</button>
                        </form>
                    </div>

                    <% } else if ("PENDING".equals(card.getStatus())) { %>
                    <!-- PENDING -->
                    <div class="card">
                        <div class="alert alert-warning" style="margin-bottom:0;">
                            ⏳ Your <strong><%= card.getCardType() %></strong> card application is under review. Our team will process it shortly.
                        </div>
                    </div>

                    <% } else if ("ACTIVE".equals(card.getStatus())) { %>
                    <!-- STATS -->
                    <div class="stat-grid" style="grid-template-columns:1fr;">
                        <div class="stat-card">
                            <span class="stat-label">Credit Limit</span>
                            <div class="stat-value green">Rs. <%= String.format("%,.0f", card.getCreditLimit()) %></div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">Available Credit</span>
                            <div class="stat-value">Rs. <%= String.format("%,.0f", card.getCreditLimit() - card.getCurrentBalance()) %></div>
                        </div>
                        <div class="stat-card danger">
                            <span class="stat-label">Current Used</span>
                            <div class="stat-value red">Rs. <%= String.format("%,.0f", card.getCurrentBalance()) %></div>
                        </div>
                    </div>

                    <% } else { %>
                    <!-- REJECTED -->
                    <div class="card">
                        <div class="alert alert-danger" style="margin-bottom:1rem;">❌ Your card application was rejected.</div>
                        <form action="<%= request.getContextPath() %>/user/creditCard" method="POST">
                            <input type="hidden" name="action" value="apply">
                            <div class="form-group">
                                <label class="form-label">Re-apply with card type</label>
                                <select name="cardType" class="form-control">
                                    <option value="GOLD">🥇 Gold — Limit: Rs. 50,000</option>
                                    <option value="PLATINUM">💎 Platinum — Limit: Rs. 1,00,000</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary btn-full">Re-apply →</button>
                        </form>
                    </div>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
