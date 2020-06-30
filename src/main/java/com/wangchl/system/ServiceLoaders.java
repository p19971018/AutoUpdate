package com.wangchl.system;

import java.io.IOException;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.jcraft.jsch.JSchException;
import com.wangchl.inter.OperatAchieve;
import com.wangchl.inter.PapersHandle;
import com.wangchl.papers.ShellPushPapers;

/**
 * 
 * 取消方法，太过于笨拙
 * SPI加载
 * 如使用SPI加载目标方法须要有默认的无参构造，否则会出现 
 * @author wangchl
 * @param <T>
 * @param <R>
 *
 */
@Deprecated
public class ServiceLoaders<T, R> {

	ConcurrentHashMap<Class<T>, R> cmap = new ConcurrentHashMap<Class<T>, R>();
	
	public static <T> ServiceLoader<T> load(Class<T> clazz) {
		//服务加载器,加载实现类
		return ServiceLoader.load(clazz);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void test() throws JSchException, IOException {
		PapersHandle shellPushPapers = new ShellPushPapers(null,null);
		ServiceLoader<OperatAchieve> load = load(OperatAchieve.class);
		for (OperatAchieve<?> papersHandle : load) {
			papersHandle.executeOperat(shellPushPapers);
		}

	}


}


