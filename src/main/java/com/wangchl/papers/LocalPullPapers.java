package com.wangchl.papers;

import java.io.File;

import com.wangchl.inter.PapersHandle;
import com.wangchl.papers.utils.PapersUtils;
import com.wangchl.system.SftpChannelHandle;
import com.wangchl.thread.domain.TaskResult;
import com.wangchl.thread.domain.TaskResultType;
import com.wangchl.utils.domain.SftpConnParam;
import com.wangchl.utils.domain.SftpFileParam;

/**
 * 到服务器拉取文件
 * @author wangchl
 *
 * @param <R>
 */
public class LocalPullPapers<R>  implements PapersHandle<R> {

	public final static String JOB_NAME = "下载文件";
	/**文件地址*/
	private SftpConnParam connParam;
	/**存放路径*/
	private SftpFileParam papersParma;

	public LocalPullPapers(SftpConnParam connParam, SftpFileParam papersParma) {
		this.connParam = connParam;
		this.papersParma = papersParma;
	}
	


	/**
	 * 文件下载
	 */
	@SuppressWarnings("hiding")
	@Override
	public <R, T> TaskResult<R> executePapersOperat() {
		R r = null;
		try {
			File downloadPath = new SftpChannelHandle(connParam, papersParma).download();
			return PapersUtils.executeResult(downloadPath);
		} catch (Exception e) {
			e.printStackTrace();
			return new TaskResult<R>(TaskResultType.Exception, r , e.getMessage());
		}
	}




}
