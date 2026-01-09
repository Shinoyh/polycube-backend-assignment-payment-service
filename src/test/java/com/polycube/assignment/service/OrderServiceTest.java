package com.polycube.assignment.service;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import com.polycube.assignment.domain.member.MemberRepository;
import com.polycube.assignment.domain.order.Order;
import com.polycube.assignment.domain.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("VIP 회원 할인 정책은 1000원 할인")
    void createOrder() {

        //given
        Member member = new Member("memberA", Grade.VIP);
        memberRepository.save(member);

        //when
        Order order = orderService.createOrder(member.getId(), "커피", 4500);

        //then
        // 할인 금액이 1000원이 맞는지 확인
        assertThat(order.getDiscountPrice()).isEqualTo(1000);

        // 최종 결제 금액이 3500원이 맞는지 확인
        assertThat(order.calculatePrice()).isEqualTo(3500);
    }

    @Test
    @DisplayName("일반 회원 할인 정책은 없다")
    void createOrder_Normal() {

        //given
        Member member = new Member("memberB", Grade.NORMAL);
        memberRepository.save(member);

        //when
        Order order = orderService.createOrder(member.getId(), "커피", 4500);

        //then
        // 할인 금액이 0원이 맞는지 확인
        assertThat(order.getDiscountPrice()).isEqualTo(0);

        // 최종 결제 금액이 4500원이 맞는지 확인
        assertThat(order.calculatePrice()).isEqualTo(4500);
    }
}
