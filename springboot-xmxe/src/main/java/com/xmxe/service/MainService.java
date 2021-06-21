package com.xmxe.service;

import com.alibaba.fastjson.JSONObject;
import com.wf.captcha.utils.CaptchaUtil;
import com.xmxe.comonent.InvokeMethod;
import com.xmxe.config.aop.AopAction;
import com.xmxe.entity.Book;
import com.xmxe.entity.Dept;
import com.xmxe.entity.Page;
import com.xmxe.entity.User;
import com.xmxe.mapper.master.MasterMapper;
import com.xmxe.mapper.slave.SlaveMapper;
import com.xmxe.util.SendMailUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;


@Service
public class MainService {

	@Resource
	MasterMapper masterMapper;
	
	@Resource
	SlaveMapper slaveMapper;
	
	@Autowired
	SendMailUtil sendMail;
	
	@AopAction//日志注解
	public User getUserById(Integer userId) {
		//int a = 1/0;//测试aop @AfterThrowing
		Map<String,Object> user = slaveMapper.getUserById(userId);
		System.out.println("slaveDB------"+user);
		return masterMapper.getUserById(userId);
	}

	
	public void page(HttpServletRequest request, HttpServletResponse response){
		try{
			//1.获得当前页参数
			request.setCharacterEncoding("UTF-8");
			String tj = request.getParameter("tj");
			int currentPage = Integer.parseInt(request.getParameter("currentPage"));
			Page<Book> page = new Page<Book>();
			//设置当前页 currentPage next pre  pageSize  start
			page.setCurrentPage(currentPage);
			//2.查询总记录数 total   pageCount
			int total = masterMapper.queryUserCount(tj);
			page.setTotal(total);
			//PageHelper.startPage(2, 3);//分页插件
			//3.查询数据 rows
			List<Book> rows = masterMapper.querySome(tj,page.getStart(),page.getPageSize());
			page.setRows(rows);
			//~~~~~~~~~~组装page对象 完毕 写出到客户端
			response.setContentType("text/plain;charset=UTF-8");
			//JSONObject jo = JSONObject.fromObject(page);
			JSONObject jo = new JSONObject();
			jo.put("jo", page);
			PrintWriter out = response.getWriter();
			out.print(jo);
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public JSONObject aJsonObject(HttpServletRequest request) {
		List<Dept> depts = masterMapper.findDept();
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map = new HashMap<String,Object>();
		for (int i = 0; i < depts.size(); i++) {
			Map<String,Object> newMap = new HashMap<String,Object>();
			newMap.put("id",depts.get(i).getId());
			newMap.put("pId", depts.get(i).getFather_id());
			newMap.put("name", depts.get(i).getDept_name());
			newMap.put("sort", depts.get(i).getSort());
			newMap.put("dept_level", depts.get(i).getDept_level());
			if (depts.get(i).getDept_level() == 1) {
				newMap.put("open", true);
				newMap.put("iconSkin", "pIcon01");
			}else if (depts.get(i).getDept_level() == 2) {
				newMap.put("iconSkin", "icon02");
			}else {
				newMap.put("iconSkin", "icon03");
			}
			list.add(newMap);
		}
		map.put("zNodes", list);
		//JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
		JSONObject json = (JSONObject) JSONObject.toJSON(map);
		return json;
	}
	
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

	@InvokeMethod(param="1111")// 启动spring自动执行的方法
	public char randomChar(){
		Random r = new Random();
		String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
		return s.charAt(r.nextInt(s.length()));
	}
	
	public JSONObject loginCheck(HttpServletRequest request) {
		String code = request.getParameter("code");
		String name =request.getParameter("username");
		String password = request.getParameter("password");
		String rememberMe = request.getParameter("rememberMe");
		String codeSession = (String) request.getSession().getAttribute("code");
		JSONObject json = new JSONObject();
		
		if(code == null || "".equals(code)) {
			json.put("message", "请输入验证码");
			return json;
		}
		if (CaptchaUtil.ver(code, request)) {//使用工具类校验验证码

//		if(code.equalsIgnoreCase(codeSession)) {//普通方法校验验证码
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
			CaptchaUtil.clear(request);  // 清除session中的验证码
			json.put("message", "验证码不正确");
		}
		
		return json;
	}
	
	public void excel(HttpServletRequest request,HttpServletResponse response){
		String[] handers = {"id","书名","作者","价格"};
		List<Book> list = masterMapper.querySome(null,1,5);
		try{
			String filedisplay = "test.xlsx";
			filedisplay = URLEncoder.encode(filedisplay, "UTF-8");			
			//由浏览器指定下载路径
			//response.reset();			
			//response.setContentType("application/x-download");
			//response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.addHeader("Content-Disposition", "attachment;filename="+ filedisplay);
			request.setCharacterEncoding("UTF-8");
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Dispostion","attachment;filename=".concat(filedisplay));
				
			HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
			HSSFSheet sheet = wb.createSheet("操作");//第一个sheet
			HSSFRow rowFirst = sheet.createRow(0);//第一个sheet第一行为标题
			rowFirst.setHeight((short) 500);			   
            HSSFCellStyle cellStyle = wb.createCellStyle();// 创建单元格样式对象  
            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			for (int i = 0; i < handers.length; i++) {
			   sheet.setColumnWidth(i, 4000);// 设置列宽
			}
			//写标题了
			 for (int i = 0; i < handers.length; i++) {
				 //获取第一行的每一个单元格
				 HSSFCell cell = rowFirst.createCell(i);
				 //往单元格里面写入值
				 cell.setCellValue(handers[i]);
				 cell.setCellStyle(cellStyle);
			 }
			for (int i = 0; i < list.size(); i++) {
				Book u = list.get(i);			
				//创建数据行
				HSSFRow row = sheet.createRow(i + 1);				
				row.setHeight((short) 400);   // 设置每行的高度
				//设置对应单元格的值
				row.createCell(0).setCellValue(u.getId());row.getCell(0).setCellStyle(cellStyle);
				row.createCell(1).setCellValue(u.getBookname());row.getCell(1).setCellStyle(cellStyle);
				row.createCell(2).setCellValue(u.getBookauthor());row.getCell(2).setCellStyle(cellStyle);
				row.createCell(3).setCellValue(u.getBookprice());row.getCell(3).setCellStyle(cellStyle);
				
			}
			OutputStream os = response.getOutputStream();  
            wb.write(os);
            os.close();
            wb.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
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
