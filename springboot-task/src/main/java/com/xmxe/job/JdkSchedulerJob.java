package com.xmxe.job;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JdkSchedulerJob {

	public void timer(){
		// 第一次任务延迟时间
		long delay = 2000;
		// 任务执行频率
		long period = 3 * 1000;
		// 执行的时间
		Date time = new Date();

		Timer timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				System.out.println(1);
			}
		};
		//延时后执行一次
		timer.schedule(timerTask,delay);
		// 指定时间执行一次
		timer.schedule(timerTask,time);
		// 延时后定时执行
		timer.schedule(timerTask,delay,period);
		// 指定首次运行时间
		timer.schedule(timerTask,time, period);
		//与schedule比较当任务执行时间大于任务执行周期时 scheduler会按照以前的执行周期继续跑，
		// 而scheduleAtFixedRate会把以前未执行完的任务一次性全补跑完后在老实按照任务周期继续跑
		timer.scheduleAtFixedRate(timerTask,delay,period);
		timer.scheduleAtFixedRate(timerTask,time,period);
		// 终止并移除任务
		timer.cancel();
		// 从队列中删除所有已取消的任务
		timer.purge();
	}

	public void threadPoolTask(){
		ScheduledExecutorService pool = Executors.newScheduledThreadPool(10);

		/**
		 * schedule：只执行一次调度；
		 *
		 * scheduleAtFixedRate：如果执行时间过长，下一次调度会延迟，不会同时执行
		 * 是以上一个任务开始的时间计时，period时间过去后，检测上一个任务是否执行完毕，如果上一个任务执行完毕，则当前任务立即执行，
		 * 如果上一个任务没有执行完毕，则需要等上一个任务执行完毕后立即执行；
		 *
		 * scheduleWithFixedDelay：上一次任务执行完后再加上period时间 执行 是以上一个任务结束时开始计时，period时间过去后立即执行
		 */
		pool.scheduleAtFixedRate(() -> {
			System.out.println("---------");
		}, 2, 3, TimeUnit.SECONDS);// 延迟2s后开始执行 每3s执行一次
	}
}
