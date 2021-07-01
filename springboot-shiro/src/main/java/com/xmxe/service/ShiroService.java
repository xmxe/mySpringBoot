package com.xmxe.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class ShiroService {
	
	public void generate(HttpServletRequest request,HttpServletResponse response){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String code = drawImg(output);
		request.getSession().setAttribute("code", code);
		try {
			ServletOutputStream out = response.getOutputStream();
			output.writeTo(out);
			out.close();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	private String drawImg(ByteArrayOutputStream output){
		String code = "";
		for(int i=0; i < 4; i++){
			code += randomChar();
		}
		int width = 70;
		int height = 25;
		BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
		Font font = new Font("Times New Roman",Font.PLAIN,20);
		Graphics2D g = bi.createGraphics();
		g.setFont(font);
		Color color = new Color(66,2,82);
		g.setColor(color);
		g.setBackground(new Color(226,226,240));
		g.clearRect(0, 0, width, height);
		FontRenderContext context = g.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(code, context);
		double x = (width - bounds.getWidth()) / 2;
		double y = (height - bounds.getHeight()) / 2;
		double ascent = bounds.getY();
		double baseY = y - ascent;
		g.drawString(code, (int)x, (int)baseY);
		g.dispose();
		try {
			ImageIO.write(bi, "jpg", output);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return code;
	}

	public char randomChar(){
		Random r = new Random();
		String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
		return s.charAt(r.nextInt(s.length()));
	}
	
	public Map<String, Object> loginCheck(HttpServletRequest request) {
		String code = request.getParameter("code");
		String name =request.getParameter("username");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		String codeSession = (String) request.getSession().getAttribute("code");
		Map<String, Object> json = new HashMap<String, Object>();
		
		if(code == null || "".equals(code)) {
			json.put("message", "请输入验证码");
			return json;
		}

		if(code.equalsIgnoreCase(codeSession)) {//普通方法校验验证码
			Map<String,String> map = new HashMap<>();
			map.put("username", name);map.put("password", password);
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(name, password);
			usernamePasswordToken.setRememberMe(rememberMe == null ? false : true);
			try{
				//通过login方法进入Myrealm验证用户和权限
				subject.login(usernamePasswordToken);
				subject.getSession(true).setAttribute("user",map);
//				request.getSession().setAttribute("user",map);
				json.put("message", "success");
			}catch(UnknownAccountException e){
				json.put("message", "账号不存在");
			} catch(Exception e){
				json.put("message", "密码错误");
			}
		}else {
			json.put("message", "验证码不正确");
		}
		
		return json;
	}
}
