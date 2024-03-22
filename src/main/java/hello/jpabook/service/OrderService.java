package hello.jpabook.service;

import hello.jpabook.domain.*;
import hello.jpabook.domain.item.Item;
import hello.jpabook.repository.ItemRepository;
import hello.jpabook.repository.MemberRepository;
import hello.jpabook.repository.OrderRepository;
import hello.jpabook.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    //주문
    @Transactional//변경
    public Long order(Long memberId,Long itemId,int count){
        //엔티티 조회
        Member member=memberRepository.findOne(memberId);
        Item item= itemRepository.findOne(itemId);
        //배송 정보 지정
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
        //주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        //주문 저장
        log.info(order.toString());
        log.info("orderRepository.save  저장 시작!!");
        orderRepository.save(order);//저장
        log.info("orderRepository.save  저장 끝");
        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order=orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }
    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByString(orderSearch);
    }
}
