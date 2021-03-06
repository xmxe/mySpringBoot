package com.xmxe.config.filter;

import com.alibaba.fastjson.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;


/**
 * WebSocket在线管理
 */
public class OnlineChatServer extends WebSocketServer {
	// log4j2实现
	// Logger logger = LogManager.getLogger(OnlineChatServer.class);
	// slf4j实现
	Logger logger = LoggerFactory.getLogger(OnlineChatServer.class);

	public OnlineChatServer(int port) {
		super(new InetSocketAddress(port));
	}

	public OnlineChatServer(InetSocketAddress address) {
		super(address);
	}

	/**
	 * 触发连接事件
	 */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		logger.info("WebSocket-onOpen：{},{}" ,conn.getRemoteSocketAddress().getAddress().getHostAddress(),handshake.getResourceDescriptor());
	}

	/**
	 * 触发关闭事件（用户下线处理）
	 *
	 */
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		userLeave(conn);
	}

	/**
	 * 客户端发送消息到服务器时触发事件
	 */
	@Override
	public void onMessage(WebSocket conn, String message) {
		logger.info("WebSocket-onMessage：{}",message);
		if (null != message && message.startsWith("join_")) {
			this.userjoin(message.replaceFirst("join_", ""), conn);
		} else if (null != message && message.startsWith("goOut_")) {
			this.goOut(message.replaceFirst("goOut_", ""));
		} else if (null != message && message.startsWith("fhsms_")) {
			this.senFhsms(message.replaceFirst("fhsms_", ""));
		} else if (null != message && message.startsWith("leave_")) {
			this.userLeave(conn);
		} else if (null != message && message.startsWith("count_")) {
			this.getUserCount(conn);
		} else if (null != message && message.startsWith("special_")) {
			OnlineChatServerPool.setFhadmin(conn);
			this.getUserList();
		} else {
			OnlineChatServerPool.sendMessageToUser(conn, message);// 同时向本人发送消息
		}
	}
	@Override
	public void onStart() {
		logger.info("WebSocket启动 :{}","WebSocketServer onStart");
	}


	/**
	 * 触发异常事件
	 */
	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific websocket
			logger.info("websocket error != null");
		}
	}

	public void onFragment(WebSocket conn, Framedata fragment) {}

	/**
	 * 用户加入处理
	 */
	public void userjoin(String user, WebSocket conn) {
		onlineMaganger(1, user, conn);
	}

	/**
	 * 站内信通知
	 */
	public void senFhsms(String user) {
		JSONObject result = new JSONObject();
		result.put("type", "senFhsms");
		OnlineChatServerPool.sendMessageToUser(OnlineChatServerPool.getWebSocketByUser(user), result.toJSONString());
	}

	/**
	 * 强制某用户下线
	 */
	public void goOut(String user) {
		this.goOut(OnlineChatServerPool.getWebSocketByUser(user), "thegoout");
	}

	/**
	 * 强制用户下线
	 */
	public void goOut(WebSocket conn, String type) {
		JSONObject result = new JSONObject();
		result.put("type", type);
		result.put("msg", "goOut");
		OnlineChatServerPool.sendMessageToUser(conn, result.toJSONString());
	}

	/**
	 * 用户下线处理
	 */
	public void userLeave(WebSocket conn) {
		onlineMaganger(2, null, conn);
	}

	/**
	 * 获取在线总数
	 */
	public void getUserCount(WebSocket conn) {
		JSONObject result = new JSONObject();
		result.put("type", "count");
		result.put("msg", OnlineChatServerPool.getUserCount());
		OnlineChatServerPool.sendMessageToUser(conn, result.toJSONString());
	}

	/**
	 * 获取在线用户列表
	 */
	public void getUserList() {
		WebSocket conn = OnlineChatServerPool.getFhadmin();
		if (null == conn) {
			return;
		}
		JSONObject result = new JSONObject();
		result.put("type", "userlist");
		result.put("list", OnlineChatServerPool.getOnlineUser());
		OnlineChatServerPool.sendMessageToUser(conn, result.toJSONString());
	}

	/**
	 * 用户上下线管理
	 * 
	 * @param type 1：上线；2：下线
	 * @param user
	 * @param conn
	 */
	public synchronized void onlineMaganger(int type, String user, WebSocket conn) {
		if (type == 1) {
			if (null == OnlineChatServerPool.getWebSocketByUser(user)) { // 判断用户是否在其它终端登录
				OnlineChatServerPool.addUser(user, conn); // 向连接池添加当前的连接对象
				addUserToFhadmin(user);
			} else {
				// goOut(conn,"goOut"); //不能重复登录

				/* 挤掉对方 */
				goOut(user);
				OnlineChatServerPool.addUser(user, conn);
				addUserToFhadmin(user);
			}
		} else {
			OnlineChatServerPool.removeUser(conn); // 在连接池中移除连接
			this.getUserList();
		}
	}

	/**
	 * 有用户登录系统,加入在线列表
	 */
	public void addUserToFhadmin(String user) {
		WebSocket conn = OnlineChatServerPool.getFhadmin();
		if (null == conn) {
			return;
		}
		JSONObject result = new JSONObject();
		result.put("type", "addUser");
		result.put("user", user);
		OnlineChatServerPool.sendMessageToUser(conn, result.toJSONString());
	}



	// 假如SpringBoot项目以jar方式运行 当maven install生成jar包会报错
	// 因为不允许在其它类出现main方法以防止和启动类的main方法冲突
	/*
	 public static void main( String[] args ) throws InterruptedException,IOException { 
	 	WebSocketImpl.DEBUG = false; int port = 8887; //端口
		OnlineChatServer s = new OnlineChatServer(port); s.start();
		//System.out.println( "服务器的端口" + s.getPort() );
	}*/
	 
}
