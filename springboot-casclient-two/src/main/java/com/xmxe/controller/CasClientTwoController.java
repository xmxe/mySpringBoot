package com.xmxe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class CasClientTwoController {
    @RequestMapping("/c2")
    @ResponseBody
    public String c1(){
        return "success-c2";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:http://192.168.236.131:9100/cas/logout?service=http://127.0.0.1:9102/index";
    }
    @RequestMapping("/index")
    @ResponseBody
    public String index(){
        return "退出成功";
    }
}

