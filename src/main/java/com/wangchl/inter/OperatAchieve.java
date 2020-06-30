package com.wangchl.inter;

import java.io.IOException;

import com.jcraft.jsch.JSchException;
import com.wangchl.thread.domain.TaskResult;

public interface OperatAchieve<R> {

	public  TaskResult<R> executeOperat(PapersHandle<R> prisoners) throws JSchException, IOException; 

//	public  TaskResult<R> pullPapers(PapersHandle<R> prisoners) throws JSchException, IOException ;




}
