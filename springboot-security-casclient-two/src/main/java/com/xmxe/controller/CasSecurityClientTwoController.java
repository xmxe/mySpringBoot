package com.xmxe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CasSecurityClientTwoController {
    // 访问http://localhost:9104/guest/hello 此时会自动跳转到 CAS Server 上登录，
    // 登录成功之后，经过spring security控制权限，如果没有使用guest用户登陆会提示403权限不足
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/guest/hello")
    public String user() {
        return "user";
    }
}
