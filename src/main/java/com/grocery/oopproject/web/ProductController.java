package com.grocery.oopproject.web;

import com.grocery.oopproject.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/add")
    public String addForm() {
        return "products/add";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name,
                      @RequestParam double price,
                      @RequestParam int quantity,
                      Model model) {
        try {
            productService.addProduct(name, price, quantity);
            return "redirect:/products";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "products/add";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("product", productService.find(id));
        return "products/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable String id,
                       @RequestParam String name,
                       @RequestParam double price,
                       @RequestParam int quantity,
                       Model model) {
        try {
            productService.updateProduct(id, name, price, quantity);
            return "redirect:/products";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", productService.find(id));
            return "products/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
