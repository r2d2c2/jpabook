package hello.jpabook.controller;

import hello.jpabook.domain.Member;
import hello.jpabook.domain.Order;
import hello.jpabook.domain.item.Item;
import hello.jpabook.repository.OrderSearch;
import hello.jpabook.service.ItemService;
import hello.jpabook.service.MemberService;
import hello.jpabook.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();
        model.addAttribute("members",members);
        model.addAttribute("items",items);
        return "order/orderForm";
    }
    @PostMapping("/order")
    public String order(@RequestParam("memberId")Long memberId,
                        @RequestParam("itemId")Long itemId,@RequestParam("count") int count){
        log.info("삼품 주문 저장");
        orderService.order(memberId, itemId, count);
        log.info("상품 저장 끝");
        return "redirect:/orders";
    }
    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);
        return "order/orderList";
    }
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
