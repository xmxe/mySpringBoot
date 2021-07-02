package com.xmxe.comonent;

import com.xmxe.entity.ResultInfo;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局异常定义,属性编辑器定义（RequestMapping参数字符串自动转Date）,全局ModelAttribute
 */
@RestControllerAdvice
public class CustomGlobal {

    @ExceptionHandler(Exception.class)
    public ResultInfo<String> excetionHand(Exception e){
        return new ResultInfo<>(110,"出错了",e.getMessage());
    }

    /**
     * 应用到所有被@RequestMapping注解的方法，在其执行之前初始化数据绑定器
     */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // allowEmpty = true 允许date字段为null
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);
        webDataBinder.registerCustomEditor(Date.class, dateEditor);
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     */
    @ModelAttribute
    public void addModelAttribute(Model model){
        model.addAttribute("global","customGolbal");
    }

}
