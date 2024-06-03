package com.example.controller;

import com.example.model.User;
import com.example.security.CustomUserDetails;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CancelOrderController {

    private final UserService userService;

    @Autowired
    public CancelOrderController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cancelOrder")
    public String cancelOrder(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            userService.cancelOrder(user.getId());
            userDetails.getUser().setPurchased(false); // Güncel kullanıcı nesnesinin güncellenmesi
            model.addAttribute("user", userDetails);
        }
        return "redirect:/products";
    }
}
