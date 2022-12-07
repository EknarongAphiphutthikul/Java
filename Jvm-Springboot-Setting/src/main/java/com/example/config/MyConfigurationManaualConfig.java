package com.example.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.example.constants.KafkaConfig;
import com.example.constants.PropertyConfig;

@Configuration
public class MyConfigurationManaualConfig extends KafkaConfigUtils {
	
	@Autowired
	private PropertyConfig propertyConstants;
	@Autowired
	private KafkaConfig kafkaConfig;
	
	@Bean("initProducerFactorySendMsg")
	public ProducerFactory<String, String> initProducerFactorySendMsg() throws Exception {
		String clientId = propertyConstants.clusterName + "_" + propertyConstants.podName;
		getKafkaServerPort();
		return producerFactorySendMsg(kafkaConfig, clientId + "-send-msg");
	}

	@Bean("kafkaTemplateSendMsg")
	public KafkaTemplate<String, String> initReplyingTemplateSendMsg(@Qualifier("initProducerFactorySendMsg") ProducerFactory<String, String> producerFactory) throws Exception {
		return kafkaTemplateSendMsg(producerFactory);
	}
	
	private String getKafkaServerPort() throws Exception {
		String serverPort = kafkaConfig.kafkaServerPort;
		if (StringUtils.isBlank(serverPort)) {
			throw new RuntimeException("KAFKA_SERVER_PORT is blank.");
		}
		return serverPort;
	}
}
