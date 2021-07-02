package com.xmxe.service;

import com.xmxe.util.SendMailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;


@Service
public class MailService {

	@Autowired
	SendMailUtil sendMail;

	public void sendMail() {
		String 	to = "464817304@qq.com",
				subject="testMail",				
				context="测试邮件",				
				htmlContext = "<html>\n" +
				               "<body>\n" +
				               "<h3>hello world</h3>\n" +
				               "<h1>html</h1>\n" +
				               "<body>\n" +
				               "</html>\n",				                
				filePath="C:\\Users\\wangx\\Pictures\\Saved Pictures\\k.jpg",
				filePath2="C:\\Users\\wangx\\Pictures\\Saved Pictures\\88.jpg",
				p01="p01",p02="p02",
				staticContext="<p>hello 大家好，这是一封测试邮件，这封邮件包含两种图片，分别如下</p><p>第一张图片：</p><img src='cid:"+p01+"'/><p>第二张图片：</p><img src='cid:"+p02+"'/>";
				String[] p = {p01,p02},file= {filePath,filePath2};
			    
		try {
			//sendMail.sendSimpleMail(to,subject,context);
			//sendMail.sendHtmlMail(to,subject,htmlContext);
			//sendMail.sendAttachmentsMail(to, subject, context, filePath);
			//sendMail.sendImgResMail(to, subject, staticContext, p, file);
			
			/*User user = new User();
			user.setUsername("mmmm");
			user.setId(1);
			user.setPasswd("pass");
			Map<String,Object> map = new HashMap<>();
			List<User> userList = new ArrayList<>();
			userList.add(user);
			map.put("users", userList);
			sendMail.sendFreemarkerMail(to, subject, map);*/
			
			
			Context con = new Context();
		    con.setVariable("username", "mmm");
		    con.setVariable("num","000001");
		    con.setVariable("salary", "99999");
		    sendMail.sendThymeleafMail(to, subject, con);
		    
			System.out.println("发送完毕！");
		} catch (Exception e) {			
			e.printStackTrace();
		}
				
	}
}
