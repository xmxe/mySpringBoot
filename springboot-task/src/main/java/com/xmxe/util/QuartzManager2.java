package com.xmxe.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.util.Map;

//@Component
public class QuartzManager2 {

    private final Logger log = LogManager.getLogger(this.getClass());

//	@Autowired
	private Scheduler scheduler;

    /**
     * 添加任务可以传参数
     * @param clazzName
     * @param jobName
     * @param groupName
     * @param cronExp
     * @param param
     */
	public void addJob(String clazzName, String jobName, String groupName, String cronExp, Map<String, Object> param) {
		try {
			// 启动调度器，默认初始化的时候已经启动
//            scheduler.start();
			//构建job信息
			Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName(clazzName);
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, groupName).build();
			//表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
			//按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).withSchedule(scheduleBuilder).build();
			//获得JobDataMap，写入数据
			if (param != null) {
				trigger.getJobDataMap().putAll(param);
			}
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			log.error("创建任务失败", e);
		}
	}

    /**
     * 暂停任务
     * @param jobName
     * @param groupName
     */
	public void pauseJob(String jobName, String groupName) {
		try {
			scheduler.pauseJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			log.error("暂停任务失败", e);
		}
	}

    /**
     * 恢复任务
     * @param jobName
     * @param groupName
     */
	public void resumeJob(String jobName, String groupName) {
		try {
			scheduler.resumeJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			log.error("恢复任务失败", e);
		}
	}

    /**
     * 立即运行一次定时任务
     * @param jobName
     * @param groupName
     */
	public void runOnce(String jobName, String groupName) {
		try {
			scheduler.triggerJob(JobKey.jobKey(jobName, groupName));
		} catch (SchedulerException e) {
			log.error("立即运行一次定时任务失败", e);
		}
	}

    /**
     * 更新任务
     * @param jobName
     * @param groupName
     * @param cronExp
     * @param param
     */
	public void updateJob(String jobName, String groupName, String cronExp, Map<String, Object> param) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobName, groupName);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (cronExp != null) {
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExp);
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			}
			//修改map
			if (param != null) {
				trigger.getJobDataMap().putAll(param);
			}
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (Exception e) {
			log.error("更新任务失败", e);
		}
	}

    /**
     * 删除任务
     * @param jobName
     * @param groupName
     */
	public void deleteJob(String jobName, String groupName) {
		try {
			//暂停、移除、删除
			scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, groupName));
			scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, groupName));
			scheduler.deleteJob(JobKey.jobKey(jobName, groupName));
		} catch (Exception e) {
			log.error("删除任务失败", e);
		}
	}

    /**
     * 启动所有任务
     */
	public void startAllJobs() {
		try {
			scheduler.start();
		} catch (Exception e) {
			log.error("开启所有的任务失败", e);
		}
	}

    /**
     * 暂停所有任务
     */
	public void pauseAllJobs() {
		try {
			scheduler.pauseAll();
		} catch (Exception e) {
			log.error("暂停所有任务失败", e);
		}
	}

    /**
     * 恢复所有任务
     */
	public void resumeAllJobs() {
		try {
			scheduler.resumeAll();
		} catch (Exception e) {
			log.error("恢复所有任务失败", e);
		}
	}
    /**
     * 关闭所有任务
     */
	public void shutdownAllJobs() {
		try {

			if (!scheduler.isShutdown()) {
				// 需谨慎操作关闭scheduler容器
				// scheduler生命周期结束，无法再 start() 启动scheduler
				scheduler.shutdown(true);
			}
		} catch (Exception e) {
			log.error("关闭所有的任务失败", e);
		}
	}

}
