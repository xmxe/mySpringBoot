package com.xmxe.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
//@DS("dsName") //dsName可以为组名也可以为具体某个库的名称 可以注解在方法上或类上，同时存在就近原则 方法上注解 优先于 类上注解。
public class DataSourceService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 方法之间的内部调用不能切换数据源
	 */
	@DS("slave")
	public void readSlaveByDs() {
		List<Map<String,Object>> user_detail = jdbcTemplate.queryForList("select * from tbluser");
		logger.info("slave--{}",user_detail);
	}

	@DS("master")
	public void readMasterByDS(){
		List<Map<String,Object>> job_detail = jdbcTemplate.queryForList("select * from qrtz_job_details");
		logger.info("master--{}",job_detail);
	}
}
