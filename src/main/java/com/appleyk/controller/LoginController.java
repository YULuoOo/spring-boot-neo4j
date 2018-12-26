package com.appleyk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String toUserLogin(){ return "login"; }

    @GetMapping("/register")
    public String toUserRegister(){ return "register";}

    @GetMapping("/personalpage")
    public String toUserPage(){ return "personalpage";}

    @GetMapping("/modifyInfo")
    public String toModifyPage(){return "modify_info";}

    @GetMapping("/homepage")
    public String toHomePage(){return  "homepage";}

    @GetMapping("/advance")
    public String toAdvance(){return  "advance";}
}
