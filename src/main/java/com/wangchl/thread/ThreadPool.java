package com.wangchl.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.wangchl.thread.domain.ITaskProcesser;
import com.wangchl.thread.domain.JobInfo;
import com.wangchl.thread.domain.TaskResult;
import com.wangchl.thread.domain.TaskResultType;

/**
 * 线程池
 * 线程数控制在CPU*2，过多不会利于并发，时间片轮询频繁反而会拖累程序的执行速度
 * @author wangchl
 *
 */
public class ThreadPool {

	private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors()*2;
	
	/**存放待处理任务*/
	private static BlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(5000);
	/**工作信息存放容器*/
	private static Map<String, JobInfo<?>> jobInfoMap = new ConcurrentHashMap<>();
	/**线程池(线程池大小为CPU*2)*/
	private volatile static ExecutorService threadPool = new ThreadPoolExecutor( THREAD_COUNT,THREAD_COUNT,60,TimeUnit.SECONDS,taskQueue);


	public static Map<String, JobInfo<?>> getMap(){
		return jobInfoMap;
	}

	public static void shutdown() {
		threadPool.shutdown();
	}


	private static class PendingTsak<T,R> implements Callable<String>{

		private JobInfo<R> jobInfo;
		private T processData;


		public PendingTsak(JobInfo<R> jobInfo, T processData) {
			super();
			this.jobInfo = jobInfo;
			this.processData = processData;
		}


		@SuppressWarnings("unchecked")
		@Override
		public String call() throws Exception {
			R r = null;
			TaskResult<R> result = null;
			try {
				ITaskProcesser<T, R> taskProcesser = (ITaskProcesser<T, R>) jobInfo.getTaskProcesser();
				//执行任务，获得结果
				result = taskProcesser.taskExecute(processData);
				if(null == result) {
					result = new TaskResult<R>(TaskResultType.Exception,r, "result is null");
				}
				if(null == result.getResultType()) {
					if(null == result.getReason()){
						result = new TaskResult<R>(TaskResultType.Exception,r, "result is null,reason:"+result.getReason());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new TaskResult<R>(TaskResultType.Exception, r,e.getMessage());
			}finally {
				//将任务的处理结果写入缓存
				jobInfo.addTaskResult(result);
			}
			return jobInfo.toString();
		}
	}
	
	/***
	 * 任务注册
	 * @param jobName 任务名称
	 * @param jobLength 任务长度
	 * @param taskProcesser 实现方法
	 * @param expireTime 结果缓存时间
	 */
	public static <R> void registerJob(String jobName , int jobLength ,ITaskProcesser<?, ?> taskProcesser,long expireTime) {
		JobInfo<R> jobInfo = new JobInfo<>(jobName, jobLength, taskProcesser, expireTime);
		if(null != jobInfoMap.putIfAbsent(jobName, jobInfo) ) {
			throw new RuntimeException(jobName + "已经注册"); 
		}
	}
	/**
	 * 提交工作中的任务
	 * @param jobName 任务名称
	 * @param t 执行参数
	 */
	public static <T,R> void putTask(String jobName , T t ) {
		JobInfo<R> jobInfo = getJob(jobName);
		PendingTsak<T, R> task = new PendingTsak<T, R>(jobInfo, t);
		threadPool.submit(task);
	}
	/**
	 * 根据工作名称进行检索
	 * @param jobName 任务名称
	 * @return  
	 */
	@SuppressWarnings("unchecked")
	public static <R> JobInfo<R> getJob(String jobName) {
		JobInfo<R> jobInfo = (JobInfo<R>) jobInfoMap.get(jobName);
		if(null == jobInfo) {
			throw new RuntimeException(jobName + "未查找到任务");
		}
		return jobInfo;
	}
	
	/**
	 * 获取每个任务的处理详情
	 * @param jobName
	 * @return
	 */
	public static <R> List<TaskResult<R>> getTaskDetail(String jobName) {
		JobInfo<R> jobInfo = getJob(jobName);
		return jobInfo.getTaskDetail();
	}
	
	/**
	 * 获取工作的整体处理进度
	 * @param jobName
	 * @return
	 */
	public static <R> String getTaskProgess(String jobName) {
		JobInfo<R> job = getJob(jobName);
		return job.getTotalProcess();

	}


}
