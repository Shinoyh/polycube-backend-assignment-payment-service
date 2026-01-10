package com.polycube.assignment.domain.discount;

import com.polycube.assignment.domain.member.Grade;
import com.polycube.assignment.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountPolicyRouter {

    private final FixDiscountPolicy fixDiscountPolicy;
    private final RateDiscountPolicy rateDiscountPolicy;

    public DiscountPolicy getDiscountPolicy(Member member) {

        if (member.getGrade() == Grade.VIP) {
            return fixDiscountPolicy;
        }
        else if (member.getGrade() == Grade.VVIP) {
            return rateDiscountPolicy;
        }

        // 일반 회원은 어차피 고정 할인 정책에서 할인을 못받는다.
        // 가독성을 위한다면 normalDiscontPolicy를 만드는 것이 좋겠다.
        return fixDiscountPolicy;
    }
}
