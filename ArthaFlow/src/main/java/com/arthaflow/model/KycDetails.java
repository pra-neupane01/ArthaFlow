package com.arthaflow.model;

import java.sql.Date;
import java.sql.Timestamp;

/** One row per account opening or per credit card application (see {@code purpose}). */
public class KycDetails {
    public static final String PURPOSE_ACCOUNT_OPENING = "ACCOUNT_OPENING";
    public static final String PURPOSE_CREDIT_CARD = "CREDIT_CARD";

    private int kycId;
    private int userId;
    private String purpose;
    private Integer accountId;
    private Integer cardId;
    private String status;
    private String citizenshipNumber;
    private Date dateOfBirth;
    private String occupation;
    private String fatherName;
    private String motherName;
    private String familyDetails;
    private String gender;
    private String permanentAddress;
    private String mailingAddress;
    private String idDocumentPath;
    private String addressProofPath;
    private double annualIncome;
    private double minimumIncome;
    private String personalInformation;
    private String incomeDetails;
    private String employmentDetails;
    private String cardPreferences;
    private String creditInformation;
    private boolean termsAccepted;
    private Timestamp createdDate;

    public KycDetails() {}

    public int getKycId() { return kycId; }
    public void setKycId(int kycId) { this.kycId = kycId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    public Integer getCardId() { return cardId; }
    public void setCardId(Integer cardId) { this.cardId = cardId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCitizenshipNumber() { return citizenshipNumber; }
    public void setCitizenshipNumber(String citizenshipNumber) { this.citizenshipNumber = citizenshipNumber; }
    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }
    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
    public String getFamilyDetails() { return familyDetails; }
    public void setFamilyDetails(String familyDetails) { this.familyDetails = familyDetails; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    public String getMailingAddress() { return mailingAddress; }
    public void setMailingAddress(String mailingAddress) { this.mailingAddress = mailingAddress; }
    /** Alias used by legacy admin credit views (same as mailing / temporary address). */
    public String getTemporaryAddress() { return mailingAddress; }
    public void setTemporaryAddress(String v) { this.mailingAddress = v; }
    public String getIdDocumentPath() { return idDocumentPath; }
    public void setIdDocumentPath(String idDocumentPath) { this.idDocumentPath = idDocumentPath; }
    public String getAddressProofPath() { return addressProofPath; }
    public void setAddressProofPath(String addressProofPath) { this.addressProofPath = addressProofPath; }
    public double getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(double annualIncome) { this.annualIncome = annualIncome; }
    public double getMinimumIncome() { return minimumIncome; }
    public void setMinimumIncome(double minimumIncome) { this.minimumIncome = minimumIncome; }
    public String getPersonalInformation() { return personalInformation; }
    public void setPersonalInformation(String personalInformation) { this.personalInformation = personalInformation; }
    public String getIncomeDetails() { return incomeDetails; }
    public void setIncomeDetails(String incomeDetails) { this.incomeDetails = incomeDetails; }
    public String getEmploymentDetails() { return employmentDetails; }
    public void setEmploymentDetails(String employmentDetails) { this.employmentDetails = employmentDetails; }
    public String getCardPreferences() { return cardPreferences; }
    public void setCardPreferences(String cardPreferences) { this.cardPreferences = cardPreferences; }
    public String getCreditInformation() { return creditInformation; }
    public void setCreditInformation(String creditInformation) { this.creditInformation = creditInformation; }
    public boolean isTermsAccepted() { return termsAccepted; }
    public void setTermsAccepted(boolean termsAccepted) { this.termsAccepted = termsAccepted; }
    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}
