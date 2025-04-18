package com.example.spring.dropin.route.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
public class ContentRouter {

    @GetMapping
    public String main(Model model) {
        log.info("main");
        model.addAttribute("page", "home");
        return "main";
    }

    @GetMapping( "/home")
    public String home(Model model) {
        log.info("home");
        model.addAttribute("page", "home");
        return "main";    // base.html 렌더링
    }

    @GetMapping("/individual")
    public String profile(Model model) {
        log.info("individual");
        model.addAttribute("page", "individual");
        return "main";
    }

    @GetMapping("/group")
    public String team(Model model) {
        log.info("group");
        model.addAttribute("page", "group");
        return "main";
    }

}
