package hello.jpabook.service;

import hello.jpabook.domain.Address;
import hello.jpabook.domain.Member;
import hello.jpabook.domain.Order;
import hello.jpabook.domain.OrderStatus;
import hello.jpabook.domain.item.Book;
import hello.jpabook.domain.item.Item;
import hello.jpabook.exception.NotEnoughStockException;
import hello.jpabook.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMembeer();

        Book book = createBook("시골 jpa",10000, 10);
        int oderCount=2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), oderCount);
        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1,getOrder.getOrderItems().size(),"주문한 사품 종류 수가 정확해야한다.");
        assertEquals(10000*oderCount,getOrder.getTotalPrice(),"주문 가격은 가격*수량이다.");
        assertEquals(8,book.getStockQuantity(),"주문 수량만큰 재고가 줄어야한다.");
    }



    @Test//(expected=NotEnoughStockException.class)
    public void 재고수량초과() throws Exception{
        //given
        Member membeer = createMembeer();
        Item item=createBook("시골 jpa",10000, 10);
        int orderCount=11;
        //when

        //then
        NotEnoughStockException exception =
                assertThrows(NotEnoughStockException.class, () -> {
                    orderService.order(membeer.getId(),item.getId(),orderCount);
                    //최대 10개인데 11개로 에러
                });
        assertEquals("need more stock", exception.getMessage());
    }
    @Test
    public void 주문취소() throws Exception{
        //given
        Member membeer = createMembeer();
        Book item = createBook("cigol jpa", 10000, 10);
        int orderCount=2;
        Long orderId = orderService.order(membeer.getId(), item.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL,getOrder.getStatus(),"주문이 취소시 상태는 cencel 이다");
        assertEquals(10,item.getStockQuantity(),"주문이 취소된 상품은 그만큼 재고가 증가해야 한다");
    }
    @Test
    public void 재고수량추가() throws Exception{
        //given

        //when

        //then

    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMembeer() {
        Member member=new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        Item item=new Book();
        em.persist(member);
        return member;
    }
}