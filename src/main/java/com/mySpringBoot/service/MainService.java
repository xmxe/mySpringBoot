package com.mySpringBoot.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.mySpringBoot.dao.MainDao;
import com.mySpringBoot.entity.Book;
import com.mySpringBoot.entity.Dept;
import com.mySpringBoot.entity.User;
import com.mySpringBoot.util.Page;


@Service
public class MainService {

	@Autowired
	MainDao mainDao;
	
	public User getUserById(Integer userId) {
		return mainDao.getUserById(userId);
	}
	
	public void upload(HttpServletRequest request, HttpServletResponse response){
		try{
			if (request instanceof MultipartHttpServletRequest) {
				MultipartFile file =  ((MultipartHttpServletRequest) request).getFile("fileName");//文件名
				if(file != null) {
					SimpleDateFormat sff = new SimpleDateFormat("yyyyMMdd");//20180101
					String today = sff.format(new Date());
					File f = new File(request.getSession().getServletContext().getRealPath(File.separator) + today);//tomcat webapps路径+项目名 例:E:\apache-tomcat-7.0.82\webapps\club
					if(!f.exists()) {f.mkdirs();}
					String fileName = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());//自定义文件名
					/*①							
					file.transferTo(new File(f,fileName.replace("-", "")));
					*/			
					 /*②
					 FileUtils.writeByteArrayToFile(new File(f,fileName.replace("-", "")), file.getBytes());
					 */
					InputStream is = (FileInputStream) file.getInputStream();
					OutputStream os = new FileOutputStream(new File(f,fileName.replace("-", "")));
					byte[] data = new byte[1024];
					int len;
					while((len = is.read(data)) != -1) {
						os.write(data, 0, len);
					}
					is.close();
					os.flush();
					os.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
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
			int total = mainDao.queryUserCount(tj);
			page.setTotal(total);
			//PageHelper.startPage(2, 3);//分页插件
			//3.查询数据 rows
			List<Book> rows = mainDao.querySome(tj,page.getStart(),page.getPageSize());
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
		List<Dept> depts = mainDao.findDept();
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
	
	private char randomChar(){
		Random r = new Random();
		String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
		return s.charAt(r.nextInt(s.length()));
	}
	
	public JSONObject loginCheck(HttpServletRequest request) {
		String code = request.getParameter("code");
		String name =request.getParameter("username");
		String password = request.getParameter("password");
		String codeSession = (String) request.getSession().getAttribute("code");
		JSONObject json = new JSONObject();
		Map<String,String> map = new HashMap<>();
		map.put("username", "1");map.put("password", "1");
		request.getSession().setAttribute("user",map);
		if(code == null || "".equals(code)) {
			json.put("message", "请输入验证码");
			return json;
		}
		if(code.equalsIgnoreCase(codeSession)) {
			if(!("1").equals(name) || !("1").equals(password)) {
				json.put("message", "用户名或密码不正确");
			}else {
				SecurityUtils.getSubject().login(new UsernamePasswordToken(name, password));
				json.put("message", "success");
			}		
		}else {
			json.put("message", "验证码不正确");
		}
		
		return json;
	}
	
	public void excel(HttpServletRequest request,HttpServletResponse response){
		String[] handers = {"id","书名","作者","价格"};
		List<Book> list = mainDao.querySome(null,1,5);	
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
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
				row.createCell(0).setCellValue(u.getId());
				row.createCell(1).setCellValue(u.getBookname());
				row.createCell(2).setCellValue(u.getBookauthor());
				row.createCell(3).setCellValue(u.getBookprice());						
			}
			OutputStream os = response.getOutputStream();  
            wb.write(os);
            os.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

}
