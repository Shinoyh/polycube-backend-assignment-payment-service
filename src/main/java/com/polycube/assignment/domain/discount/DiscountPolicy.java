package com.polycube.assignment.domain.discount;

import com.polycube.assignment.domain.member.Member;

public interface DiscountPolicy {
    /**
     * @param member 회원 정보
     * @param price 주문 금액
     * @return 할인된 금액
     */
    int discount(Member member, int price);
}
