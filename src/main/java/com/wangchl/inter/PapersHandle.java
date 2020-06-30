package com.wangchl.inter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wangchl.thread.domain.TaskResult;

public interface PapersHandle<R> {
	
	static Logger logger = LogManager.getLogger();

	@SuppressWarnings("hiding")
	public <R,T>  TaskResult<R> executePapersOperat();
	
	
	 

}
