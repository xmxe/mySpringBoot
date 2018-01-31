<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="js/jquery.min.js" ></script>
<script type="text/javascript">
	var next,pre,pageCount;
	//当整个页面加载完毕时执行
	$(function(){
		queryData(1);
		//~~~~~~~注册点击事件
		$("#first").click(function(){
			queryData(1);
		});
		$("#next").click(function(){
			queryData(next);
		});
		$("#pre").click(function(){
			queryData(pre);
		});
		$("#end").click(function(){
			queryData(pageCount);
		});
	})
		function queryData(current){
			$.ajax({
				type:"post",
				url:"page.do", //currentPage=1&tj=zhangsan
				data:"currentPage="+current,
				dataType:"json",
				success:function(msg){// msg======>page
					console.log(msg);
					next = msg.jo.next;
					pre = msg.jo.pre;
					pageCount = msg.jo.pageCount;
					var trs="";
					$.each(msg.jo.rows,function(i,user){
						trs+="<tr><td>"+(msg.jo.start+i+1)+"</td><td>"+user.realname
						+"</td></tr>";
					});
					$("#data").html(trs);
				}
			});	
		}
	var tj="";
	function searchbyconditions(){
		tj = $("input[name='tj']").val();
		if(tj!="" && tj!=null){
			tj="&tj="+tj;
		}
		queryData(1,tj);
	}
	</script>
  </head>
  
  <body>
  	<div style="text-align:center">
  		<button type="button" onclick="searchbyconditions()">查询</button>
	    <table align="center" border="1" cellspacing="0" width="600px" 
	    bgcolor="#CCCCCC">
	    	<thead>
	    		<tr>
	    			<th>编号</th>
	    			<th>名字</th>
	    		</tr>
	    	</thead>
	    	<tbody id="data"></tbody>	
	    </table>
	    <button type="button" id="first">首页</button>
	    <button type="button" id="pre">上一页</button>
	    <button type="button" id="next">下一页</button>
	    <button type="button" id="end">尾页</button>
    </div>
  </body>
</html>