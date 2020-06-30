package com.wangchl.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.wangchl.inter.PapersOperatHandle;
import com.wangchl.utils.domain.SftpConnParam;
import com.wangchl.utils.domain.SftpFileParam;

/**
 * 核心处理类
 * @author wangchl
 *
 */
public class SftpChannelHandle implements PapersOperatHandle{

	static ChannelSftp sftp = null;
	static Channel channel = null;
	static Session sshSession = null;

	SftpConnParam connParam;
	SftpFileParam papersParma;

	@SuppressWarnings("unused")
	private SftpChannelHandle() {}

	public SftpChannelHandle(SftpConnParam connParam, SftpFileParam papersParma) {
		this.connParam = connParam;
		this.papersParma = papersParma;
	}

	public void openSession() {
		openSession(connParam);

	}
	public void openSession(SftpConnParam connParam) {
		String host = connParam.getHost();
		try {
			JSch jsch = new JSch();
			sshSession = jsch.getSession(connParam.getUser(),host, connParam.getPort());
			sshSession.setPassword(connParam.getPassword());

			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			if(logger.isInfoEnabled()) {
				logger.info("{} Session connected!",host);
			}

			channel = sshSession.openChannel("sftp");
			channel.connect();
			if(logger.isInfoEnabled()) {
				logger.info("{} Channel connected!",host);
			}
			sftp =  (ChannelSftp) channel;
		} catch (Exception e) {
			logger.error("连接{}服务器失败  " , host , e);
			e.printStackTrace();
		} 
	}
	/**
	 *  文件上传
	 * @return
	 * @throws SftpException
	 * @throws IOException 
	 * @throws JSchException 
	 */
	public String upload()  {
		openSession();
		String depositaryPath = papersParma.getDepositaryPath();
		String papersName = papersParma.getPapersName();
		String papersPath = papersParma.getPapersPath() + papersName;

		try {

			for (Object item : sftp.ls(depositaryPath)) {
				LsEntry entry = (LsEntry) item;

				if(papersName.equals(entry.getFilename())) {

					String oldPath = depositaryPath + papersName;
					if(logger.isInfoEnabled()) {
						logger.info(papersName + "已存在,开始备份并准备覆盖");
					}
					String newPath = oldPath+System.currentTimeMillis();
					sftp.rename(oldPath, newPath);

					if(logger.isInfoEnabled()) {
						logger.info(papersName + "已备份,准备覆盖");
					}
					sftp.put(papersPath, depositaryPath);
					if(logger.isInfoEnabled()) {
						logger.info(papersName + "已完成替换");
					}
				}
			}
			
			sftp.put(papersPath, depositaryPath);
			if(papersParma.isRun()) { 
				logger.info("开始执行{} ",papersName); 
				String result = run();
				logger.info("执行完成，执行日志：{}" , result ); 
				return result;
			}
			logger.info("upload success");
			return "upload success";
		} catch (SftpException e) {
			logger.error("上传失败！文件名：{} " , papersName, e);
			e.printStackTrace();
		}finally {
			closeConnection();
		}

		return null;
	}
	public void closeConnection() {
		closeChannel(sftp);
		closeChannel(channel);
		closeSession(sshSession);
	}

	public  String run()  {
		String depositaryPath = papersParma.getDepositaryPath();
		String path = depositaryPath+papersParma.getPapersName();

		String str= "cd "+depositaryPath+" \n chmod +x "+path+"\n"+path;
		if(logger.isInfoEnabled()) {
			logger.info("执行{{}}",str);
		}
		OutputStream outputstream_for_the_channel = null;
		InputStream inputstream_from_the_channel = null;
		PrintStream commander = null;
		BufferedReader br = null;
		StringBuffer result = new StringBuffer();
		try {
			channel = sshSession.openChannel("shell");
			outputstream_for_the_channel = channel.getOutputStream();
			commander = new PrintStream(outputstream_for_the_channel, true, "UTF-8");
			channel.connect();
			logger.info("执行完成，正在等待服务器返回结果");
			commander.println(str);
			commander.println("exit");

			inputstream_from_the_channel = channel.getInputStream();
			br = new BufferedReader(new InputStreamReader(inputstream_from_the_channel, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				result.append(line );
			}
			long shellTimeOut = papersParma.getShellTimeOut();
			shellTimeOut = shellTimeOut > 0 ? shellTimeOut : 3000 ;
			long startTime = System.currentTimeMillis(); 
			while(channel.getExitStatus() < 0 ) {
				if(System.currentTimeMillis() - startTime > shellTimeOut) {
					logger.error("{}执行超时，终止操作",str);
					return null;
				}
			}
			return result.toString();
		} catch (JSchException e) {
			logger.info("执行失败！路径：{} ",  path,e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.info("读取文件异常！路径：{} ",  path,e);
			e.printStackTrace();
		}finally {
			commander.close();
			closeConnection();
		}
		return null;
	}

	public static void closeChannel(Channel channel) {
		if (channel != null) {
			if (channel.isConnected()) {
				channel.disconnect();
			}
		}
	}

	public static void closeSession(Session session) {
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}

	public File download()  {
		openSession();

		String depositaryPath = papersParma.getDepositaryPath();
		String papersName = papersParma.getPapersName();
		String papersPath = papersParma.getPapersPath();
		File targetFile = null;
		try {

			if (depositaryPath != null && !"".equals(depositaryPath)) {
				sftp.cd(depositaryPath);
			}

			InputStream inputStream = sftp.get(papersName);
			targetFile = new File(papersPath + papersName);

			java.nio.file.Files.copy(
					inputStream,
					targetFile.toPath(),
					StandardCopyOption.REPLACE_EXISTING);
			logger.info("下载文件成功！文件：{} 下载成功，已存入{}",  papersName,papersPath);
		} catch (IOException e) {
			logger.error("下载文件失败！文件名：{} " , papersName, e);
			e.printStackTrace();
		} catch (SftpException e) {
			logger.error("下载文件读取失败！文件名：{}" , papersName, e);
			e.printStackTrace();
		}finally {
			closeConnection();
		}
		return targetFile;
	}

}
