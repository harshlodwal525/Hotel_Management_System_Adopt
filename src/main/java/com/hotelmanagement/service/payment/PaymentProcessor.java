package com.hotelmanagement.service.payment;

import java.math.BigDecimal;

public interface PaymentProcessor {
    boolean processPayment(String paymentDetails, BigDecimal amount);
    String getPaymentMethod();
}