package com.grocery.oopproject.web;

import com.grocery.oopproject.domain.Admin;
import com.grocery.oopproject.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("admins", adminService.findAll());
        return "admins/list";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "admins/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam(required = false) String roleName,
                           Model model) {
        try {
            adminService.register(name, email, password, roleName);
            return "redirect:/admins";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admins/register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admins/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model) {
        try {
            Admin a = adminService.login(email, password);
            model.addAttribute("admin", a);
            return "admins/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "admins/login";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("admin", adminService.find(id));
        return "admins/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id,
                         @RequestParam String name,
                         @RequestParam String email,
                         @RequestParam(required = false) String roleName,
                         Model model) {
        try {
            adminService.update(id, name, email, roleName);
            return "redirect:/admins";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("admin", adminService.find(id));
            return "admins/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        adminService.delete(id);
        return "redirect:/admins";
    }
}
