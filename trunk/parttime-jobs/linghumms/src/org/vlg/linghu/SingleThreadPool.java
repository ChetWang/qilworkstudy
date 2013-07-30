package org.vlg.linghu;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleThreadPool {

	private static final Logger logger = LoggerFactory
			.getLogger(SingleThreadPool.class);

	private static final ThreadPoolExecutor single = new ThreadPoolExecutor(1,
			1, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(50000),
			new VLGThreadFactory("single-thread-executor"),
			getRejectedExecutionHandler());

	protected static RejectedExecutionHandler getRejectedExecutionHandler() {
		return new RejectedExecutionHandler() {

			public void rejectedExecution(Runnable r,
					ThreadPoolExecutor executor) {
				logger.warn("Threadpool for processor Working queue is full, rejectedExecution happened, will execute runnable in current thread!");
				// CallerRunPolicy
				if (!executor.isShutdown()) {
					r.run();
				}
			}
		};
	}

	public synchronized static void execute(Runnable run) {
		single.execute(run);
	}

	public static class VLGThreadFactory implements ThreadFactory {
		final ThreadGroup group;
		final AtomicInteger threadNumber = new AtomicInteger(1);
		final String namePrefix;

		public VLGThreadFactory(String namePreifx) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null ? s.getThreadGroup() : Thread
					.currentThread().getThreadGroup());
			this.namePrefix = namePreifx;
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(this.group, r, this.namePrefix
					+ this.threadNumber.getAndIncrement(), 0L);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != 5)
				t.setPriority(5);
			return t;
		}
	}

}
