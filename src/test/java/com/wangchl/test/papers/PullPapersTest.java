package com.wangchl.test.papers;

import java.io.IOException;

import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.wangchl.lord.PapersOperatAchieve;
import com.wangchl.papers.LocalPullPapers;
import com.wangchl.thread.domain.TaskResult;
import com.wangchl.utils.domain.SftpConnParam;
import com.wangchl.utils.domain.SftpFileParam;

public class PullPapersTest {

	static SftpConnParam connParam = new SftpConnParam.Builder().host("127.0.0.1").port(22)
			.user("root").password("123456.").build();
	
	SftpFileParam papersParma = new SftpFileParam.Builder().papersPath("F:\\A_01_test\\")
			.depositaryPath("/data/").papersName("test.sh")
			.netPapersPath("http://www.wangchunlong.cn/attachment/20200408/e6cd1f0e8e9d424cbb2f8b1ff7001e97.jpg")
			.build();
	
	@SuppressWarnings("unchecked")
	@Test
	public void executePullPapersParam() throws JSchException, IOException {
		SftpFileParam papersParma = 
				new SftpFileParam.Builder().
				papersPath("F:\\A_01_test\\")
				.depositaryPath("/data/").papersName("test.sh")
				.build();
		@SuppressWarnings("rawtypes")
		TaskResult<?> pullPapers = new PapersOperatAchieve().executeOperat(new LocalPullPapers<Object>(connParam, papersParma));
		System.out.println(pullPapers.toString());
		
	}
	
	
}
