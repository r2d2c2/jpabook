package hello.jpabook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.jpabook.domain.item.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity@Getter@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)// protected OrderItem(){}//상속이 아니면 new 호츨 막기
public class OrderItem {
    @Id@GeneratedValue
    @Column(name = "order_item_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private int orderPrice;//가격
    private int count;//수량

    //생성 메소드
   // protected OrderItem(){}//상속이 아니면 new 호츨 막기
    public static OrderItem createOrderItem(Item item,int orderPrice,int count){
        OrderItem orderItem=new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);//팔린 아이템 제거
        return orderItem;
    }

    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count);//취소 되면 제고 원상복귀
    }
    //조회 로직
    public int getTotalPrice() {
        return getOrderPrice()*getCount();//주분한 가격*수량 반환
    }
}
