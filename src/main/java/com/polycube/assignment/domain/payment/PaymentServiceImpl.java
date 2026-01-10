package com.polycube.assignment.domain.payment;

import com.polycube.assignment.domain.discount.DiscountPolicy;
import com.polycube.assignment.domain.discount.DiscountPolicyRouter;
import com.polycube.assignment.domain.member.Member;
import com.polycube.assignment.domain.member.MemberRepository;
import com.polycube.assignment.domain.order.Order;
import com.polycube.assignment.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Long orderId, PaymentMethod paymentMethod) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        int finalPrice = order.calculatePrice();

        Payment payment = Payment.builder()
                .order(order)
                .finalPrice(finalPrice)
                .paymentMethod(paymentMethod)
                .build();

        paymentRepository.save(payment);
    }
}
