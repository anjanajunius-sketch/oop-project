package com.grocery.oopproject.controller;

import com.grocery.oopproject.model.Customer;
import com.grocery.oopproject.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "customers/list";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "customers/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String address,
                           @RequestParam(required = false) String phone,
                           Model model) {
        try {
            customerService.register(name, email, password, address, phone);
            return "redirect:/customers";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "customers/register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "customers/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        try {
            Customer c = customerService.login(email, password);
            model.addAttribute("customer", c);
            return "customers/profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "customers/login";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("customer", customerService.find(id));
        return "customers/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id,
                         @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam(required = false) String address,
                         @RequestParam(required = false) String phone,
                         Model model) {
        try {
            customerService.update(id, name, email, address, phone);
            return "redirect:/customers";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("customer", customerService.find(id));
            return "customers/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        customerService.delete(id);
        return "redirect:/customers";
    }
}
