<%-- Shared sidebar for user pages --%>
<%@ page import="com.arthaflow.model.User" %>
<%
    User _u = (User) session.getAttribute("user");
    String _uri = request.getRequestURI();
    String _ctx = request.getContextPath();
    String _pp = (_u != null && _u.getProfilePicture() != null && !_u.getProfilePicture().isEmpty())
            ? _ctx + "/" + _u.getProfilePicture() : null;
%>
<aside class="sidebar">
    <div class="sidebar-logo">
        <div class="logo-icon">A</div>
        <div>
            <div class="logo-text">ArthaFlow</div>
            <div class="logo-sub">Digital Banking</div>
        </div>
    </div>

    <nav class="sidebar-nav">
        <div class="nav-section-label">Main Menu</div>
        <a href="<%= _ctx %>/user/dashboard" class="<%= _uri.contains("dashboard") ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
            Dashboard
        </a>
        <a href="<%= _ctx %>/user/account" class="<%= _uri.contains("/user/account") ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9,22 9,12 15,12 15,22"/></svg>
            My Account & KYC
        </a>
        <a href="<%= _ctx %>/user/transaction?action=history" class="<%= _uri.contains("transaction") ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg>
            Transactions
        </a>
        <a href="<%= _ctx %>/user/creditCard" class="<%= _uri.contains("creditCard") ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
            Credit Card
        </a>

        <div class="nav-section-label">Account</div>
        <a href="<%= _ctx %>/user/profile" class="<%= _uri.contains("profile") ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            My Profile
        </a>
    </nav>

    <div class="sidebar-footer">
        <% if (_u != null) { %>
        <div style="display:flex;align-items:center;gap:0.6rem;padding:0.75rem;background:rgba(255,255,255,0.05);border-radius:8px;margin-bottom:0.75rem;">
            <div class="navbar-avatar" style="width:32px;height:32px;font-size:0.8rem;flex-shrink:0;">
                <% if (_pp != null) { %><img src="<%= _pp %>" alt=""><% } else { %><%= _u.getInitials() %><% } %>
            </div>
            <div style="overflow:hidden;">
                <div style="color:white;font-size:0.82rem;font-weight:600;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;"><%= _u.getFullName() %></div>
                <div style="color:rgba(168,213,190,0.6);font-size:0.7rem;">USER</div>
            </div>
        </div>
        <% } %>
        <a href="<%= _ctx %>/logout" class="btn btn-danger btn-sm btn-full">Logout</a>
    </div>
</aside>
