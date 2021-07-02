package com.xmxe.spring;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service("kafkaProducer")
public class KafkaProduce {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void testSend(){
		for (int i = 0; i < 5000; i++) {
			Map<String, Object> map = new LinkedHashMap<>();
			map.put("datekey", 20210610);
			map.put("userid", i);
			map.put("salaryAmount", i);
			//向kafka的big_data_topic主题推送数据
			kafkaTemplate.send("big_data_topic", JSONObject.toJSONString(map));
		}
	}
}
