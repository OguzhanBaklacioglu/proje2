package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index"; // Tanıtım sayfasını döner
    }

    @GetMapping("/home/profile")
    public String profile(Model model, Principal principal) {
        // Kullanıcı bilgilerini model'e ekleyebilirsiniz
        model.addAttribute("username", principal.getName());
        return "profile";
    }
}
