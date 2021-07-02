package com.xmxe.general;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerCall {
 
    public static void main(String[] args) {
    	 Properties properties = new Properties();
         properties.put("bootstrap.servers", "192.168.236.128:9092");
         properties.put("group.id", "group-1");
         properties.put("enable.auto.commit", "true");
         properties.put("auto.commit.interval.ms", "1000");
         properties.put("auto.offset.reset", "earliest");
         properties.put("session.timeout.ms", "30000");
         properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
         properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

         KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
         kafkaConsumer.subscribe(Arrays.asList("HelloWorld"));
         while (true) {
             ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
             for (ConsumerRecord<String, String> record : records) {
                 System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                 System.out.println();
             }
         }
    }

}
