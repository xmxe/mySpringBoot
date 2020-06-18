package com.xmxe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xmxe.config.quartz.QuartzManager;
import com.xmxe.config.redis.RedisUtils;
import com.xmxe.entity.HttpResult;
import com.xmxe.entity.User;
import com.xmxe.job.Jobs;
import com.xmxe.service.MainService;
import com.xmxe.util.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;

@Controller
//@RequestMapping("**.do")
public class MainController {	
	Logger logger = Logger.getLogger(MainController.class);
	@Autowired
	MainService mainService;

	@Autowired
    private RedisUtils redisUtils;
	
	@Autowired
	QuartzManager quartManager;
	
	@Resource(name = "jobDetail")  
    private JobDetail jobDetail;	
	
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
	
	//上传
	@RequestMapping("/upload")
	public void upload(HttpServletRequest request, HttpServletResponse response) {
		try {
			mainService.upload(request,response);			
		}catch(Exception e) {
			e.printStackTrace();
		}
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
	
	@RequestMapping("/redis")
	@ResponseBody
    public String redis(){
        redisUtils.set("123", "hello world");
        System.err.println("进入了方法");
        String string= redisUtils.get("123").toString();
        return string;
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
	
	@ResponseBody
    @RequestMapping("/httpclient")
	public String httpclient() throws Exception {
		String url = "http://localhost:8080/zhongzhu/appnewhouse/newhouseLine.do";
		Map<String,Object> map = new HashMap<>();
		map.put("Id","6b340d4c17ed47449409f35a709ca298");//追加参数
		HttpClientUtil client = new HttpClientUtil();
		HttpResult result = client.doGet(url,map);
		//System.err.println(result.getBody());
		return result.getBody();
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
