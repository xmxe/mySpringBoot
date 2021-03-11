package com.xmxe.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmxe.util.QuartzService;
import com.xmxe.job.QuartzJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class JobController {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    QuartzService quartzService;

    /**
     * 从数据库获取所有的调度实例
     * @return
     */
    @RequestMapping("/quartz")
    @ResponseBody
    public JSONObject quartz(){
        JSONObject json = new JSONObject();
        try{
            List<Map<String, Object>> jobList = quartzService.queryAllJob();
            jobList.forEach(m -> logger.info("数据库的所有调度：{}",m.values()));
            json.put("msg", jobList);
        }catch(Exception e){
            e.printStackTrace();
            json.put("msg", e.getMessage());
        }
        return json;
    }

    /**
     * 添加调度任务
     */
    @RequestMapping("/quartz_add")
    @ResponseBody
    public void quartz_add(){
        Map<String,String> map = Map.of("k","v");
        quartzService.addJob(QuartzJob.class,"jobname1","groupname1","0/5 * * * * ?",map);
    }

    /**删除调度任务
     *
     */
    @RequestMapping("/quartz_del")
    @ResponseBody
    public void quartz_del(){
        quartzService.deleteJob("jobname1","groupname1");
    }
}
