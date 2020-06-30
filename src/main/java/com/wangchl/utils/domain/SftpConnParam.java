package com.wangchl.utils.domain;

/**
 * 连接服务器参数
 * @author wangchl
 *
 */
public class SftpConnParam {

	/**服务器地址*/
	private String host;
	/**端口*/
	private int port;
	/**连接用户*/
	private String user;
	/**连接密码*/
	private String password;

	public SftpConnParam(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.user = builder.user;
		this.password = builder.password;
	}
	
	public String getHost() {
		return host;
	}
	public int getPort() {
		return port;
	}
	public String getUser() {
		return user;
	}
	public String getPassword() {
		return password;
	}
	

	public static final class Builder{
		/**服务器地址*/
		private String host;
		/**端口*/
		private int port;
		/**连接用户*/
		private String user;
		/**连接密码*/
		private String password;
		public Builder() {
		}

		public Builder host(String host) {
			this.host = host;
			return this;
		}
		public Builder port(int port) {
			this.port = port;
			return this;
		}
		public Builder user(String user) {
			this.user = user;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		public SftpConnParam build() {
			return new  SftpConnParam(this);
		} 
	}
	@Override
	public String toString() {
		return "SftpConnParam [host=" + host + ", port=" + port + ", user=" + user + ", password=" + password + "]";
	}

}
