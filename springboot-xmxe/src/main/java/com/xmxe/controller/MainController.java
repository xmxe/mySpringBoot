package com.xmxe.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmxe.config.quartz.QuartzManager;
import com.xmxe.entity.User;
import com.xmxe.job.Jobs;
import com.xmxe.service.MainService;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
//@RequestMapping("**.do")
public class MainController {	
	Logger logger = Logger.getLogger(MainController.class);
	@Autowired
	MainService mainService;

	@Autowired
	QuartzManager quartManager;
	
    @Resource(name = "jobTrigger")  
    private CronTrigger cronTrigger;  

    @Resource(name = "scheduler")  
    private Scheduler scheduler;   
    
	//分页页面
	@RequestMapping("/pageView")
	public String pageView() {
		return "content/page";
	}
	
	//ztree页面
	@RequestMapping("/ztreeView")
	public String ztreeView() {
		return "content/ztree";
	}
	
	//form
	@RequestMapping("/form")
	public String form() {
		return "content/form";
	}

	@RequestMapping(value="/download")//问题:下载的文件乱码
	@ResponseBody
	public ResponseEntity<byte[]> down(HttpServletRequest request) throws Exception{
		String path = request.getParameter("path") == null ?null:request.getParameter("path");
		String filename = request.getParameter("filename") == null ?null:request.getParameter("filename");
		try {
			filename = new String(filename.getBytes("iso-8859-1"),"utf-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String downloadPath = request.getSession().getServletContext().getRealPath("/")+"/download/";

		File file = new File(downloadPath);
		if(!file.exists()){
			file.mkdirs();
		}
		//File f = File.createTempFile("sss", ".txt",file);
//		FtpClient ftpClient=Ftp.connect();
//		Ftp.downloadFile(ftpClient, path, downloadPath+filename);
//		Ftp.closeServer(ftpClient);
		File f = new File(downloadPath+filename);

		InputStream is = new FileInputStream(f);
		byte[] body = new byte[is.available()];
		is.read(body);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attchement;filename="+new String(filename.getBytes("utf-8"), "iso8859-1"));
//		headers.setContentDispositionFormData("attachment",filename);
//		byte[] body = FileUtils.readFileToByteArray(f);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		HttpStatus status = HttpStatus.OK;
		ResponseEntity<byte[]> entity = new ResponseEntity<>(body,headers,status);
		return entity;
	}

	@RequestMapping("/downLoadFromFTP")
	@ResponseBody
	public void downLoadFromFTP(HttpServletRequest request,HttpServletResponse response){
		String fileurl = request.getParameter("fileurl") == null ?null:request.getParameter("fileurl").toString();
		String name = request.getParameter("name") == null ?null:request.getParameter("name");
		try {
			name = new String(name.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedInputStream bis = null;
		BufferedOutputStream os = null;
		InputStream newinputstream = null;
		try{
			URL url = new URL(fileurl);
			URLConnection conn = url.openConnection();
			int filesize = conn.getContentLength(); // 取数据长度
			try{
				newinputstream = conn.getInputStream();
			}catch(FileNotFoundException e){
				newinputstream = null;
			}catch(Exception e){
				newinputstream = null;
			}
			if(newinputstream !=null){
				bis = new BufferedInputStream(conn.getInputStream());
				// 清空response
				response.reset();
				// 文件名称转换编码格式为utf-8,保证不出现乱码,这个文件名称用于浏览器的下载框中自动显示的文件名
				response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes("utf-8"), "iso8859-1"));
				response.addHeader("Content-Length", "" + filesize);
				os = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
				// 从输入流中读入字节流，然后写到文件中
				byte[] buffer = new byte[1024];
				int nRead;
				while ((nRead = bis.read(buffer, 0, 1024)) > 0) { // bis为网络输入流
					os.write(buffer, 0, nRead);
				}
			}
		}catch(FileNotFoundException fe){
			fe.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(bis !=null){
					bis.close();
				}
				if(os !=null){
					os.flush();
					os.close();
				}
				if(newinputstream !=null){
					newinputstream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//上传
	@RequestMapping("/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response) {
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

	@RequestMapping("/getUserById")
	@ResponseBody
	public JSONObject getUserById(@RequestParam(value = "username",required = false) String userId,
								  @RequestParam(value = "userId",defaultValue="1") String username) {
		//如果加了@RequestParam注解，那么请求url里必须包含这一参数，否则会报400。那么如果允许不传呢？有两种办法：1）使用default值2）使用required值
		User user = mainService.getUserById(Integer.valueOf(username));
		JSONObject json = new JSONObject();
		json.put("user",user);
		System.err.println(userId);
		System.err.println(username);
		return json;
	}

	//分页后台逻辑
	@RequiresRoles("a")//指定角色才可以执行的权限
	@GetMapping("/page")//相当于@RequestMapping(value="/page",method = RequestMethod.GET)
	public void page(HttpServletRequest request, HttpServletResponse response) {
		try{
			mainService.page(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//ztree
	@RequiresPermissions("user:add")//指定拥有此权限的才可以执行
	@RequestMapping(value="/dept/aJsonObject",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject aJsonObject(HttpServletRequest request) {
		JSONObject json = null;
		try{
			json = mainService.aJsonObject(request);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
	//验证码
	@RequestMapping(value="/code")
	public void generate(HttpServletRequest request,HttpServletResponse response){
		try{
			mainService.generate(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//校验登陆
	@RequestMapping(value="/loginCheck")
	@ResponseBody
	public JSONObject loginCheck(HttpServletRequest request) {
		JSONObject json = null;
		try{
			json = mainService.loginCheck(request);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	} 
	
	//excel
	@RequestMapping(value="/excel")
	@ResponseBody
	public void excel(HttpServletRequest request,HttpServletResponse response){
		try{
			mainService.excel(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}		
		
	}

	
	@ResponseBody
    @RequestMapping("/changeQuartz")
    public String quartzTest() throws SchedulerException{
         CronTrigger trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());  
         String currentCron = trigger.getCronExpression();// 当前Trigger使用的  
         System.err.println("当前trigger使用的-"+currentCron);
         //1秒钟执行一次
         CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/1 * * * * ?");  
         // 按新的cronExpression表达式重新构建trigger  
         trigger = (CronTrigger) scheduler.getTrigger(cronTrigger.getKey());  
         trigger = trigger.getTriggerBuilder().withIdentity(cronTrigger.getKey())  
                 .withSchedule(scheduleBuilder).build();  
         // 按新的trigger重新设置job执行  
         scheduler.rescheduleJob(cronTrigger.getKey(), trigger);  
        return "-这是quartz测试";
    }
	@ResponseBody
    @RequestMapping("/quartz")
	public JSONObject quartz(){
		JSONObject json = new JSONObject();
		try{
			//quartManager.startJobs();
			//quartManager.removeJob("scheduler", "scheduler_group", "myTigger", "group");
			quartManager.addJob("a", "group", "t", "tri", Jobs.class, "0/6 * * * * ?");
			quartManager.addJob("b", "group", "t1", "tri1", Jobs.class, "0/2 * * * * ?");
			json.put("msg", "quartz成功启动");
		}catch(Exception e){
			e.printStackTrace();
			json.put("msg", "quartz启动失败");
		}
		return json;
	}
	
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
