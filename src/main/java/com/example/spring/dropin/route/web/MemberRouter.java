package com.example.spring.dropin.route.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberRouter {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/new")
    public String join() {
        return "join";
    }

}
