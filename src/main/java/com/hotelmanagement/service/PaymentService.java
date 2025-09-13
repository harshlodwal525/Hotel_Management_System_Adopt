package com.hotelmanagement.service;

import com.hotelmanagement.service.payment.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final List<PaymentProcessor> paymentProcessors;

    public boolean processPayment(String paymentMethod, String paymentDetails, BigDecimal amount) {
        Map<String, PaymentProcessor> processorMap = paymentProcessors.stream()
                .collect(Collectors.toMap(
                        PaymentProcessor::getPaymentMethod,
                        Function.identity()
                ));

        PaymentProcessor processor = processorMap.get(paymentMethod.toUpperCase());

        if (processor == null) {
            log.error("Unsupported payment method: {}", paymentMethod);
            return false;
        }

        return processor.processPayment(paymentDetails, amount);
    }

    public List<String> getSupportedPaymentMethods() {
        return paymentProcessors.stream()
                .map(PaymentProcessor::getPaymentMethod)
                .collect(Collectors.toList());
    }
}
