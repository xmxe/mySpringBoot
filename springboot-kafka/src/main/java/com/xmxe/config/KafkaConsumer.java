package com.xmxe.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class KafkaConsumer {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@KafkaListener(topics = {"big_data_topic"})
	public void consumer(ConsumerRecord<?,?> consumerRecord){
		//判断是否为null
		Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
		log.info(">>>>>>>>>> record =" + kafkaMessage);
		if(kafkaMessage.isPresent()){
			//得到Optional实例中的值
			Object message = kafkaMessage.get();
			System.err.println("消费消息:"+message);
		}
	}

}
