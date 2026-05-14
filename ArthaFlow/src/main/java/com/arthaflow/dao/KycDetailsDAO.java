package com.arthaflow.dao;

import com.arthaflow.model.KycDetails;
import com.arthaflow.util.DatabaseConnection;

import java.sql.*;

public class KycDetailsDAO {

    public boolean insert(KycDetails k, Connection conn) throws SQLException {
        String sql = "INSERT INTO kyc_details (user_id, purpose, account_id, card_id, status, "
                + "citizenship_number, date_of_birth, occupation, father_name, mother_name, family_details, gender, "
                + "permanent_address, mailing_address, id_document_path, address_proof_path, "
                + "annual_income, minimum_income, personal_information, income_details, "
                + "employment_details, card_preferences, credit_information, terms_accepted, rejection_remarks) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            ps.setInt(i++, k.getUserId());
            ps.setString(i++, k.getPurpose());
            setNullableInt(ps, i++, k.getAccountId());
            setNullableInt(ps, i++, k.getCardId());
            ps.setString(i++, k.getStatus() != null ? k.getStatus() : "PENDING");
            ps.setString(i++, emptyToNull(k.getCitizenshipNumber()));
            if (k.getDateOfBirth() != null) ps.setDate(i++, k.getDateOfBirth());
            else ps.setNull(i++, Types.DATE);
            ps.setString(i++, emptyToNull(k.getOccupation()));
            ps.setString(i++, emptyToNull(k.getFatherName()));
            ps.setString(i++, emptyToNull(k.getMotherName()));
            ps.setString(i++, emptyToNull(k.getFamilyDetails()));
            ps.setString(i++, emptyToNull(k.getGender()));
            ps.setString(i++, emptyToNull(k.getPermanentAddress()));
            ps.setString(i++, emptyToNull(k.getMailingAddress()));
            ps.setString(i++, emptyToNull(k.getIdDocumentPath()));
            ps.setString(i++, emptyToNull(k.getAddressProofPath()));
            setMoneyNullable(ps, i++, k.getAnnualIncome());
            setMoneyNullable(ps, i++, k.getMinimumIncome());
            ps.setString(i++, emptyToNull(k.getPersonalInformation()));
            ps.setString(i++, emptyToNull(k.getIncomeDetails()));
            ps.setString(i++, emptyToNull(k.getEmploymentDetails()));
            ps.setString(i++, emptyToNull(k.getCardPreferences()));
            ps.setString(i++, emptyToNull(k.getCreditInformation()));
            ps.setInt(i++, k.isTermsAccepted() ? 1 : 0);
            ps.setString(i++, emptyToNull(k.getRejectionRemarks()));
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) {
                conn.close();
            }
        }
    }

    private static void setNullableInt(PreparedStatement ps, int idx, Integer v) throws SQLException {
        if (v == null) ps.setNull(idx, Types.INTEGER);
        else ps.setInt(idx, v);
    }

    /** Store NULL when amount is 0 (unused field for that row type). */
    private static void setMoneyNullable(PreparedStatement ps, int idx, double v) throws SQLException {
        if (v == 0) ps.setNull(idx, Types.DECIMAL);
        else ps.setDouble(idx, v);
    }

    private static String emptyToNull(String s) {
        if (s == null || s.isBlank()) return null;
        return s.trim();
    }

    public KycDetails findByAccountId(int accountId) {
        String sql = "SELECT * FROM kyc_details WHERE account_id = ? AND purpose = 'ACCOUNT_OPENING' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("findByAccountId: " + e.getMessage());
        }
        return null;
    }

    public KycDetails findByCardId(int cardId) {
        String sql = "SELECT * FROM kyc_details WHERE card_id = ? AND purpose = 'CREDIT_CARD' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return map(rs);
        } catch (SQLException e) {
            System.out.println("findByCardId: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStatusByCardId(int cardId, String status, Connection conn) throws SQLException {
        String sql = "UPDATE kyc_details SET status = ? WHERE card_id = ? AND purpose = 'CREDIT_CARD'";
        boolean closeConn = false;
        if (conn == null) {
            conn = DatabaseConnection.getConnection();
            closeConn = true;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, cardId);
            return ps.executeUpdate() > 0;
        } finally {
            if (closeConn && conn != null) conn.close();
        }
    }

    public boolean updateStatusByCardId(int cardId, String status) {
        try {
            return updateStatusByCardId(cardId, status, null);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean updateStatusByAccountId(int accountId, String status) {
        String sql = "UPDATE kyc_details SET status = ? WHERE account_id = ? AND purpose = 'ACCOUNT_OPENING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateStatusByAccountId: " + e.getMessage());
            return false;
        }
    }

    public boolean updateStatusAndRemarksByAccountId(int accountId, String status, String rejectionRemarks) {
        String sql = "UPDATE kyc_details SET status = ?, rejection_remarks = ? WHERE account_id = ? AND purpose = 'ACCOUNT_OPENING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, emptyToNull(rejectionRemarks));
            ps.setInt(3, accountId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("updateStatusAndRemarksByAccountId: " + e.getMessage());
            return false;
        }
    }

    private KycDetails map(ResultSet rs) throws SQLException {
        KycDetails k = new KycDetails();
        k.setKycId(rs.getInt("kyc_id"));
        k.setUserId(rs.getInt("user_id"));
        k.setPurpose(rs.getString("purpose"));
        int aid = rs.getInt("account_id");
        k.setAccountId(rs.wasNull() ? null : aid);
        int cid = rs.getInt("card_id");
        k.setCardId(rs.wasNull() ? null : cid);
        k.setStatus(rs.getString("status"));
        k.setCitizenshipNumber(rs.getString("citizenship_number"));
        Date dob = rs.getDate("date_of_birth");
        k.setDateOfBirth(rs.wasNull() ? null : dob);
        k.setOccupation(rs.getString("occupation"));
        k.setFatherName(rs.getString("father_name"));
        k.setMotherName(rs.getString("mother_name"));
        k.setFamilyDetails(rs.getString("family_details"));
        k.setGender(rs.getString("gender"));
        k.setPermanentAddress(rs.getString("permanent_address"));
        k.setMailingAddress(rs.getString("mailing_address"));
        k.setIdDocumentPath(rs.getString("id_document_path"));
        k.setAddressProofPath(rs.getString("address_proof_path"));
        double ann = rs.getDouble("annual_income");
        k.setAnnualIncome(rs.wasNull() ? 0 : ann);
        double min = rs.getDouble("minimum_income");
        k.setMinimumIncome(rs.wasNull() ? 0 : min);
        k.setPersonalInformation(rs.getString("personal_information"));
        k.setIncomeDetails(rs.getString("income_details"));
        k.setEmploymentDetails(rs.getString("employment_details"));
        k.setCardPreferences(rs.getString("card_preferences"));
        k.setCreditInformation(rs.getString("credit_information"));
        k.setTermsAccepted(rs.getInt("terms_accepted") == 1);
        k.setRejectionRemarks(rs.getString("rejection_remarks"));
        k.setCreatedDate(rs.getTimestamp("created_date"));
        return k;
    }
}
