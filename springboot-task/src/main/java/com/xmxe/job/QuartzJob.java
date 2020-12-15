package com.xmxe.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuartzJob extends QuartzJobBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("jobExecutionContext->{}",jobExecutionContext.getJobDetail().getJobDataMap());
    }
}
