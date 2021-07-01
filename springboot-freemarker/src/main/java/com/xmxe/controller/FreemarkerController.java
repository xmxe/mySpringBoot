package com.xmxe.controller;

import com.xmxe.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FreemarkerController {
	
	@GetMapping("/freemarker")
    public String fm(Model model) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("username>>>>" + i);
            user.setPasswd("password>>>>" + i);
            users.add(user);
        }
        model.addAttribute("users", users);
        return "user";
	}

}
