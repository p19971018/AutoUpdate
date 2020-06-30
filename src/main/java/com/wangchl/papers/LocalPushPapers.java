package com.wangchl.papers;

import com.wangchl.inter.PapersHandle;
import com.wangchl.papers.utils.PapersUtils;
import com.wangchl.system.SftpChannelHandle;
import com.wangchl.thread.domain.TaskResult;
import com.wangchl.thread.domain.TaskResultType;
import com.wangchl.utils.domain.SftpConnParam;
import com.wangchl.utils.domain.SftpFileParam;

/**
 * 本地文件上传
 * @author wangchl
 * @param <R>
 *
 */
public class LocalPushPapers<R>  implements PapersHandle<R>{

	public final static String JOB_NAME = "文件上传";

	/**服务器信息*/
	private SftpConnParam connParam;
	/**文件信息*/
	private SftpFileParam papersParma;

	public LocalPushPapers(SftpConnParam connParam, SftpFileParam papersParma) {
		this.connParam = connParam;
		this.papersParma = papersParma;
	}
	
	/**
	 * 文件上传
	 */
	@SuppressWarnings("hiding")
	@Override
	public <R, T> TaskResult<R> executePapersOperat() {
		R r = null;
		try {
			String upload = new SftpChannelHandle(connParam, papersParma).upload();
			return PapersUtils.executeResult(upload);
		} catch (Exception e) {
			e.printStackTrace();
			return new TaskResult<R>(TaskResultType.Exception, r , e.getMessage());
		}
	}



}
