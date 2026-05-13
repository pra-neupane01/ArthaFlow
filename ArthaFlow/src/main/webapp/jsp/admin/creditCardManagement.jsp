<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.CreditCard" %>
<%@ page import="com.arthaflow.model.KycDetails" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User admin = (User) session.getAttribute("user");
    if (admin == null || !"ADMIN".equals(admin.getRole())) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    List<CreditCard> requests = (List<CreditCard>) request.getAttribute("cardRequests");
    @SuppressWarnings("unchecked")
    Map<Integer, KycDetails> kycByCardId = (Map<Integer, KycDetails>) request.getAttribute("kycByCardId");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Credit Card Applications | ArthaFlow Admin</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="adminSidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div><h2 style="font-size:1rem;font-weight:700;">Credit Card Applications</h2><p style="font-size:0.78rem;color:var(--text-muted);">Verify KYC, then issue the card</p></div>
            <div class="navbar-user">
                <div class="navbar-avatar" style="background:var(--danger);">A</div>
                <div><div class="navbar-name"><%= admin.getFullName() %></div><div class="navbar-role">Administrator</div></div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-header"><h1>Credit card &amp; KYC</h1><p>Step 1: Verify KYC. Step 2: Issue card (bank generates card number). User sets PIN after activation.</p></div>

            <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>
            <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger">⚠ <%= request.getParameter("error") %></div>
            <% } %>

            <div class="card">
                <div class="table-wrapper" style="overflow-x:auto;">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th><th>User</th><th>Type</th><th>KYC</th><th>Card</th><th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% if (requests != null && !requests.isEmpty()) {
                               for (CreditCard card : requests) {
                                   KycDetails k = kycByCardId != null ? kycByCardId.get(card.getCardId()) : null;
                        %>
                        <tr>
                            <td class="text-muted">#<%= card.getCardId() %></td>
                            <td>User #<%= card.getUserId() %></td>
                            <td>
                                <span style="font-weight:600;"><%= card.getCardType() %></span>
                                <div class="text-muted" style="font-size:0.78rem;">
                                    Limit: Rs. <%= "PLATINUM".equals(card.getCardType()) ? "1,00,000" : "50,000" %>
                                </div>
                            </td>
                            <td style="max-width:280px;font-size:0.8rem;">
                                <% if (k == null) { %>
                                    <span class="text-muted">No KYC row (legacy). Reject and ask user to re-apply.</span>
                                <% } else { %>
                                    <div><strong>Status:</strong> <%= k.getStatus() %></div>
                                    <div class="text-muted">Citizenship: <%= k.getCitizenshipNumber() %> · DOB: <%= k.getDateOfBirth() %></div>
                                    <div class="text-muted">Occupation: <%= k.getOccupation() != null ? k.getOccupation() : "—" %> · Annual: Rs. <%= String.format("%,.0f", k.getAnnualIncome() > 0 ? k.getAnnualIncome() : k.getMinimumIncome()) %></div>
                                    <div class="text-muted">Parents: <%= k.getFatherName() != null ? k.getFatherName() : "—" %> / <%= k.getMotherName() != null ? k.getMotherName() : "—" %></div>
                                    <div style="margin-top:4px;font-size:0.78rem;"><strong>Permanent:</strong> <%= k.getPermanentAddress() != null && k.getPermanentAddress().length() > 60 ? k.getPermanentAddress().substring(0, 60) + "…" : (k.getPermanentAddress() != null ? k.getPermanentAddress() : "—") %></div>
                                    <div style="font-size:0.78rem;"><strong>Employment:</strong> <%= k.getEmploymentDetails() != null && k.getEmploymentDetails().length() > 70 ? k.getEmploymentDetails().substring(0, 70) + "…" : (k.getEmploymentDetails() != null ? k.getEmploymentDetails() : "—") %></div>
                                    <% String pi = k.getPersonalInformation(); if (pi != null && !pi.isEmpty()) { %>
                                    <div style="font-size:0.78rem;"><strong>Notes:</strong> <%= pi.length() > 60 ? pi.substring(0, 60) + "…" : pi %></div>
                                    <% } %>
                                <% } %>
                            </td>
                            <td class="text-muted" style="font-size:0.82rem;">
                                <%= card.getCreatedDate() %><br>
                                <span class="badge <%= "ACTIVE".equals(card.getStatus()) ? "badge-success" : "PENDING".equals(card.getStatus()) ? "badge-pending" : "badge-danger" %>"><%= card.getStatus() %></span>
                                <% if (card.getCardNumber() != null && !card.getCardNumber().isEmpty()) { %>
                                    <div style="margin-top:4px;">Num: <%= card.getCardNumber() %></div>
                                <% } %>
                            </td>
                            <td style="min-width:200px;">
                                <% if ("PENDING".equals(card.getStatus())) { %>
                                    <% if (k != null && "PENDING".equals(k.getStatus())) { %>
                                    <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="margin-bottom:6px;">
                                        <input type="hidden" name="action" value="verifyCardKyc">
                                        <input type="hidden" name="cardId" value="<%= card.getCardId() %>">
                                        <button type="submit" class="btn btn-outline btn-sm">1 · Verify KYC</button>
                                    </form>
                                    <% } %>
                                    <% if (k != null && "APPROVED".equals(k.getStatus())) { %>
                                    <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST" style="margin-bottom:6px;">
                                        <input type="hidden" name="action" value="issueCard">
                                        <input type="hidden" name="cardId" value="<%= card.getCardId() %>">
                                        <button type="submit" class="btn btn-primary btn-sm">2 · Issue card</button>
                                    </form>
                                    <% } %>
                                    <form action="<%= request.getContextPath() %>/admin/dashboard" method="POST">
                                        <input type="hidden" name="action" value="rejectCard">
                                        <input type="hidden" name="cardId" value="<%= card.getCardId() %>">
                                        <button type="submit" class="btn btn-danger btn-sm">Reject</button>
                                    </form>
                                <% } else { %>
                                    <span class="text-muted" style="font-size:0.82rem;">—</span>
                                <% } %>
                            </td>
                        </tr>
                        <% } } else { %>
                        <tr><td colspan="6" style="text-align:center;padding:3rem;color:var(--text-muted);">No credit card applications.</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
