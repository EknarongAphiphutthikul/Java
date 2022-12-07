package com.example.utils;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.example.constants.KafkaConfig;
import com.example.constants.LogConstants.LogLevel;
import com.example.constants.MetricConstants;

@Component
public class KafkaSenderUtils {

	@Autowired
	@Qualifier("kafkaTemplateSendMsg")
	private KafkaTemplate<String, String> kafkaTemplateSendMsg;

	@Autowired
	private KafkaConfig kafkaConfig;

	private String topicSend = null;

	@PostConstruct
	public void initKafkaSenderUtils() throws Exception {
		this.topicSend = this.kafkaConfig.kafkaTopic;

		if (StringUtils.isBlank(this.topicSend)) {
			throw new RuntimeException("KAFKA_TOPIC is blank.");
		}
		
		this.topicSend = this.topicSend.trim();
	}

	public void sendToTopic(String inputJsonMsg) {
		try {
			Message<String> message = MessageBuilder
					.withPayload(inputJsonMsg)
					.setHeader(KafkaHeaders.TOPIC, this.topicSend)
					.build();

			Date start = CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime();
			ListenableFuture<SendResult<String, String>> future = this.kafkaTemplateSendMsg.send(message);
			future.addCallback(new KafkaSendCallback<String, String>() {
				@Override
				public void onSuccess(SendResult<String, String> result) {
					Date temp = CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime();
					Long useTimeSend = temp.getTime() - start.getTime();
					String topic = result.getRecordMetadata().topic();
					String partition = Integer.toString(result.getRecordMetadata().partition());
					MetricUtil.log(LogLevel.INFO, temp, MetricConstants.SEND_TIME_TOPIC + topic, useTimeSend);
					MetricUtil.log(LogLevel.INFO, temp, MetricConstants.SEND_TIME_TOPIC_PAR + topic + "-" + partition, useTimeSend);
				}

				@Override
				public void onFailure(KafkaProducerException ex) {
					TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "sendToTopic Exception : ", ex);
					
					ProducerRecord<String, String> failed = ex.getFailedProducerRecord();
					TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "sendToTopic Fail Msg : " + failed.value());
				}

			});
		} catch (Exception e) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "sendToTopic Exception : ", e);
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "sendToTopic Fail Msg : " + inputJsonMsg);
		}
	}
}
