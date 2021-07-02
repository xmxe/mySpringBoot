package com.xmxe.spring;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BigDataTopicListener {
	private static final Logger log = LoggerFactory.getLogger(BigDataTopicListener.class);

	/**
	 * 监听kafka数据（批量消费）
	 * @param consumerRecords
	 * @param ack
	 */
	@KafkaListener(topics = {"big_data_topic"}, containerFactory = "batchFactory")
	public void batchConsumer(List<ConsumerRecord<?, ?>> consumerRecords, Acknowledgment ack) {
		long start = System.currentTimeMillis();

		//...
		//db.batchSave(consumerRecords);//批量插入或者批量更新数据

		//手动提交
		ack.acknowledge();
		log.info("收到bigData推送的数据，拉取数据量：{}，消费时间：{}ms", consumerRecords.size(), (System.currentTimeMillis() - start));
	}

}
