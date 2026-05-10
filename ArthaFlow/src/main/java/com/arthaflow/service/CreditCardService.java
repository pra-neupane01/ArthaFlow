package com.arthaflow.service;

import com.arthaflow.dao.CreditCardDAO;
import com.arthaflow.model.CreditCard;

import java.util.List;
import java.util.Random;

public class CreditCardService {
    private CreditCardDAO cardDAO = new CreditCardDAO();

    public boolean applyForCard(int userId, String cardType) {
        CreditCard card = new CreditCard();
        card.setUserId(userId);
        card.setCardType(cardType);
        return cardDAO.applyForCard(card);
    }

    public CreditCard getCardDetails(int userId) {
        return cardDAO.getCardByUserId(userId);
    }

    public List<CreditCard> getAllRequests() {
        return cardDAO.getAllCardRequests();
    }

    public boolean approveCard(int cardId) {
        String cardNumber = generateCardNumber();
        String expiryDate = "12/29"; // Mock expiry
        double limit = 50000.0; // Default limit
        return cardDAO.updateCardStatus(cardId, "ACTIVE", cardNumber, expiryDate, limit);
    }

    public boolean rejectCard(int cardId) {
        return cardDAO.updateCardStatus(cardId, "REJECTED", null, null, 0);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("4242");
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
