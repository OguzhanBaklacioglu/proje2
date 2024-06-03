package com.example.controller;

import com.example.model.User;
import com.example.security.CustomUserDetails;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PurchaseController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public PurchaseController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/purchase")
    public String purchaseProduct(@RequestParam Long productId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            userService.purchaseProduct(user.getId(), productId);
            userDetails.getUser().setPurchased(true); // Güncel kullanıcı nesnesinin güncellenmesi
            model.addAttribute("user", userDetails);
            model.addAttribute("product", productService.findById(productId));
            return "purchase"; // Purchase sayfasına yönlendirme
        }
        return "redirect:/login";
    }
    @PostMapping("/purchase")
    public String handlePurchase(@RequestParam("productId") Long productId, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userService.purchaseProduct(customUserDetails.getUser().getId(), productId);
        return "redirect:/products";
    }
}
