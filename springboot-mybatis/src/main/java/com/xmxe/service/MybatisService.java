package com.xmxe.service;

import com.xmxe.entity.User;
import com.xmxe.mapper.master.MasterMapper;
import com.xmxe.mapper.slave.SlaveMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class MybatisService {

	@Resource
	MasterMapper masterMapper;
	
	@Resource
	SlaveMapper slaveMapper;


	public User getUserById(Integer userId) {
		Map<String,Object> user = slaveMapper.getUserById(userId);
		System.out.println("slaveDB------"+user);
		return masterMapper.getUserById(userId);
	}

}
