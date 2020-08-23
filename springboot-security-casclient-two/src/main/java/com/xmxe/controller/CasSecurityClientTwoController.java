package com.xmxe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CasSecurityClientTwoController {
    //访问http://localhost:9104/user/hello 访问 hello 接口，此时会自动跳转到 CAS Server 上登录，登录成功之后，经过两个重定向，会重新回到 hello 接口。
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
    @GetMapping("/user/hello")
    public String user() {
        return "user";
    }
}
