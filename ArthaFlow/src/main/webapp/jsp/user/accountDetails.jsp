<%@ page import="com.arthaflow.model.User" %>
<%@ page import="com.arthaflow.model.Account" %>
<%@ page import="com.arthaflow.model.KycDetails" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private String h(Object value) {
        if (value == null) return "";
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
%>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) { response.sendRedirect(request.getContextPath() + "/login"); return; }
    Account account = (Account) request.getAttribute("account");
    KycDetails accountKyc = (KycDetails) request.getAttribute("accountKyc");
    boolean canApply = account == null || "REJECTED".equals(account.getStatus()) || "CLOSED".equals(account.getStatus());
    String picUrl = (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        ? request.getContextPath() + "/" + user.getProfilePicture() : null;
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Account & KYC | ArthaFlow</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/premium.css">
</head>
<body>
<div class="page-wrapper">
    <jsp:include page="sidebar.jsp" />
    <div class="main-content">
        <div class="top-navbar">
            <div>
                <h2 style="font-size:1rem;font-weight:700;">Account & KYC</h2>
                <p style="font-size:0.78rem;color:var(--text-muted);">Identity verification and account management</p>
            </div>
            <div class="navbar-user">
                <div class="navbar-avatar">
                    <% if (picUrl != null) { %><img src="<%= picUrl %>" alt="avatar"><% } else { %><%= user.getInitials() %><% } %>
                </div>
                <div><div class="navbar-name"><%= user.getFullName() %></div><div class="navbar-role"><%= user.getRole() %></div></div>
            </div>
        </div>

        <div class="page-content">
            <div class="page-header">
                <h1>Account & KYC Verification</h1>
                <p>Complete your identity verification to activate full banking features.</p>
            </div>

            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger">⚠ <%= request.getAttribute("error") %></div>
            <% } %>
            <% if (request.getParameter("success") != null) { %>
                <div class="alert alert-success">✓ <%= request.getParameter("success") %></div>
            <% } %>

            <% if (canApply) { %>
            <!-- NO ACCOUNT — KYC FORM -->
            <% if (account != null && "REJECTED".equals(account.getStatus())) { %>
            <div class="alert alert-danger" style="max-width:720px;">
                Your previous account opening application was rejected.
                <% if (accountKyc != null && accountKyc.getRejectionRemarks() != null && !accountKyc.getRejectionRemarks().isBlank()) { %>
                    <div style="margin-top:0.4rem;"><strong>Admin remark:</strong> <%= h(accountKyc.getRejectionRemarks()) %></div>
                <% } %>
            </div>
            <% } %>
            <div class="card" style="max-width: 720px;">
                <div class="card-header"><h3><%= account == null ? "Account opening &amp; KYC" : "Reapply for account opening" %></h3></div>
                <p class="text-muted mb-3">Submit your details and upload identity documents. Our admin team will verify and issue your account number within 1–2 business days.</p>
                <form action="<%= request.getContextPath() %>/user/account" method="POST" enctype="multipart/form-data">
                    <h4 style="font-size:0.95rem;margin:0.5rem 0;color:var(--text);">Account</h4>
                    <div class="form-group">
                        <label class="form-label">Account type</label>
                        <select name="accountType" class="form-control" required>
                            <option value="SAVINGS">Savings Account</option>
                            <option value="CURRENT">Current Account</option>
                        </select>
                    </div>

                    <h4 style="font-size:0.95rem;margin:1rem 0 0.5rem;color:var(--text);">Personal &amp; family</h4>
                    <div class="grid-2" style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
                        <div class="form-group">
                            <label class="form-label">Citizenship number</label>
                            <input type="text" name="citizenshipNumber" class="form-control" required maxlength="50" placeholder="As on citizenship / national ID">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Date of birth</label>
                            <input type="date" name="dateOfBirth" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Gender</label>
                        <select name="gender" class="form-control" required>
                            <option value="">Select</option>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                            <option value="OTHER">Other</option>
                            <option value="PREFER_NOT_SAY">Prefer not to say</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Occupation</label>
                        <input type="text" name="occupation" class="form-control" required maxlength="200" placeholder="e.g. Student, Teacher, Business">
                    </div>
                    <div class="grid-2" style="display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
                        <div class="form-group">
                            <label class="form-label">Father's name</label>
                            <input type="text" name="fatherName" class="form-control" required maxlength="200">
                        </div>
                        <div class="form-group">
                            <label class="form-label">Mother's name</label>
                            <input type="text" name="motherName" class="form-control" required maxlength="200">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Family details</label>
                        <textarea name="familyDetails" class="form-control" rows="2" required placeholder="Spouse, dependents, household, emergency contact"></textarea>
                    </div>

                    <h4 style="font-size:0.95rem;margin:1rem 0 0.5rem;color:var(--text);">Address</h4>
                    <div class="form-group">
                        <label class="form-label">Permanent address</label>
                        <textarea name="permanentAddress" class="form-control" rows="2" required maxlength="500" placeholder="Ward, municipality, district"></textarea>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Mailing or temporary address</label>
                        <textarea name="mailingAddress" class="form-control" rows="2" required maxlength="500" placeholder="If same as permanent, write Same as permanent"></textarea>
                    </div>

                    <h4 style="font-size:0.95rem;margin:1rem 0 0.5rem;color:var(--text);">Documents</h4>
                    <div class="form-group">
                        <label class="form-label">Identity document (citizenship / passport)</label>
                        <input type="file" name="idDocument" accept="image/*,.pdf" required class="form-control">
                        <span class="text-muted" style="font-size:0.8rem;">Accepted: JPG, PNG, PDF (max 2MB)</span>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Proof of address (utility bill / letter)</label>
                        <input type="file" name="addressProof" accept="image/*,.pdf" required class="form-control">
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="margin-top:0.5rem;">Submit account opening application</button>
                </form>
            </div>

            <% } else if ("PENDING".equals(account.getStatus())) { %>
            <!-- PENDING STATE -->
            <div class="card" style="max-width:720px;">
                <div style="font-size:4rem;margin-bottom:1rem;text-align:center;">⏳</div>
                <h2 style="color:var(--warning);text-align:center;">Application under review</h2>
                <p class="text-muted" style="margin:1rem 0 1.5rem;text-align:center;">Our administrators are verifying your KYC documents. You will receive your account number once approved.</p>
                <div style="background:var(--bg);border-radius:var(--radius-sm);padding:1.25rem;display:grid;grid-template-columns:1fr 1fr;gap:1rem;">
                    <div><span class="stat-label">KYC row status</span><div class="fw-bold" style="color:var(--warning);"><%= accountKyc != null && accountKyc.getStatus() != null ? accountKyc.getStatus() : "—" %></div></div>
                    <div><span class="stat-label">Account type</span><div class="fw-bold"><%= account.getAccountType() %></div></div>
                    <div><span class="stat-label">Citizenship no.</span><div class="fw-bold" style="font-size:0.9rem;"><%= accountKyc != null && accountKyc.getCitizenshipNumber() != null && !accountKyc.getCitizenshipNumber().isEmpty() ? accountKyc.getCitizenshipNumber() : "—" %></div></div>
                    <div><span class="stat-label">Date of birth</span><div class="fw-bold"><%= accountKyc != null && accountKyc.getDateOfBirth() != null ? accountKyc.getDateOfBirth().toString() : "—" %></div></div>
                    <div><span class="stat-label">Gender</span><div class="fw-bold"><%= accountKyc != null && accountKyc.getGender() != null && !accountKyc.getGender().isEmpty() ? accountKyc.getGender() : "—" %></div></div>
                    <div><span class="stat-label">Occupation</span><div class="fw-bold" style="font-size:0.9rem;"><%= accountKyc != null && accountKyc.getOccupation() != null ? accountKyc.getOccupation() : "—" %></div></div>
                </div>
                <p class="text-muted" style="font-size:0.82rem;margin-top:1rem;">Father: <%= accountKyc != null && accountKyc.getFatherName() != null ? accountKyc.getFatherName() : "—" %> · Mother: <%= accountKyc != null && accountKyc.getMotherName() != null ? accountKyc.getMotherName() : "—" %></p>
                <p class="text-muted" style="font-size:0.82rem;">Permanent: <%= accountKyc != null && accountKyc.getPermanentAddress() != null ? accountKyc.getPermanentAddress() : "—" %></p>
                <p class="text-muted" style="font-size:0.82rem;">Mailing: <%= accountKyc != null && accountKyc.getMailingAddress() != null ? accountKyc.getMailingAddress() : "—" %></p>
            </div>

            <% } else if ("ACTIVE".equals(account.getStatus())) { %>
            <!-- ACTIVE STATE -->
            <div class="stat-grid" style="max-width:700px;">
                <div class="stat-card">
                    <span class="stat-label">Account Status</span>
                    <div style="display:flex;align-items:center;gap:0.5rem;margin-top:0.4rem;">
                        <span class="badge badge-success">✓ ACTIVE</span>
                    </div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Account Number</span>
                    <div class="stat-value" style="font-size:1.25rem;">#<%= account.getAccount_number() %></div>
                </div>
                <div class="stat-card">
                    <span class="stat-label">Current Balance</span>
                    <div class="stat-value green">Rs. <%= String.format("%,.2f", account.getBalance()) %></div>
                </div>
            </div>
            <div class="card" style="max-width:700px;margin-top:1.5rem;">
                <div class="card-header"><h3>Account details</h3></div>
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:1.25rem;">
                    <div><span class="stat-label">Account type</span><div class="fw-bold mt-1"><%= account.getAccountType() %></div></div>
                    <div><span class="stat-label">KYC status</span><div class="mt-1"><span class="badge badge-success"><%= account.getKycStatus() != null ? account.getKycStatus() : "—" %></span></div></div>
                    <div><span class="stat-label">Citizenship number</span><div class="fw-bold mt-1" style="font-size:0.9rem;"><%= accountKyc != null && accountKyc.getCitizenshipNumber() != null ? accountKyc.getCitizenshipNumber() : "—" %></div></div>
                    <div><span class="stat-label">Date of birth</span><div class="fw-bold mt-1"><%= accountKyc != null && accountKyc.getDateOfBirth() != null ? accountKyc.getDateOfBirth().toString() : "—" %></div></div>
                    <div><span class="stat-label">Gender</span><div class="fw-bold mt-1"><%= accountKyc != null && accountKyc.getGender() != null ? accountKyc.getGender() : "—" %></div></div>
                    <div><span class="stat-label">Occupation</span><div class="fw-bold mt-1" style="font-size:0.9rem;"><%= accountKyc != null && accountKyc.getOccupation() != null ? accountKyc.getOccupation() : "—" %></div></div>
                    <div style="grid-column:1/-1;"><span class="stat-label">Father / mother</span><div class="fw-bold mt-1" style="font-size:0.88rem;"><%= accountKyc != null && accountKyc.getFatherName() != null ? accountKyc.getFatherName() : "—" %> / <%= accountKyc != null && accountKyc.getMotherName() != null ? accountKyc.getMotherName() : "—" %></div></div>
                    <div style="grid-column:1/-1;"><span class="stat-label">Family details</span><div class="mt-1" style="font-size:0.88rem;"><%= accountKyc != null && accountKyc.getFamilyDetails() != null ? accountKyc.getFamilyDetails() : "—" %></div></div>
                    <div style="grid-column:1/-1;"><span class="stat-label">Permanent address</span><div class="mt-1" style="font-size:0.88rem;"><%= accountKyc != null && accountKyc.getPermanentAddress() != null ? accountKyc.getPermanentAddress() : "—" %></div></div>
                    <div style="grid-column:1/-1;"><span class="stat-label">Mailing / temporary address</span><div class="mt-1" style="font-size:0.88rem;"><%= accountKyc != null && accountKyc.getMailingAddress() != null ? accountKyc.getMailingAddress() : "—" %></div></div>
                    <div><span class="stat-label">Member since</span><div class="fw-bold mt-1"><%= account.getCreatedDate() %></div></div>
                    <div><span class="stat-label">Account ID</span><div class="fw-bold mt-1">#<%= account.getAccountId() %></div></div>
                </div>
            </div>

            <% } else { %>
            <!-- REJECTED -->
            <div class="card" style="max-width:620px;text-align:center;">
                <div style="font-size:4rem;margin-bottom:1rem;">❌</div>
                <h2 style="color:var(--danger);">Application <%= account.getStatus() %></h2>
                <p class="text-muted" style="margin-bottom:1.5rem;">Your KYC application was not approved. Please contact our branch for assistance.</p>
                <p style="font-weight:600;">📍 Sundar Haraicha 04, Dulari, Itahari, Nepal</p>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
