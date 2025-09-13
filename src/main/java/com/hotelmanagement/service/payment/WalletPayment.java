package com.hotelmanagement.service.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class WalletPayment implements PaymentProcessor {

    @Override
    public boolean processPayment(String paymentDetails, BigDecimal amount) {

        log.info("Processing wallet payment of {} with details: {}", amount, paymentDetails);
        try {
            Thread.sleep(500);
            boolean success = Math.random() < 0.95;

            if (success) {
                log.info("Wallet payment successful for amount: {}", amount);
            } else {
                log.error("Wallet payment failed for amount: {}", amount);
            }

            return success;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Wallet payment processing interrupted", e);
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "WALLET";
    }
}