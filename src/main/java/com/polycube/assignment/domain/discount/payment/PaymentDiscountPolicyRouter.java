package com.polycube.assignment.domain.discount.payment;

import com.polycube.assignment.domain.payment.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentDiscountPolicyRouter {

    private final PointPaymentDiscountPolicy pointPaymentDiscountPolicy;
    private final NonePaymentDiscountPolicy nonePaymentDiscountPolicy;

    public PaymentDiscountPolicy getPaymentDiscountPolicy(PaymentMethod paymentMethod) {
        if (paymentMethod == PaymentMethod.POINT) {
            return pointPaymentDiscountPolicy;
        }

        return nonePaymentDiscountPolicy;
    }

}
