package com.xmxe.util;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class SendMailUtil {

	 @Value("${spring.mail.username}")
	 private String from;
	 
	 @Autowired
	 private JavaMailSender javaMailSender;

	 @Autowired
	 TemplateEngine templateEngine;
	 
	 /**
	  * 简单文本邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param contnet 邮件内容
     */
    public void sendSimpleMail(String to, String subject, String context){
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setSubject(subject);//邮件主题
	    message.setFrom(from);//邮件发送者
	    message.setTo(to);//邮件接收者，可以多个
	    //message.setCc("37xxxxx37@qq.com");抄送人，可以多个
	    //message.setBcc("14xxxxx098@qq.com");隐秘抄送人，可以多个
	    message.setSentDate(new Date());
	    message.setText(context);
	    javaMailSender.send(message);
	}
	
    
    /**
     * HTML 文本邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param htmlContext HTML内容
     * @throws MessagingException
     */
    public void sendHtmlMail(String to, String subject, String htmlContext) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContext, true);
        helper.setFrom(from);

        javaMailSender.send(message);
    }
    
    /**
	 *	附件邮件
     * @param to 接收者邮件
     * @param subject 邮件主题
     * @param context 内容
     * @param filePath 附件路径
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String to, String subject, String context,
                                    String filePath) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(context, true);
        helper.setFrom(from);

        FileSystemResource file = new FileSystemResource(new File(filePath));
        String fileName = file.getFilename();
        helper.addAttachment(fileName, file);

        javaMailSender.send(message);
    }

	
	/**
	 * 	发送静态资源文件
	 * @param to 接收者邮件
     * @param subject 邮件主题
     * @param staticContext 内容
     * @param parmArray 参数集合
     * @param filePath 路径集合
	 * @throws MessagingException
	 */
	
	public void sendImgResMail(String to, String subject, String staticContext,
            String[] parmArray,String[] filePath) throws MessagingException {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	    
	    helper.setSubject(subject);
	    helper.setFrom(from);
	    helper.setTo(to);
	    helper.setSentDate(new Date());
	    helper.setText(staticContext,true);
	    if(parmArray.length > 0) {
	    	for(int i = 0;i < parmArray.length; i++) {
	    		helper.addInline(parmArray[i],new FileSystemResource(new File(filePath[i])));
	    	}
	    }else {
	    	helper.addInline(parmArray[0],new FileSystemResource(new File(filePath[0])));
		    
	    }
	    
	    javaMailSender.send(mimeMessage);
	}
	
	/**
	 * freemarker邮件模板
	 * @param to 接收者邮件
     * @param subject 邮件主题
     * @param dataModel  
	 * @throws Exception
	 * 
	 */	
	public void sendFreemarkerMail(String to, String subject,Object dataModel) throws Exception {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	    
	    helper.setSubject(subject);
	    helper.setFrom(from);
	    helper.setTo(to);

	    helper.setSentDate(new Date());
	    //构建 Freemarker 的基本配置
	    Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
	    // 配置模板位置
	    ClassLoader loader = SendMailUtil.class.getClassLoader();
	    configuration.setClassLoaderForTemplateLoading(loader, "freemarker");
	    //加载模板
	    Template template = configuration.getTemplate("user.ftl");
	    StringWriter out = new StringWriter();
	    //模板渲染，渲染的结果将被保存到 out 中 ，将out 中的 html 字符串发送即可
	    template.process(dataModel, out);
	    helper.setText(out.toString(),true);
	    javaMailSender.send(mimeMessage);
	}
	
	/**
	 * thymeleaf邮件模板
	 * @param to 接收者邮件
     * @param subject 邮件主题
     * @param context 内容
	 * @throws MessagingException
	 */	
	public void sendThymeleafMail(String to, String subject,Context context) throws MessagingException {
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	    helper.setSubject(subject);
	    helper.setFrom(from);
	    helper.setTo(to);
	    helper.setSentDate(new Date());
	    	   
	    String process = templateEngine.process("mail.html", context);
	    helper.setText(process,true);
	    javaMailSender.send(mimeMessage);
	}
}
