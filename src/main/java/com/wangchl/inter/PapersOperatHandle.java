package com.wangchl.inter;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface PapersOperatHandle {
	
	static Logger logger = LogManager.getLogger(PapersOperatHandle.class);
	
	String upload();
	
	String run();
	
	File download() ;

}
