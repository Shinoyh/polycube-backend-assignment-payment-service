package com.polycube.assignment.domain.payment;

import com.polycube.assignment.domain.discount.grade.DiscountPolicy;
import com.polycube.assignment.domain.discount.grade.DiscountPolicyRouter;
import com.polycube.assignment.domain.discount.payment.PaymentDiscountPolicy;
import com.polycube.assignment.domain.discount.payment.PaymentDiscountPolicyRouter;
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
    private final PaymentDiscountPolicyRouter paymentDiscountPolicyRouter;

    @Override
    public void processPayment(Long orderId, PaymentMethod paymentMethod) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        Member member = memberRepository.findById(order.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        DiscountPolicy gradeDiscountPolicy = discountPolicyRouter.getDiscountPolicy(member);

        // 등급 할인 적용된 가격 (1순위)
        int gradeDiscountPrice = order.calculatePrice();
        int finalPrice = gradeDiscountPrice;

        PaymentDiscountPolicy paymentDiscountPolicy = paymentDiscountPolicyRouter.getPaymentDiscountPolicy(paymentMethod);

        int paymentDiscountAmount = paymentDiscountPolicy.calculateDiscount(gradeDiscountPrice);

        if (paymentDiscountAmount > 0) {
            finalPrice -= paymentDiscountAmount;
        }

        Payment payment = Payment.builder()
                .order(order)
                .finalPrice(finalPrice)
                .paymentMethod(paymentMethod)
                .build();

        paymentRepository.save(payment);

        // 이력 저장
        // 등급 할인 이력
        if (order.getDiscountPrice() > 0) {
            saveHistory(payment, member, gradeDiscountPolicy.getPolicyName(),
                    order.getDiscountPrice(), gradeDiscountPolicy.getDiscountPercent(), gradeDiscountPolicy.getDescription());
        }

        // 결제 수단 할인 이력
        if (paymentDiscountAmount > 0) {
            saveHistory(payment, member, paymentDiscountPolicy.getDiscountPolicyName(),
                    paymentDiscountAmount, paymentDiscountPolicy.getDiscountPercent(), paymentDiscountPolicy.getDescription());
        }
    }

    // 할인 이력 저장 메서드
    private void saveHistory(Payment payment, Member member, String policyName, int discountAmount, int discountRate, String description) {

        DiscountHistory history = DiscountHistory.builder()
                .payment(payment)               // 어떤 결제 건인지
                .memberGrade(member.getGrade()) // 회원 등급
                .discountPolicyName(policyName) // 적용된 할인 정책 이름
                .discountAmount(discountAmount) // 할인 금액
                .discountPercent(discountRate)  // 할인율 (없으면 0)
                .description(description)       // 할인 정책 설명
                .build();

        discountHistoryRepository.save(history);
    }
}
