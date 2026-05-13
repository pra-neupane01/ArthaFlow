<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Account" %>
<%@ page import="com.arthaflow.model.CreditCard" %>
<%@ page import="com.arthaflow.model.KycDetails" %>
<%@ page import="com.arthaflow.model.CreditCardTransaction" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    CreditCard card = (CreditCard) request.getAttribute("creditCard");
    KycDetails cardKyc = (KycDetails) request.getAttribute("cardKyc");
    Account bankAccount = (Account) request.getAttribute("bankAccount");
    @SuppressWarnings("unchecked")
    List<CreditCardTransaction> cardTx = (List<CreditCardTransaction>) request.getAttribute("cardTransactions");
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;

    String displayNumber = "•••• •••• •••• ••••";
    if (card != null && card.getCardNumber() != null && !card.getCardNumber().isEmpty()) {
        String n = card.getCardNumber().replaceAll("\\s+", "");
        if (n.length() >= 4) {
            displayNumber = "•••• •••• •••• " + n.substring(n.length() - 4);
        }
    }
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
            <div class="page-header"><h1>Credit Card Management</h1><p>Apply with KYC, set your PIN when approved, then record card purchases.</p></div>

            <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>
            <% if (request.getParameter("error") != null) { %>
            <div class="alert alert-danger">⚠ <%= request.getParameter("error") %></div>
            <% } %>

            <div style="display:flex;gap:2rem;flex-wrap:wrap;align-items:flex-start;">

                <div>
                    <% if (card != null && "ACTIVE".equals(card.getStatus())) { %>
                    <div class="cc-visual">
                        <div class="cc-logo">ArthaFlow</div>
                        <div class="cc-chip"></div>
                        <div class="cc-number"><%= displayNumber %></div>
                        <div class="cc-details">
                            <div><label>Card Holder</label><span><%= user.getFullName().toUpperCase() %></span></div>
                            <div><label>Expires</label><span><%= card.getExpiryDate() != null ? card.getExpiryDate() : "—" %></span></div>
                            <div><label>Type</label><span><%= card.getCardType() %></span></div>
                        </div>
                    </div>
                    <% } else { %>
                    <div class="cc-visual demo">
                        <div class="cc-logo" style="opacity:0.5;">ArthaFlow</div>
                        <div class="cc-chip"></div>
                        <div class="cc-number">•••• •••• •••• ••••</div>
                        <div class="cc-details">
                            <div><label>Card Holder</label><span><%= user.getFullName().toUpperCase() %></span></div>
                            <div><label>Expires</label><span>MM/YY</span></div>
                            <div><label>Type</label><span><%= card == null ? "—" : card.getStatus() %></span></div>
                        </div>
                    </div>
                    <div style="text-align:center;margin-top:0.75rem;font-size:0.78rem;color:var(--text-muted);">
                        <%= card == null ? "No card yet — apply below" : ("PENDING".equals(card.getStatus()) ? "Application & KYC under review" : "Application not active") %>
                    </div>
                    <% } %>
                </div>

                <div style="flex:1;min-width:280px;">
                    <% if (card == null || "REJECTED".equals(card.getStatus())) {
                        boolean canApplyCard = bankAccount != null && "ACTIVE".equals(bankAccount.getStatus());
                    %>
                    <div class="card">
                        <div class="card-header"><h3><%= card == null ? "Credit card application" : "Re-apply for credit card" %></h3></div>
                        <% if (!canApplyCard) { %>
                        <p class="text-muted mb-3">Credit card applications use the identity and address details from your <strong>bank account opening</strong> application. Once your account is <strong>ACTIVE</strong>, you can apply here with income and card-related information only.</p>
                        <div class="alert alert-warning" style="margin-bottom:0;">
                            <% if (bankAccount == null) { %>
                            You do not have a bank account application yet. Open an account and complete KYC first.
                            <% } else if (!"ACTIVE".equals(bankAccount.getStatus())) { %>
                            Your bank account is not active yet (status: <strong><%= bankAccount.getStatus() %></strong>). Please wait for admin approval or check Account &amp; KYC for updates.
                            <% } %>
                            <div style="margin-top:0.75rem;">
                                <a href="<%= request.getContextPath() %>/user/account" class="btn btn-primary btn-sm">Go to Account &amp; KYC</a>
                            </div>
                        </div>
                        <% } else { %>
                        <p class="text-muted mb-3">Your identity is on file from your approved account. Complete the sections below for income, employment, and card preferences. An admin will review before your card is issued.</p>
                        <form action="<%= request.getContextPath() %>/user/creditCard" method="POST">
                            <input type="hidden" name="action" value="apply">

                            <h4 style="font-size:0.95rem;margin:1rem 0 0.5rem;color:var(--text);">Card type</h4>
                            <div class="form-group">
                                <label class="form-label">Card type</label>
                                <select name="cardType" class="form-control" required>
                                    <option value="GOLD">Gold — Limit: Rs. 50,000</option>
                                    <option value="PLATINUM">Platinum — Limit: Rs. 1,00,000</option>
                                </select>
                            </div>

                            <h4 style="font-size:0.95rem;margin:1.25rem 0 0.5rem;color:var(--text);">Occupation &amp; income</h4>
                            <div class="form-group">
                                <label class="form-label">Occupation (for this application)</label>
                                <input type="text" name="occupation" class="form-control" required maxlength="200" placeholder="e.g. Teacher, Engineer, Business owner">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Annual income (NPR)</label>
                                <input type="number" name="annualIncome" class="form-control" required min="0" step="1" placeholder="Total annual gross">
                            </div>

                            <h4 style="font-size:0.95rem;margin:1.25rem 0 0.5rem;color:var(--text);">Employment details</h4>
                            <div class="form-group">
                                <label class="form-label">Employment details</label>
                                <textarea name="employmentDetails" class="form-control" rows="3" required placeholder="Employer name, job title, years of service, business address"></textarea>
                            </div>

                            <h4 style="font-size:0.95rem;margin:1.25rem 0 0.5rem;color:var(--text);">Card preferences</h4>
                            <div class="form-group">
                                <label class="form-label">Card preferences</label>
                                <textarea name="cardPreferences" class="form-control" rows="2" required placeholder="E-statement vs paper, billing cycle preference, add-on cards, etc."></textarea>
                            </div>

                            <h4 style="font-size:0.95rem;margin:1.25rem 0 0.5rem;color:var(--text);">Credit-related information</h4>
                            <div class="form-group">
                                <label class="form-label">Existing loans / cards / monthly obligations</label>
                                <textarea name="creditInformation" class="form-control" rows="3" required placeholder="List existing credit cards, loans, or write None"></textarea>
                            </div>

                            <h4 style="font-size:0.95rem;margin:1.25rem 0 0.5rem;color:var(--text);">Terms and conditions</h4>
                            <div class="form-group" style="display:flex;align-items:flex-start;gap:0.5rem;">
                                <input type="checkbox" name="termsAccepted" id="termsAccepted" value="on" required style="margin-top:0.25rem;">
                                <label for="termsAccepted" style="font-size:0.85rem;line-height:1.4;">I confirm that the information provided is true and I agree to the bank's credit card terms, fees, and use of my data for verification.</label>
                            </div>

                            <button type="submit" class="btn btn-primary btn-full" style="margin-top:1rem;">Submit application</button>
                        </form>
                        <% } %>
                    </div>

                    <% } else if ("PENDING".equals(card.getStatus())) { %>
                    <div class="card">
                        <div class="alert alert-warning" style="margin-bottom:1rem;">
                            Your <strong><%= card.getCardType() %></strong> application is under admin review.
                        </div>
                        <% if (cardKyc != null) { %>
                        <p style="font-size:0.85rem;color:var(--text-muted);">KYC status: <strong><%= cardKyc.getStatus() %></strong></p>
                        <% } %>
                    </div>

                    <% } else if ("ACTIVE".equals(card.getStatus())) { %>
                    <div class="stat-grid" style="grid-template-columns:1fr;">
                        <div class="stat-card">
                            <span class="stat-label">Credit limit</span>
                            <div class="stat-value green">Rs. <%= String.format("%,.0f", card.getCreditLimit()) %></div>
                        </div>
                        <div class="stat-card">
                            <span class="stat-label">Available credit</span>
                            <div class="stat-value">Rs. <%= String.format("%,.0f", card.getCreditLimit() - card.getCurrentBalance()) %></div>
                        </div>
                        <div class="stat-card danger">
                            <span class="stat-label">Amount used</span>
                            <div class="stat-value red">Rs. <%= String.format("%,.0f", card.getCurrentBalance()) %></div>
                        </div>
                    </div>

                    <% if (!card.isPinSet()) { %>
                    <div class="card" style="margin-top:1rem;">
                        <div class="card-header"><h3>Set your card PIN</h3></div>
                        <p class="text-muted mb-3">Choose a 4–6 digit PIN. You will need it for card purchases and payments.</p>
                        <form action="<%= request.getContextPath() %>/user/creditCard" method="POST">
                            <input type="hidden" name="action" value="setPin">
                            <input type="hidden" name="cardId" value="<%= card.getCardId() %>">
                            <div class="form-group">
                                <label class="form-label">PIN</label>
                                <input type="password" name="pin" class="form-control" required pattern="[0-9]{4,6}" maxlength="6" inputmode="numeric" autocomplete="new-password">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Confirm PIN</label>
                                <input type="password" name="pinConfirm" class="form-control" required pattern="[0-9]{4,6}" maxlength="6" inputmode="numeric" autocomplete="new-password">
                            </div>
                            <button type="submit" class="btn btn-primary btn-full">Save PIN</button>
                        </form>
                    </div>
                    <% } else { %>
                    <div class="card" style="margin-top:1rem;">
                        <div class="card-header"><h3>Card purchase</h3></div>
                        <p class="text-muted mb-3" style="font-size:0.85rem;">Charges your card (credit used). Cash withdrawal is not available on the card.</p>
                        <form action="<%= request.getContextPath() %>/user/creditCard" method="POST">
                            <input type="hidden" name="action" value="purchase">
                            <div class="form-group">
                                <label class="form-label">Amount (NPR)</label>
                                <input type="number" name="amount" class="form-control" required min="0.01" step="0.01">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Remarks</label>
                                <input type="text" name="remarks" class="form-control" placeholder="Merchant / note">
                            </div>
                            <div class="form-group">
                                <label class="form-label">PIN</label>
                                <input type="password" name="pin" class="form-control" required maxlength="6" inputmode="numeric">
                            </div>
                            <button type="submit" class="btn btn-primary btn-full">Record purchase</button>
                        </form>
                    </div>
                    <% } %>

                    <div class="card" id="card-activity" style="margin-top:1rem;">
                        <div class="card-header"><h3>Card transactions</h3></div>
                        <p class="text-muted mb-3" style="font-size:0.82rem;">Purchases increase your card balance (credit used) up to your limit. Set your PIN above to record purchases.</p>
                        <div class="table-wrapper">
                            <table class="data-table">
                                <thead><tr><th>Type</th><th>Amount (Rs.)</th><th>Balance after</th><th>Date</th><th>Remarks</th></tr></thead>
                                <tbody>
                                <% if (cardTx != null && !cardTx.isEmpty()) {
                                       for (CreditCardTransaction t : cardTx) { %>
                                <tr>
                                    <td><span class="badge badge-danger"><%= t.getType() %></span></td>
                                    <td class="fw-bold">Rs. <%= String.format("%,.2f", t.getAmount()) %></td>
                                    <td>Rs. <%= String.format("%,.2f", t.getBalanceAfter()) %></td>
                                    <td class="text-muted" style="font-size:0.82rem;"><%= t.getTransactionDate() %></td>
                                    <td class="text-muted"><%= t.getRemarks() != null ? t.getRemarks() : "—" %></td>
                                </tr>
                                <% } } else { %>
                                <tr><td colspan="5" class="text-muted" style="text-align:center;padding:1.5rem;">No card transactions yet. After you set your PIN, use Card purchase above.</td></tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
