package com.wangchl.test.papers;

import java.io.IOException;

import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.wangchl.lord.PapersOperatAchieve;
import com.wangchl.papers.ShellPushPapers;
import com.wangchl.thread.domain.TaskResult;
import com.wangchl.utils.domain.SftpConnParam;
import com.wangchl.utils.domain.SftpFileParam;

public class ShellPushPapersTest {

	
	static SftpConnParam connParam = new SftpConnParam.Builder().host("127.0.01").port(22)
			.user("root").password("123456.").build();
	
	SftpFileParam papersParma = new SftpFileParam.Builder().papersPath("F:\\A_01_test\\test.sh")
			.depositaryPath("/data/").papersName("test.sh")
			.netPapersPath("http://www.wangchunlong.cn/attachment/20200408/e6cd1f0e8e9d424cbb2f8b1ff7001e97.jpg")
			.build();
	
	@SuppressWarnings("unchecked")
	@Test
	public void executePushPapersParam() throws JSchException, IOException {
		SftpFileParam papersParma = 
				new SftpFileParam.Builder().
				papersPath("F:\\A_01_test\\")
				.depositaryPath("/data/").papersName("test.sh")
				.netPapersPath("http://www.wangchunlong.cn/attachment/20200408/e6cd1f0e8e9d424cbb2f8b1ff7001e97.jpg")
				.run(true).wget(true).shellTimeOut(3000)
				.build();
		@SuppressWarnings("rawtypes")
		TaskResult<?> pushPapers = new PapersOperatAchieve().executeOperat(new ShellPushPapers(connParam, papersParma));
		System.out.println(pushPapers.toString());
	}
}

