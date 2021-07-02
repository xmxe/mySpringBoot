package com.xmxe.controller;

import com.xmxe.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MailController {
	@Autowired
	MailService mailService;

	@GetMapping("mail")
	public void sendMail(){
		mailService.sendMail();
	}
}
