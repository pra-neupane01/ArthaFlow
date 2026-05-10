<%-- Shared sidebar for admin pages --%>
<%@ page import="com.arthaflow.model.User" %>
<% User _admin = (User) session.getAttribute("user"); String _ctx = request.getContextPath(); String _act = request.getParameter("action"); %>
<aside class="sidebar">
    <div class="sidebar-logo">
        <div class="logo-icon">A</div>
        <div>
            <div class="logo-text">ArthaFlow</div>
            <div class="logo-sub">Admin Panel</div>
        </div>
    </div>
    <nav class="sidebar-nav">
        <div class="nav-section-label">Admin Menu</div>
        <a href="<%= _ctx %>/admin/dashboard" class="<%= (_act == null) ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/></svg>
            Overview
        </a>
        <a href="<%= _ctx %>/admin/dashboard?action=users" class="<%= "users".equals(_act) ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            User & KYC Management
        </a>
        <a href="<%= _ctx %>/admin/dashboard?action=creditCards" class="<%= "creditCards".equals(_act) ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="1" y="4" width="22" height="16" rx="2"/><line x1="1" y1="10" x2="23" y2="10"/></svg>
            Credit Card Apps
        </a>
        <a href="<%= _ctx %>/admin/dashboard?action=transactions" class="<%= "transactions".equals(_act) ? "active" : "" %>">
            <svg class="nav-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg>
            Transaction Logs
        </a>
    </nav>
    <div class="sidebar-footer">
        <div style="display:flex;align-items:center;gap:0.6rem;padding:0.75rem;background:rgba(255,255,255,0.05);border-radius:8px;margin-bottom:0.75rem;">
            <div class="navbar-avatar" style="width:32px;height:32px;font-size:0.8rem;flex-shrink:0;background:var(--danger);">A</div>
            <div><div style="color:white;font-size:0.82rem;font-weight:600;"><%= _admin != null ? _admin.getFullName() : "Admin" %></div><div style="color:rgba(168,213,190,0.6);font-size:0.7rem;">ADMINISTRATOR</div></div>
        </div>
        <a href="<%= _ctx %>/logout" class="btn btn-danger btn-sm btn-full">Logout</a>
    </div>
</aside>
