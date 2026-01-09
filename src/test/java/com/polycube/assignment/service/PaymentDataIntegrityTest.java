package com.polycube.assignment.service;

import com.polycube.assignment.domain.discounthistory.DiscountHistory;
import com.polycube.assignment.domain.discounthistory.DiscountHistoryRepository;
import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import com.polycube.assignment.domain.member.MemberRepository;
import com.polycube.assignment.domain.order.Order;
import com.polycube.assignment.domain.order.OrderRepository;
import com.polycube.assignment.domain.order.OrderService;
import com.polycube.assignment.domain.payment.PaymentMethod;
import com.polycube.assignment.domain.payment.PaymentService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PaymentDataIntegrityTest {

    @Autowired
    PaymentService paymentService;
    @Autowired
    OrderService orderService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DiscountHistoryRepository discountHistoryRepository;


    @Test
    @DisplayName("정책이 변경되어도 과거 결제 이력 존재 확인")
    void historyDataIntegrityTest() {
        //given
        // 과거 결제 이력 생성(이전 할인 정책이 고정 할인이다.)
        DiscountHistory pastHistory = DiscountHistory.builder()
                .payment(null)
                .memberGrade(Grade.VIP)
                .discountPolicyName("FixDiscountPolicy")
                .discountAmount(1000)
                .discountPercent(0)
                .description("1000원 고정 금액 할인")
                .build();

        discountHistoryRepository.save(pastHistory);

        //when
        // 현재 정책(퍼센트 할인) 시행 중 새로운 주문 및 결제 생성
        Member memberA = new Member("A", Grade.VVIP);
        memberRepository.save(memberA);

        Order order = orderService.createOrder(memberA.getId(), "커피", 5000);

        paymentService.processPayment(order.getId(), PaymentMethod.CREDIT_CARD);

        List<DiscountHistory> discountHistories = discountHistoryRepository.findAll();

        boolean pastHistoryFound = false;
        boolean newHistoryFound = false;

        for (DiscountHistory discountHistory : discountHistories) {
            if (discountHistory.getDiscountPolicyName().equals("FixDiscountPolicy")) {
                pastHistoryFound = true;
                assertThat(discountHistory.getDiscountAmount()).isEqualTo(1000);
                assertThat(discountHistory.getDiscountPercent()).isEqualTo(0);
            }

            if (discountHistory.getDiscountPolicyName().equals("RateDiscountPolicy")) {
                newHistoryFound = true;
                assertThat(discountHistory.getDiscountAmount()).isEqualTo(500);
                assertThat(discountHistory.getDiscountPercent()).isEqualTo(10);
            }
        }

        assertThat(pastHistoryFound).isTrue();
        assertThat(newHistoryFound).isTrue();
    }
}
