package com.wangchl.utils.domain;


/**
 * 文件信息配置
 * @author wangchl
 *
 */
public class SftpFileParam {

	/**本地文件存放路径*/
	private String papersPath;
	/**网络文件路径*/
	private String netPapersPath;
	/**服务器存储文件路径*/
	private String depositaryPath;
	/**文件名*/
	private String papersName;
	/**Shell名 */
	private String shellName;
	/**是否备份*/
	private boolean backblaze;
	/**文件是否执行*/
	private boolean runPapers;
	/**是否执行*/
	private boolean run;
	/**是否关闭正在运行的程序*/
	private boolean kill;
	/**是否是从net获取*/
	private boolean wget;
	/**下载文件重命名 */
	private String wgetPapersName;
	/**Shell超时时间*/
	private long shellTimeOut;
	
	public SftpFileParam(Builder builder) {
		this.papersPath = builder.papersPath;
		this.netPapersPath = builder.netPapersPath;
		this.depositaryPath = builder.depositaryPath;
		this.papersName = builder.papersName;
		this.shellName = builder.shellName;
		this.runPapers = builder.runPapers;
		this.backblaze = builder.backblaze;
		this.run = builder.run;
		this.kill = builder.kill;
		this.wget = builder.wget;
		this.shellTimeOut = builder.shellTimeOut;
		this.wgetPapersName = builder.wgetPapersName;
	}


	public static final class Builder{

		/**文件路径*/
		private String papersPath;
		/**网络文件路径*/
		private String netPapersPath;
		/**服务器存储文件路径*/
		private String depositaryPath;
		/**文件名*/
		private String papersName;
		/**Shell名*/
		private String shellName;
		/**是否备份*/
		private boolean backblaze;
		/**文件是否执行*/
		private boolean runPapers;
		/**是否执行*/
		private boolean run;
		/**是否关闭正在运行的程序*/
		private boolean kill;
		/**是否是从net获取*/
		private boolean wget;
		/**下载文件重命名 */
		private String wgetPapersName;
		/**Shell超时时间*/
		private long shellTimeOut;


		public Builder() {
		}

		public Builder papersPath(String papersPath) {
			this.papersPath = papersPath;
			return this;
		}
		public Builder netPapersPath(String netPapersPath) {
			this.netPapersPath = netPapersPath;
			return this;
		}
		public Builder depositaryPath(String depositaryPath) {
			this.depositaryPath = depositaryPath;
			return this;
		}
		public Builder papersName(String papersName) {
			this.papersName = papersName;
			return this;
		}
		public Builder shellName(String shellName) {
			this.shellName = shellName;
			return this;
		}

		public Builder backblaze(boolean backblaze) {
			this.backblaze = backblaze;
			return this;
		}
		public Builder runPapers(boolean runPapers) {
			this.runPapers = runPapers;
			return this;
		}
		public Builder run(boolean run) {
			this.run = run;
			return this;
		}
		public Builder kill(boolean kill) {
			this.kill = kill;
			return this;
		}
		public Builder wget(boolean wget) {
			this.wget = wget;
			return this;
		}
		public Builder wgetPapersName(String wgetPapersName) {
			this.wgetPapersName = wgetPapersName;
			return this;
		}
		public Builder shellTimeOut(long shellTimeOut) {
			this.shellTimeOut = shellTimeOut;
			return this;
		}
		public SftpFileParam build() {
			return new  SftpFileParam(this);
		} 

	}


	public String getPapersPath() {
		return papersPath;
	}


	public String getDepositaryPath() {
		return depositaryPath;
	}


	public String getPapersName() {
		return papersName;
	}


	public String getShellName() {
		return shellName;
	}


	public void setPapersPath(String papersPath) {
		this.papersPath = papersPath;
	}


	public void setDepositaryPath(String depositaryPath) {
		this.depositaryPath = depositaryPath;
	}


	public void setPapersName(String papersName) {
		this.papersName = papersName;
	}


	public void setShellName(String shellName) {
		this.shellName = shellName;
	}

	public String getNetPapersPath() {
		return netPapersPath;
	}


	public void setNetPapersPath(String netPapersPath) {
		this.netPapersPath = netPapersPath;
	}


	public boolean isBackblaze() {
		return backblaze;
	}


	public boolean isRun() {
		return run;
	}


	public boolean isKill() {
		return kill;
	}


	public boolean isWget() {
		return wget;
	}


	public String getWgetPapersName() {
		return wgetPapersName;
	}


	public void setWgetPapersName(String wgetPapersName) {
		this.wgetPapersName = wgetPapersName;
	}


	public boolean isRunPapers() {
		return runPapers;
	}


	public long getShellTimeOut() {
		return shellTimeOut;
	}


	@Override
	public String toString() {
		return "SftpFileParam [papersPath=" + papersPath + ", netPapersPath=" + netPapersPath + ", depositaryPath="
				+ depositaryPath + ", papersName=" + papersName + ", shellName=" + shellName + ", backblaze="
				+ backblaze + ", runPapers=" + runPapers + ", run=" + run + ", kill=" + kill + ", wget=" + wget
				+ ", shellTimeOut=" + shellTimeOut + "]";
	}


}
