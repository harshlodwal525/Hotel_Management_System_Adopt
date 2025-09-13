package com.hotelmanagement.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class CardPayment implements PaymentProcessor {

    @Override
    public boolean processPayment(String paymentDetails, BigDecimal amount) {

        log.info("Processing card payment of {} with details: {}", amount, paymentDetails);

        try {
            Thread.sleep(1000);
            boolean success = Math.random() < 0.9;

            if (success) {
                log.info("Card payment successful for amount: {}", amount);
            } else {
                log.error("Card payment failed for amount: {}", amount);
            }

            return success;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Card payment processing interrupted", e);
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "CARD";
    }
}