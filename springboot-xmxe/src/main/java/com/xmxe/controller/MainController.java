package com.xmxe.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmxe.entity.ResultInfo;
import com.xmxe.entity.Book;
import com.xmxe.entity.User;
import com.xmxe.service.MainService;
import io.swagger.annotations.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@Api(tags = "订单模块")
//@RequestMapping("**.do")
public class MainController {	
	@Autowired
	MainService mainService;

	// thymeleaf只能被控制器访问
	@RequestMapping("/pageView")
	public String pageView() {
		return "content/page";
	}

	@RequestMapping("/ztreeView")
	public String ztreeView() {
		return "content/ztree";
	}

	@RequestMapping("/form")
	public String form() {
		return "content/form";
	}

	@RequestMapping(value="/global")//ip:port/global?date=2020-12-12 12:12:12
	@ResponseBody
	public void customGlobal(Date date,Model model){
		try{
			System.out.println(date);
			System.out.println(model.getAttribute("global"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequestMapping(value="/download")
	@ResponseBody
	public ResponseEntity<byte[]> down(HttpServletRequest request) throws Exception{
		String path = request.getParameter("path") == null ?null:request.getParameter("path");
		String filename = request.getParameter("filename") == null ?null:request.getParameter("filename");
		try {
			//filename = new String(filename.getBytes("ISO8859-1"),"UTF-8");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String downloadPath = request.getSession().getServletContext().getRealPath("/")+"/download/";

		File file = new File(downloadPath);
		if(!file.exists()){
			file.mkdirs();
		}
//		FtpClient ftpClient=Ftp.connect();
//		Ftp.downloadFile(ftpClient, path, downloadPath+filename);
//		Ftp.closeServer(ftpClient);
//		File f = new File(downloadPath+filename);
		File localFile = new File("C:\\E\\file123\\网源文档\\R模式负荷变化率.docx");
		InputStream is = new FileInputStream(localFile);
		byte[] body = new byte[is.available()];
//		byte[] body = FileUtils.readFileToByteArray(f);
		is.read(body);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attchement;filename="+new String(filename.getBytes("UTF-8"), "ISO8859-1"));
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentLength(body.length);
		HttpStatus status = HttpStatus.OK;
		is.close();
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
	/**
	 * 批量下载 转zip
	 * @param name js生成form表单提交的参数name
	 * @param code js生成form表单提交的参数code
	 * @param ts js生成form表单提交的参数ts
	 * @param response 返回响应
	 * @throws Exception
	 */
	@RequestMapping("batchDown")
	public void batchExcel(String[] name,String[] code,String[] ts,HttpServletResponse response) throws Exception{
		if(name!=null && name.length > 0){
			// 创建临时压缩文件 浏览器下载的就是这个文件
			File zipFile = File.createTempFile("zip", ".zip");
			// 当文件不存在时创建成功返回true
//			boolean iscreated = zipFile.createNewFile();
			FileOutputStream out = new FileOutputStream(zipFile);
			// 创建zip流 等待写入
			ZipOutputStream zipout = new ZipOutputStream(out);

			for(int i = 0;i < name.length;i++){
				File fileTemp = null;
				try {
					fileTemp = File.createTempFile(name[i], ".xlsx");
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 将内容写入excel 执行完后fileTemp已经是真正的excel
				creatExcel(fileTemp,code[i],ts[i]);
				// 将excel文件添加到zip
				zipFile(fileTemp,zipout);
			}
			zipout.close();
			out.close();
			response.reset();
			// 下载zip
			InputStream fis = new BufferedInputStream(new FileInputStream(zipFile));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			String filedisplay = name[0];
			filedisplay = URLEncoder.encode(filedisplay + "等数据.zip", "UTF-8");
			response.addHeader("Content-dispostion", "attachment;filename="+filedisplay);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("APPLICATION/OCTET-STREAM");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(filedisplay));
			OutputStream os = new BufferedOutputStream(response.getOutputStream());
			os.write(buffer);
			os.close();
			zipFile.delete();
		}
	}

	/**
	 *
	 * @param file 将要添加进压缩包的excel文件
	 * @param outputStream zip压缩流
	 * @throws Exception
	 */
	public void zipFile(File file,ZipOutputStream outputStream) throws Exception{
		FileInputStream IN = new FileInputStream(file);
		BufferedInputStream buffer = new BufferedInputStream(IN,1024);
		ZipEntry entry = new ZipEntry(file.getName());
		// 将文件放入压缩包
		outputStream.putNextEntry(entry);
		int number;
		byte[] bytes = new byte[1024];
		while((number = buffer.read(bytes)) != -1){
			outputStream.write(bytes,0,number);
		}
		buffer.close();
		IN.close();
	}

	/**
	 * 将内容写入到excel
	 * @param file 创建的临时excel文件 无内容
	 * @param code
	 * @param ts
	 */
	public void creatExcel(File file,String code,String ts){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// 模拟扰动时间前15s
		Date rdsj_before = new Date();
		// 模拟扰动时间后25s
		Date rdsj_after = new Date();
		try{
			Date ts_date = sf.parse(ts);
//			rdsj_before = FormatCalendar.offsetTime(ts_date, 1, -15);
//			rdsj_after = FormatCalendar.offsetTime(ts_date, 1, 25);
		}catch(Exception e){
			e.printStackTrace();
		}
		// pss输出信号
		String pss_out_tag = "PMU."+code+"_GA000U0010R";
		// 有功功率
		String yg_tag = "PMU."+code+"_UB000W0001R";
		try{
//			Map<String,List<LightModel<Object>>> allData = HbaseRead.readRawValuesModels(rdsj_before, rdsj_after, new String[]{pss_out_tag,yg_tag});
//			List<LightModel<Object>> pss_out_list = allData.get(pss_out_tag);
			List<Object> pss_out_list = new ArrayList<>();
//			List<LightModel<Object>> yg_list = allData.get(yg_tag);
			List<Object> yg_list = new ArrayList<>();
			int pss_out_length = 0;
			if(pss_out_list != null && pss_out_list.size() > 0){
				pss_out_length = pss_out_list.size();
			}
			int yg_length = 0;
			if(yg_list != null && yg_list.size() > 0){
				yg_length = yg_list.size();
			}

			int maxSize = pss_out_length > yg_length ? pss_out_length : yg_length;

			String[] header={"有功功率","时间","pss输出信号","时间"};

			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("sheet1");
			Row rowFirst = sheet.createRow(0);
			for(int i = 0;i<header.length;i++){
				sheet.setColumnWidth(i, 5000);
			}
			for(int i = 0;i<header.length;i++){
				Cell cell = rowFirst.createCell(i);
				cell.setCellValue(header[i]);
			}

			for(int i =0;i<maxSize;i++){
				Row row = sheet.createRow(i+1);
				row.createCell(0).setCellValue("");
				row.createCell(1).setCellValue("");
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue("");

			}
			// 指定本地文件流
			OutputStream os = new FileOutputStream(file);
			// excel写入
			wb.write(os);
			os.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
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
	@ApiOperation(httpMethod = "GET",value = "getUser", notes = "根据用户id获取用户信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType="query", name="@ApiImplicitParam_userId", dataType="String", required=true, value="用户 Id"),
            @ApiImplicitParam(paramType="query", name="@ApiImplicitParam_userName", dataType="String", required=true, value="用户名")
	/* paramType
	header-->放在请求头。请求参数的获取：@RequestHeader(代码中接收注解)
	query-->用于get请求的参数拼接。请求参数的获取：@RequestParam(代码中接收注解)
	path（用于restful接口）-->请求参数的获取：@PathVariable(代码中接收注解)
	body-->放在请求体。请求参数的获取：@RequestBody(代码中接收注解)
	form（不常用）*/
	})
	@ApiResponses({
			@ApiResponse(code = 400, message = "请求参数没填好"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
	})
	public JSONObject getUserById(@ApiParam(name="@ApiParam_username",value = "用户名",required = false) @RequestParam(value = "username",required = false) String userId,
								  @ApiParam(name="@ApiParam_userid",value = "用户id",required = true) @RequestParam(value = "userId",defaultValue="1") String username) {
		//如果加了@RequestParam注解，那么请求url里必须包含这一参数，否则会报400。那么如果允许不传呢？有两种办法：1）使用default值2）使用required值
		User user = mainService.getUserById(Integer.valueOf(username));
		JSONObject json = new JSONObject();
		json.put("user",user);

		return json;
	}

	@RequiresRoles("user")//指定角色才可以执行的权限
	@GetMapping("/page")//相当于@RequestMapping(value="/page",method = RequestMethod.GET)
	public void page(HttpServletRequest request, HttpServletResponse response) {
		try{
			mainService.page(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@RequiresPermissions("user:add")//指定拥有此权限的才可以执行
	@RequestMapping(value="/dept/aJsonObject",method = RequestMethod.GET)
	@ResponseBody
	public JSONObject ztree(HttpServletRequest request) {
		JSONObject json = null;
		try{
			json = mainService.aJsonObject(request);
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 验证码
	 */
	@RequestMapping(value="/code")
	public void generate(HttpServletRequest request,HttpServletResponse response){
		try{
			mainService.generate(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

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

	@RequestMapping(value="/excel")
	@ResponseBody
	public void excel(HttpServletRequest request,HttpServletResponse response){
		try{
			mainService.excel(request,response);
		}catch(Exception e){
			e.printStackTrace();
		}		
		
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

	@PostMapping("validated")
	@ResponseBody
	public ResultInfo<Book> validated(@Validated(Book.group1.class) Book book){

		return new ResultInfo<>(200,"请求成功",book);
	}
}
