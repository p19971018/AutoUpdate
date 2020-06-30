package com.wangchl.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
/**
 * 可重入式独占锁
 * @author wangchl
 * 
 * 线程可以重复进入任何一个她已经拥有的锁所同步着的代码块
 *
 */
public class ReenterSelfLock implements Lock{

	private static class Sync extends AbstractQueuedSynchronizer{
		
		private static final long serialVersionUID = 1L;

		// 是否处于占用状态
		protected boolean isHeldExclusively() {
			return getState()>0;
		}
		// 当状态为0的时候获取锁
		@Override
		public boolean tryAcquire(int acquires) {
			if(compareAndSetState(0, 1)) {
				setExclusiveOwnerThread(Thread.currentThread());
				return true;
			}else if(getExclusiveOwnerThread() == Thread.currentThread()) {
				setState(getState()+1);
				return true;
			}
			return false;
		}
		 // 释放锁，将状态设置为0
		@Override
		protected boolean tryRelease(int arg) {
			if(getExclusiveOwnerThread() != Thread.currentThread()) {
				throw new IllegalMonitorStateException();
			}
			if(getState() == 0 ) {
				throw new IllegalMonitorStateException();
			}
			setState(getState()-1);
			if(getState() == 0) {
				setExclusiveOwnerThread(null);
			}
			return true;
		}
		
		// 返回一个Condition，每个condition都包含了一个condition队列
		Condition newCondition() {
			return new ConditionObject();
		}
		
	}
	
	// 仅需要将操作代理到Sync上即可
	private final Sync sync = new Sync();
			
	/**
	 * 以独占模式获取，忽略中断
	 */
	public void lock() {
		sync.acquire(1);
	}

	/**
	 * 以独占锁方式获取，如果被中断则终止
	 */
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
		
	}

	/**
	 * 获取锁
	 */
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	/**
	 * 在指定时间范围内获取锁
	 */
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	/**
	 * 释放锁
	 */
	public void unlock() {
		sync.release(1);
	}

	/**
	 * Condition 必须要配合锁一起使用，因为对共享变量的访问发生在多线程换将下。
	 * 一个Condition 的实例必须与一个 Lock 绑定，因此Condition 一般都是作为Lock的一个内部实现
	 */
	public Condition newCondition() {
		
		return sync.newCondition();
	}

}

