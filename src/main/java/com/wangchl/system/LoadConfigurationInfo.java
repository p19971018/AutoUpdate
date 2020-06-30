package com.wangchl.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @author 服务器信息加载
 *
 */
public class LoadConfigurationInfo {

	/**服务器地址*/
	protected static String host;
	/**端口*/
	protected static int port;
	/**登录用户*/
	protected static String user;
	/**登录密码*/
	protected static String password;
	
	
	private  LoadConfigurationInfo() {
	}


	static {
		Properties properties = new Properties();
		InputStream inputStream = Object.class.getResourceAsStream("/AutoUpdate.properties");
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		host = properties.get("server.host").toString();
		port = Integer.parseInt(properties.get("server.port").toString());
		user = properties.get("server.user").toString();
		password = properties.get("server.password").toString();
	}


	public static String getHost() {
		return host;
	}


	public static int getPort() {
		return port;
	}


	public static String getUser() {
		return user;
	}


	public static String getPassword() {
		return password;
	}
	
	 
}
