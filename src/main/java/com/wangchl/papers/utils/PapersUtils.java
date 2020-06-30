package com.wangchl.papers.utils;

import com.wangchl.thread.domain.TaskResult;
import com.wangchl.thread.domain.TaskResultType;

/**
 * 文件处理工具类
 * @author wangchl
 *
 */
public class PapersUtils {

	/**
	 * 文件执行完成后返回格式
	 * @param t 处理参数
	 * @return TaskResult:返回信息
	 */
	@SuppressWarnings("unchecked")
	public static <R,T> TaskResult<R> executeResult(T t) {
		R r = null;
		TaskResult<R> result = null;
		try {
			if(null == t) {
				return new TaskResult<R>(TaskResultType.Failure , r , "result is NULL");
			}
			return  new TaskResult<R>(TaskResultType.Success, (R) t);
		} catch (Exception e) {
			e.printStackTrace();
			result = new TaskResult<R>(TaskResultType.Exception, r , e.getMessage());
		}
		return result;
	}
}
