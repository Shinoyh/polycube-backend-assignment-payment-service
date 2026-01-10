package com.polycube.assignment.domain.discount.payment;

import com.polycube.assignment.domain.payment.Payment;

public interface PaymentDiscountPolicy {

    int calculateDiscount(int price);

    String getDiscountPolicyName();

    String getDescription();

    int getDiscountPercent();
}
