package com.grocery.oopproject.controller;

import com.grocery.oopproject.service.CustomerService;
import com.grocery.oopproject.service.ProductService;
import com.grocery.oopproject.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ProductService productService;
    private final CustomerService customerService;

    public ReviewController(ReviewService reviewService,
                            ProductService productService,
                            CustomerService customerService) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        return "reviews/list";
    }

    @GetMapping("/product/{productId}")
    public String listByProduct(@PathVariable String productId, Model model) {
        model.addAttribute("reviews", reviewService.findByProduct(productId));
        model.addAttribute("product", productService.find(productId));
        return "reviews/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "reviews/add";
    }

    @PostMapping("/add")
    public String add(@RequestParam String customerId,
                      @RequestParam String productId,
                      @RequestParam int rating,
                      @RequestParam(required = false) String comment,
                      Model model) {
        try {
            reviewService.addReview(customerId, productId, rating, comment);
            return "redirect:/reviews";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productService.findAll());
            model.addAttribute("customers", customerService.findAll());
            return "reviews/add";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("review", reviewService.find(id));
        return "reviews/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable String id,
                       @RequestParam int rating,
                       @RequestParam(required = false) String comment) {
        reviewService.update(id, rating, comment);
        return "redirect:/reviews";
    }

    @GetMapping("/moderate")
    public String moderateList(Model model) {
        model.addAttribute("reviews", reviewService.findAll());
        return "reviews/moderate";
    }

    @PostMapping("/{id}/moderate")
    public String moderate(@PathVariable String id,
                           @RequestParam(defaultValue = "true") boolean approve) {
        reviewService.moderate(id, approve);
        return "redirect:/reviews/moderate";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        reviewService.delete(id);
        return "redirect:/reviews";
    }
}
