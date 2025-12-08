package ru.vadim.common.model;

public class PaymentDetails {
    private final String name;
    private final String cardNumber;
    private final int validUntilMonth;
    private final int validUntilYear;
    private final String cvv;

    public PaymentDetails(String name, String cardNumber, int validUntilMonth, int validUntilYear, String cvv) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.validUntilMonth = validUntilMonth;
        this.validUntilYear = validUntilYear;
        this.cvv = cvv;
    }

    public String getName() {
        return name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getValidUntilMonth() {
        return validUntilMonth;
    }

    public int getValidUntilYear() {
        return validUntilYear;
    }

    public String getCvv() {
        return cvv;
    }
}
