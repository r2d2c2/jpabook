package hello.jpabook.api;

import hello.jpabook.domain.Address;
import hello.jpabook.domain.Order;
import hello.jpabook.domain.OrderItem;
import hello.jpabook.domain.OrderStatus;
import hello.jpabook.repository.OrderRepository;
import hello.jpabook.repository.OrderSearch;
import hello.jpabook.repository.order.query.OrderQueryDto;
import hello.jpabook.repository.order.query.OrderQuryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQuryRepository orderQuryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){//앤티티 직접 연결(권장 하지 않음)
        List<Order> all=orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(i->i.getItem().getName());
            //람다식 초기화
        }
        return all;
    }
    @GetMapping("/api/v2/orders")//엔티티를 dto로 반환
    public List<OrderDto> ordersV2(){//근데 sql문이 여러개다
        List<Order> orderDtos=orderRepository.findAllByString(new OrderSearch());
        return orderDtos.stream().map(i -> new OrderDto(i)).collect(Collectors.toList());
    }
    @GetMapping("/api/v3/orders")//최적화
    public List<OrderDto> ordersV3(){//근데 페이징 불가(가능은 하지만 메모리 낭비가 심하다)
        List<Order> orders=orderRepository.findAllWithItem();//패치조인으로 메소드 추가
        return orders.stream().map(i->new OrderDto(i)).collect(Collectors.toList());
    }
    @GetMapping("/api/v3.1/orders")//최적화
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /*@GetMapping("/api/v4/orders")//dto
    public List<OrderQueryDto> ordersV4(){
        return orderQuryRepository.findOrderQueryDtos();
    }*/

    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;//엔티티가 출력 안되게 class를 추가 필수
        public OrderDto(Order i) {
            orderId=i.getId();
            name=i.getMember().getName();
            orderDate=i.getOrderDate();
            address=i.getDelivery().getAddress();
//            i.getOrderItems().stream().forEach(ii->ii.getItem().getName());
//            orderItems=i.getOrderItems();//엔티티를 직접 출력 된다

            orderItems=i.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem)).collect(Collectors.toList());
        }
    }
    @Getter
    static class OrderItemDto{//dto리스트를 위해 추가
        private String itemName;//필요한 값만 담아서 출력
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            itemName=orderItem.getItem().getName();
            orderPrice=orderItem.getOrderPrice();
            count=orderItem.getCount();
        }

    }
}
