package ru.vadim.common.query;

public class FetchUserPaymentDetailsQuery {
    private String userId;

    public FetchUserPaymentDetailsQuery(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
