package com.polycube.assignment.domain.payment;

import com.polycube.assignment.domain.discount.DiscountPolicy;
import com.polycube.assignment.domain.discount.DiscountPolicyRouter;
import com.polycube.assignment.domain.discounthistory.DiscountHistory;
import com.polycube.assignment.domain.discounthistory.DiscountHistoryRepository;
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
    private final MemberRepository memberRepository;
    private final DiscountHistoryRepository discountHistoryRepository;
    private final DiscountPolicyRouter discountPolicyRouter;

    @Override
    public void processPayment(Long orderId, PaymentMethod paymentMethod) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        Member member = memberRepository.findById(order.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        DiscountPolicy discountPolicy = discountPolicyRouter.getDiscountPolicy(member);

        // 등급 할인 적용된 가격 (1순위)
        int gradeDiscountPrice = order.calculatePrice();
        //int finalPrice = gradeDiscountPrice;

        Payment payment = Payment.builder()
                .order(order)
                .finalPrice(gradeDiscountPrice)
                .paymentMethod(paymentMethod)
                .build();

        paymentRepository.save(payment);

        DiscountHistory discountHistory = DiscountHistory.builder()
                .payment(payment)
                .memberGrade(member.getGrade())
                .discountPolicyName(discountPolicy.getPolicyName())
                .discountPercent(discountPolicy.getDiscountPercent())
                .discountAmount(order.getDiscountPrice())
                .description(discountPolicy.getDescription())
                .build();

        discountHistoryRepository.save(discountHistory);
    }
}
