package com.xmxe.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CasSecurityClientOneController {
    // 访问http://localhost:9103/user/hello ，此时会自动跳转到 CAS Server 上登录
    // 登录成功之后，经过spring security控制权限 如果没有使用admin登陆 就没有user角色访问权限 访问此接口会报403权限不足
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/user/hello")
    public String user() {
        return "user";
    }
}
