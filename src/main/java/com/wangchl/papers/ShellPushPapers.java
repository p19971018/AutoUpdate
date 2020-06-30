package com.wangchl.papers;

import java.io.File;

import com.wangchl.inter.PapersHandle;
import com.wangchl.papers.utils.PapersUtils;
import com.wangchl.system.SftpChannelHandle;
import com.wangchl.thread.domain.TaskResult;
import com.wangchl.thread.domain.TaskResultType;
import com.wangchl.utils.ShellUtils;
import com.wangchl.utils.domain.SftpConnParam;
import com.wangchl.utils.domain.SftpFileParam;

/**
 * 自动生成文本并上传
 * @author wangchl
 * @param <R>
 * @param <T>
 *
 */
public class ShellPushPapers<R, T> implements PapersHandle<String>{


	/**服务器信息*/
	private SftpConnParam connParam;
	/**文件信息*/
	private SftpFileParam fileParam;

	public ShellPushPapers(SftpConnParam connParam, SftpFileParam fileParam) {
		this.connParam = connParam;
		this.fileParam = fileParam;
	}
	
	@SuppressWarnings({ "hiding" })
	@Override
	public <R,T> TaskResult<R> executePapersOperat() {

		R r = null;
		File createShell = null;
		logger.info("开始生成文件");

		try {
			createShell = createShell();
			
			logger.info("文件生成成功,文件存放路径：{}" , createShell.getPath());
			
			if( null != createShell  && fileParam.isRun() ) {
				
				logger.info("正在上传至服务器" , createShell.getPath());
				
				String upload = new SftpChannelHandle(connParam, fileParam).upload();
				return PapersUtils.executeResult(upload);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return  new TaskResult<R>(TaskResultType.Exception, r , e.getMessage());
		}
		return PapersUtils.executeResult(createShell);
	}


	public File createShell() throws Exception {
		String papersName = fileParam.getPapersName();
		StringBuffer buffer = new StringBuffer();
		buffer.append("#!/bin/sh\n" );
		boolean backblaze = fileParam.isBackblaze();
		if(backblaze) {
			buffer.append("echo \"开始备份文件.....\"\n");
			buffer.append("mv_status=`mv "+papersName+" "+ papersName + System.currentTimeMillis() + "`\n");
			buffer.append("if [[ $mv_status -ne 0 ]];then\n");
			buffer.append(" echo \"备份失败,终止执行\"\n");
			buffer.append("else\n");
			if(logger.isInfoEnabled()) {
				logger.info("写入备份指令完成");
			}
		}

		boolean wget = fileParam.isWget();
		if (wget) {
			buffer.append(" wget_status=`wget "+fileParam.getNetPapersPath());
			if(null != fileParam.getWgetPapersName()) {
				buffer.append(" -O  "+papersName);
			}
			buffer.append(" `\n");
			buffer.append(" if [[ $wget_status -ne 0 ]];then\n");
			buffer.append("  echo \"文件下载失败\"\n");
			buffer.append(" else\n");
			buffer.append("  echo \"文件下载成功\"\n");
			if(logger.isInfoEnabled()) {
				logger.info("写入wget指令成功");
			}
		}
		if(fileParam.isKill()) {
			buffer.append("  echo \"准备关闭程序 \"\n");
			buffer.append("  processId=`ps -ef | grep "+papersName+" | grep -v grep | awk '{print $2}'`\n");
			buffer.append("  kill -9 $processId\n" ); 
			buffer.append("  echo \"执行完成\"\n");
			if(logger.isInfoEnabled()) {
				logger.info("关闭程序指令成功");
			}
		}

		if(wget) {
			buffer.append(" fi\n");
		}
		if(fileParam.isRunPapers()) {
			buffer.append(" sh "+fileParam.getDepositaryPath() + papersName+"\n");
			buffer.append(" if [[ $? -ne 0 ]]; then\n");
			buffer.append("  echo \"start success....\"\n");
			buffer.append(" else\n");
			buffer.append("  echo \"start failure....\"\n");
			buffer.append(" fi\n");
			if(logger.isInfoEnabled()) {
				logger.info("启动脚本写入成功");
			}
		}
		if(backblaze) {
			buffer.append("fi\n");
		}

		return ShellUtils.createShell(fileParam.getPapersPath(),papersName, buffer);
	}





}
