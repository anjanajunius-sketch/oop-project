package com.grocery.oopproject.controller;

import com.grocery.oopproject.service.CartService;
import com.grocery.oopproject.service.CustomerService;
import com.grocery.oopproject.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;

    public CartController(CartService cartService,
                          CustomerService customerService,
                          ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @GetMapping("/{customerId}")
    public String view(@PathVariable String customerId, Model model) {
        var cart = cartService.find(customerId);
        model.addAttribute("cart", cart);
        model.addAttribute("customer", customerService.find(customerId));
        model.addAttribute("total", cart.calculateTotal());
        return "cart/view";
    }

    @GetMapping("/{customerId}/add")
    public String addForm(@PathVariable String customerId, Model model) {
        model.addAttribute("customerId", customerId);
        model.addAttribute("products", productService.findAll());
        return "cart/add";
    }

    @PostMapping("/{customerId}/add")
    public String add(@PathVariable String customerId,
                      @RequestParam String productId,
                      @RequestParam int qty,
                      Model model) {
        try {
            cartService.addItem(customerId, productId, qty);
            return "redirect:/cart/" + customerId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customerId", customerId);
            model.addAttribute("products", productService.findAll());
            return "cart/add";
        }
    }

    @PostMapping("/{customerId}/update")
    public String update(@PathVariable String customerId,
                         @RequestParam String productId,
                         @RequestParam int qty) {
        cartService.updateItemQuantity(customerId, productId, qty);
        return "redirect:/cart/" + customerId;
    }

    @PostMapping("/{customerId}/remove")
    public String remove(@PathVariable String customerId,
                         @RequestParam String productId) {
        cartService.removeItem(customerId, productId);
        return "redirect:/cart/" + customerId;
    }

    @PostMapping("/{customerId}/clear")
    public String clear(@PathVariable String customerId) {
        cartService.clearCart(customerId);
        return "redirect:/cart/" + customerId;
    }
}
