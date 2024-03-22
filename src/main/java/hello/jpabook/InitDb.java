package hello.jpabook;

import hello.jpabook.domain.*;
import hello.jpabook.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;
    @PostConstruct//스프링웹 시작시 실행
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }
    @Transactional
    @Component
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        public  void dbInit1(){
            Member member=new Member();
            member.setName("userA");
            member.setAddress(new Address("jeju","1","1111"));
            em.persist(member);//유저 등록

            Book book=new Book();
            book.setName("jpa1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);//상품 등록

            Book book2=new Book();
            book2.setName("jpa2");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 1000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 2000, 1);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            //...orderItem 으로 배열 값으로 보네기
            em.persist(order);//상품 주문
        }
        public  void dbInit2(){
            Member member=new Member();
            member.setName("userA2");
            member.setAddress(new Address("jeju2","12","11112"));
            em.persist(member);

            Book book=new Book();
            book.setName("jpa12");
            book.setPrice(100000);
            book.setStockQuantity(1000);
            em.persist(book);

            Book book2=new Book();
            book2.setName("jpa22");
            book2.setPrice(200000);
            book2.setStockQuantity(1000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 1);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            //...orderItem 으로 배열 값으로 보네기
            em.persist(order);
        }
    }
}
