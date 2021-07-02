package com.xmxe.controller;

import com.xmxe.entity.HttpResult;
import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "订单模块")
public class SwaggerController {

	@RequestMapping("/getUserById")
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "getUser", notes = "根据用户id获取用户信息",response = HttpResult.class)
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
			@ApiResponse(code = 400, message = "请求参数有误 bad request"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
	})

	public HttpResult getUserById(@ApiParam(name="@ApiParam_username",value = "用户名",required = false) @RequestParam(value = "username",required = false) String userId,
								  @ApiParam(name="@ApiParam_userid",value = "用户id",required = true) @RequestParam(value = "userId",defaultValue="1") String username) {
		//如果加了@RequestParam注解，那么请求url里必须包含这一参数，否则会报400。那么如果允许不传呢？有两种办法：1）使用default值2）使用required值
		System.out.println(111);

		return new HttpResult(200,"success");
	}

}
