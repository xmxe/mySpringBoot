package com.xmxe.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Jobs implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.err.println("----------");
	}

}
