package com.wangchl.system;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler{

	
	private Object delegate;
	public Object bind(Object delegate) {
		this.delegate = delegate;
		
		return Proxy.newProxyInstance(delegate.getClass().getClassLoader(), delegate.getClass().getInterfaces(), this);
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		long start=System.currentTimeMillis();
		Object result=method.invoke(delegate, args);
		
//		long end=System.currentTimeMillis();
//		System.out.println("Exit method "+method.getName());
//		System.out.println("执行时间："+(end-start));
		return result;
	}
	

}


