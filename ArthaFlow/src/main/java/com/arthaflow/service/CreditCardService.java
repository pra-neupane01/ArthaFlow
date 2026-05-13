package com.arthaflow.service;

import com.arthaflow.dao.CreditCardDAO;
import com.arthaflow.dao.CreditCardTransactionDAO;
import com.arthaflow.dao.KycDetailsDAO;
import com.arthaflow.model.CreditCard;
import com.arthaflow.model.CreditCardTransaction;
import com.arthaflow.model.KycDetails;
import com.arthaflow.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class CreditCardService {
    private final CreditCardDAO cardDAO = new CreditCardDAO();
    private final KycDetailsDAO kycDetailsDAO = new KycDetailsDAO();
    private final CreditCardTransactionDAO cctDAO = new CreditCardTransactionDAO();

    public CreditCard getCardDetails(int userId) {
        return cardDAO.getCardByUserId(userId);
    }

    public KycDetails getKycForCard(int cardId) {
        return kycDetailsDAO.findByCardId(cardId);
    }

    public List<CreditCard> getAllRequests() {
        return cardDAO.getAllCardRequests();
    }

    /** Block apply if latest card is PENDING or ACTIVE */
    public boolean applyForCardWithKyc(int userId, String cardType, KycDetails kyc) {
        CreditCard latest = cardDAO.getCardByUserId(userId);
        if (latest != null && ("PENDING".equals(latest.getStatus()) || "ACTIVE".equals(latest.getStatus()))) {
            return false;
        }
        CreditCard card = new CreditCard();
        card.setUserId(userId);
        card.setCardType(cardType);
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int cardId = cardDAO.insertPendingCard(card, conn);
                if (cardId < 0) {
                    conn.rollback();
                    return false;
                }
                kyc.setUserId(userId);
                kyc.setPurpose(KycDetails.PURPOSE_CREDIT_CARD);
                kyc.setAccountId(null);
                kyc.setCardId(cardId);
                kyc.setStatus("PENDING");
                if (!kycDetailsDAO.insert(kyc, conn)) {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("applyForCardWithKyc: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("applyForCardWithKyc conn: " + e.getMessage());
            return false;
        }
    }

    public boolean verifyCardKyc(int cardId) {
        CreditCard c = cardDAO.getCardById(cardId);
        if (c == null || !"PENDING".equals(c.getStatus())) {
            return false;
        }
        KycDetails k = kycDetailsDAO.findByCardId(cardId);
        if (k == null || !"PENDING".equals(k.getStatus())) {
            return false;
        }
        return kycDetailsDAO.updateStatusByCardId(cardId, "APPROVED");
    }

    public boolean issueCardAfterKyc(int cardId) {
        CreditCard c = cardDAO.getCardById(cardId);
        if (c == null || !"PENDING".equals(c.getStatus())) {
            return false;
        }
        KycDetails k = kycDetailsDAO.findByCardId(cardId);
        if (k == null || !"APPROVED".equals(k.getStatus())) {
            return false;
        }
        double limit = "PLATINUM".equalsIgnoreCase(c.getCardType()) ? 100_000.0 : 50_000.0;
        String expiry = "12/29";
        String number = generateUniqueCardNumber();
        return cardDAO.updateCardStatus(cardId, "ACTIVE", number, expiry, limit);
    }

    public boolean rejectCardApplication(int cardId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                kycDetailsDAO.updateStatusByCardId(cardId, "REJECTED", conn);
                if (!cardDAO.rejectCard(cardId, conn)) {
                    conn.rollback();
                    return false;
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("rejectCardApplication: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("rejectCardApplication conn: " + e.getMessage());
            return false;
        }
    }

    private String generateUniqueCardNumber() {
        Random random = new Random();
        for (int attempt = 0; attempt < 50; attempt++) {
            StringBuilder sb = new StringBuilder("4242");
            for (int i = 0; i < 12; i++) {
                sb.append(random.nextInt(10));
            }
            String num = sb.toString();
            if (!cardDAO.cardNumberExists(num)) {
                return num;
            }
        }
        return "4242" + System.currentTimeMillis() % 1_000_000_000_000L;
    }

    public String setPin(int userId, int cardId, String pin, String confirm) {
        if (pin == null || pin.length() < 4 || pin.length() > 6 || !pin.matches("\\d+")) {
            return "PIN must be 4–6 digits.";
        }
        if (!pin.equals(confirm)) {
            return "PINs do not match.";
        }
        CreditCard c = cardDAO.getCardById(cardId);
        if (c == null || c.getUserId() != userId || !"ACTIVE".equals(c.getStatus())) {
            return "Invalid card.";
        }
        if (c.isPinSet()) {
            return "PIN is already set.";
        }
        String hash = BCrypt.hashpw(pin, BCrypt.gensalt(10));
        return cardDAO.updatePinHash(cardId, hash) ? null : "Could not save PIN.";
    }

    public String purchaseWithCard(int userId, String pin, double amount, String remarks) {
        if (amount <= 0) {
            return "Amount must be positive.";
        }
        CreditCard c = cardDAO.getCardByUserId(userId);
        if (c == null || !"ACTIVE".equals(c.getStatus())) {
            return "No active card.";
        }
        if (!c.isPinSet()) {
            return "Please set your card PIN first.";
        }
        if (pin == null || !BCrypt.checkpw(pin, c.getPinHash())) {
            return "Invalid PIN.";
        }
        double newBal = c.getCurrentBalance() + amount;
        if (newBal > c.getCreditLimit()) {
            return "Exceeds credit limit.";
        }
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (!cardDAO.updateCurrentBalance(c.getCardId(), newBal, conn)) {
                    conn.rollback();
                    return "Update failed.";
                }
                CreditCardTransaction tx = new CreditCardTransaction();
                tx.setCardId(c.getCardId());
                tx.setType("PURCHASE");
                tx.setAmount(amount);
                tx.setBalanceAfter(newBal);
                tx.setRemarks(remarks == null || remarks.isEmpty() ? "Card purchase" : remarks);
                tx.setStatus("SUCCESS");
                if (!cctDAO.insert(tx, conn)) {
                    conn.rollback();
                    return "Ledger failed.";
                }
                conn.commit();
                return null;
            } catch (SQLException e) {
                conn.rollback();
                return "Error: " + e.getMessage();
            }
        } catch (SQLException e) {
            return "Database error.";
        }
    }

    public List<CreditCardTransaction> getCardTransactionHistory(int userId) {
        CreditCard c = cardDAO.getCardByUserId(userId);
        if (c == null) {
            return List.of();
        }
        return cctDAO.listByCardId(c.getCardId());
    }
}
