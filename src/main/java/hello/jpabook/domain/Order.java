package hello.jpabook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity@Table(name = "orders")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)//new 금지
public class Order {
    @Id@GeneratedValue
    @Column(name = "order_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")//외부 키의 주인
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")//주인
    private Delivery delivery;
    private LocalDateTime orderDate;//주문 시간
    @Enumerated(EnumType.STRING)
    private OrderStatus status;//주분 상태

    //연관관계 편의 메소드 양방향 객체를 편하게 연결
    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){//리스트 추가
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }


    //주문 생성 메소드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order=new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);//오더롤 설정(취소 아님)
        order.setOrderDate(LocalDateTime.now());//현제 시간으로 설정
        return order;
    }
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus()== DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능 합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();//제고 원상복귀
        }
    }
    //조회 로직
    /**
     * 전체 주문 가격 주문 조회
     */
    public int getTotalPrice(){
        int totalPrice=0;
        for (OrderItem orderItem : orderItems) {
            totalPrice+= orderItem.getTotalPrice();//주문 수량*가격 받기
        }
        return totalPrice;
        //스트림 사용시 한줄로 가능
        //return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
