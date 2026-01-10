package com.polycube.assignment.service;

import com.polycube.assignment.domain.discounthistory.DiscountHistory;
import com.polycube.assignment.domain.discounthistory.DiscountHistoryRepository;
import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import com.polycube.assignment.domain.member.MemberRepository;
import com.polycube.assignment.domain.order.Order;
import com.polycube.assignment.domain.order.OrderService;
import com.polycube.assignment.domain.payment.Payment;
import com.polycube.assignment.domain.payment.PaymentMethod;
import com.polycube.assignment.domain.payment.PaymentRepository;
import com.polycube.assignment.domain.payment.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PaymentDuplicateDiscountTest {

    @Autowired
    PaymentService paymentService;
    @Autowired
    OrderService orderService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    DiscountHistoryRepository discountHistoryRepository;

    @Test
    @DisplayName("중복 할인 검증: VIP회원(1000원 할인)이 포인트결제(5% 할인)를 하면 우선순위에 맞춰 최종 금액이 계산되고, 이력이 2개 저장된다.")
    void doubleDiscountTest() {
        //given
        // VIP 회원 생성
        Member member = new Member("testVIP", Grade.VIP);
        memberRepository.save(member);

        // 1차 할인(VIP 1000원) 적용, 9000원 예상
        Order order = orderService.createOrder(member.getId(), "피자", 10000);

        //when
        // 포인트로 결제 진행 (여기서 5% 추가 할인이 들어가야 함)
        // 9000원의 5% = 450원 추가 할인
        paymentService.processPayment(order.getId(), PaymentMethod.POINT);


        //then
        // 결제 정보 검증
        Payment payment = paymentRepository.findAll().get(0);

        // 최종 금액 검증: 10000 - 1000(등급) - 450(포인트) = 8550원
        assertThat(payment.getFinalPrice()).isEqualTo(8550);
        assertThat(payment.getPaymentMethod()).isEqualTo(PaymentMethod.POINT);

        List<DiscountHistory> histories = discountHistoryRepository.findAll();

        DiscountHistory gradeHistory = null;
        DiscountHistory pointHistory = null;

        for (DiscountHistory h : histories) {
            if (h.getDiscountPolicyName().equals("FixDiscountPolicy")) {
                gradeHistory = h; // 찾았다! 등급 할인 이력
            }
            else if (h.getDiscountPolicyName().equals("PointPaymentDiscountPolicy")) {
                pointHistory = h; // 찾았다! 포인트 할인 이력
            }
        }
        
        // 존재 여부 확인
        assertThat(gradeHistory).isNotNull();
        assertThat(pointHistory).isNotNull();

        // VIP 할인 이력 내용 검증
        assertThat(gradeHistory.getDiscountAmount()).isEqualTo(1000);
        assertThat(gradeHistory.getMemberGrade()).isEqualTo(Grade.VIP);

        // 포인트 할인 이력 내용 검증
        assertThat(pointHistory.getDiscountAmount()).isEqualTo(450); // 9000원의 5%
        assertThat(pointHistory.getDiscountPercent()).isEqualTo(5); // 5%

        System.out.println("최종 금액: " + payment.getFinalPrice() + "원 (8550원 예상)");
    }

    @Test
    @DisplayName("단일 할인 검증: VIP회원이 카드결제(할인없음)를 하면 등급 할인만 적용되어야 한다.")
    void singleDiscountTest() {

        //given
        Member member = new Member("testVIP2", Grade.VIP);
        memberRepository.save(member);
        Order order = orderService.createOrder(member.getId(), "치킨", 10000);

        //when
        // 카드 결제 (할인 없음)
        paymentService.processPayment(order.getId(), PaymentMethod.CREDIT_CARD);

        //then
        Payment payment = paymentRepository.findAll().get(0);
        List<DiscountHistory> histories = discountHistoryRepository.findAll();

        assertThat(payment.getFinalPrice()).isEqualTo(9000);

        // 이력은 등급 할인 1개만 있어야 함
        assertThat(histories).hasSize(1);
        assertThat(histories.get(0).getDiscountPolicyName()).isEqualTo("FixDiscountPolicy");
    }
}