package hello.jpabook.api;

import hello.jpabook.domain.Address;
import hello.jpabook.domain.Order;
import hello.jpabook.domain.OrderStatus;
import hello.jpabook.repository.OrderRepository;
import hello.jpabook.repository.OrderSearch;
import hello.jpabook.repository.OrderSimpleQueryDto;
import hello.jpabook.repository.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all=orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();//LAZY 강제 초기화(지연 쿼리)
        }
        return all;
        //Jackson Datatype Hibernate5 라이브러리가 필요하다
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
        //sql5번 나간다
    }
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        log.info("v3/simple-orders  시작");
        List<Order> order = orderRepository.findAllWithMemberDelivery();
        return order.stream()
                .map(o-> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        //v3는 결과는 같으나 최적화 된 sql이 나간다(sql문이 1나다)
    }

    private final OrderSimpleQueryRepository orderSimpleQueryRepository; //의존관계 주입

    //바로 dto로 조회 하기(dto가 이미 있어야 한다)(약간의 성능이 좋아지나 응용 불가)
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderSimpleQueryRepository.findOrderDtos();
    }
    @Data
    public class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
        }
    }
}
