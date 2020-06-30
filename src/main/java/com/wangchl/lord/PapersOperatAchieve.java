package com.wangchl.lord;

import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.wangchl.inter.OperatAchieve;
import com.wangchl.inter.PapersHandle;
import com.wangchl.thread.domain.TaskResult;

/**
 * 统一执行入口    
 * 迪米特法则
 * @author wangchl
 * @param <R>
 *
 */
public  class PapersOperatAchieve<R> implements OperatAchieve<R>{


	@Override
	public TaskResult<R> executeOperat(PapersHandle<R> prisoners)
			throws JSchException, IOException {
		return prisoners.executePapersOperat();
			
	}

	/*@Override
	public TaskResult<R> pushPapers(PapersHandle<R> prisoners) throws JSchException, IOException {
		return prisoners.executePapersOperat();
	}*/


}
