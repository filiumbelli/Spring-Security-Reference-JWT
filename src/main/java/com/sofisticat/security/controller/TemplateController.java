package com.sofisticat.security.controller;

import com.sofisticat.security.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TemplateController {

    @GetMapping
    @RequestMapping("/login")
    public String getLoginView() {
        return "login/login";
    }


    @GetMapping
    @RequestMapping("/courses")
    public String getCoursesView() {
        return "courses";
    }

}
