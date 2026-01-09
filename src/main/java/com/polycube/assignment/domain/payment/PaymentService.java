package com.polycube.assignment.domain.payment;

public interface PaymentService {

    /**
     * @param orderId
     * @param paymentMethod
     */
    void processPayment(Long orderId, PaymentMethod paymentMethod);
}
