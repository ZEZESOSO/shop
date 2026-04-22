package com.example.shop.controller.members;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MembersController {
    @GetMapping("/members/login")
    public String login() {
        return "login";
    }
}
