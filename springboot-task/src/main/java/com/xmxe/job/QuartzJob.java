package com.xmxe.job;

import com.xmxe.service.DataSourceService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
public class QuartzJob extends QuartzJobBean {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DataSourceService dataSourceService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dataSourceService.readMasterByDS();
        dataSourceService.readSlaveByDs();
        logger.info("jobExecutionContext->{}",jobExecutionContext.getJobDetail().getJobDataMap());
    }



}
