package com.grocery.oopproject.web;

import com.grocery.oopproject.service.CustomerService;
import com.grocery.oopproject.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @GetMapping
    public String listAll(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "orders/list";
    }

    @GetMapping("/customer/{customerId}")
    public String listByCustomer(@PathVariable String customerId, Model model) {
        model.addAttribute("orders", orderService.findByCustomer(customerId));
        model.addAttribute("customer", customerService.find(customerId));
        return "orders/list";
    }

    @GetMapping("/place/{customerId}")
    public String placeForm(@PathVariable String customerId, Model model) {
        model.addAttribute("customer", customerService.find(customerId));
        return "orders/place";
    }

    @PostMapping("/place/{customerId}")
    public String place(@PathVariable String customerId, Model model) {
        try {
            var order = orderService.placeOrder(customerId);
            return "redirect:/orders/" + order.getOrderId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customerService.find(customerId));
            return "orders/place";
        }
    }

    @GetMapping("/{orderId}")
    public String summary(@PathVariable String orderId, Model model) {
        model.addAttribute("order", orderService.find(orderId));
        return "orders/summary";
    }

    @PostMapping("/{orderId}/update")
    public String update(@PathVariable String orderId,
                         @RequestParam String date) {
        orderService.updateDate(orderId, date);
        return "redirect:/orders/" + orderId;
    }

    @PostMapping("/{orderId}/delete")
    public String delete(@PathVariable String orderId) {
        orderService.delete(orderId);
        return "redirect:/orders";
    }
}
