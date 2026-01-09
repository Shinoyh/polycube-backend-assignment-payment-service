package com.polycube.assignment.domain.order;

import com.polycube.assignment.domain.discount.DiscountPolicy;
import com.polycube.assignment.domain.member.Member;
import com.polycube.assignment.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final DiscountPolicy discountPolicy;

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        int discountPrice = discountPolicy.discount(member, itemPrice);

        Order order = Order.builder()
                .memberId(memberId)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .discountPrice(discountPrice)
                .build();

        return orderRepository.save(order);
    }
}
