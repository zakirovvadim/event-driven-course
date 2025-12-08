package ru.vadim.common.model;

public class User {
    private final String firstName;
    private final String lastName;
    private final String userId;
    private final PaymentDetails paymentDetails;

    public User(String firstName, String lastName, String userId, PaymentDetails paymentDetails) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.paymentDetails = paymentDetails;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }
}
