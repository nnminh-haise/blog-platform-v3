package com.example.javaee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/login")
public class LoginController {
    @GetMapping("/")
    public String loginRoute() {
        return "login/login";
    }
}
