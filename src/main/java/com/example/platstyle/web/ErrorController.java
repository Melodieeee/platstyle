package com.example.platstyle.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ErrorController {
    @GetMapping(path = "/error/403")
    public String orders(){ return "error/403";}
}
