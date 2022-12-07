package com.example.config;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import com.example.constants.AppConstants;
import com.example.constants.KafkaConfig;

public abstract class KafkaConfigUtils {
	
	static String saslJaasConfig = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"{0}\" password=\"{1}\";";
	
	/*
	 * Kafka Producer
	 */
	// producer config
	protected static <T> Map<String, Object> producerConfig(KafkaConfig kafkaConfig, String clientId, T t) {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.kafkaServerPort);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, t);
		configProps.put(ProducerConfig.ACKS_CONFIG, kafkaConfig.kafkaProducerAcks);
		configProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
		configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, Integer.parseInt(kafkaConfig.kafkaProducerMaxInFlightReqPerConn));
		configProps.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerConnMaxIdleMs));
		configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerBatchSize));
		configProps.put(ProducerConfig.LINGER_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerLingerMs));
		configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerBufferMemory));
		configProps.put(ProducerConfig.SEND_BUFFER_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerSendBufferBytes));
		configProps.put(ProducerConfig.RECEIVE_BUFFER_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerReceiveBufferBytes));
		configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaProducerMaxBlockMs));
		if (StringUtils.isNotBlank(kafkaConfig.kafkaProducerEnableIdempotenceFlag) && AppConstants.FLAG_Y.equals(kafkaConfig.kafkaProducerEnableIdempotenceFlag) && "all".equals(kafkaConfig.kafkaProducerAcks)) {
			configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		} else {
			configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
		}
		if (StringUtils.isNotBlank(kafkaConfig.kafkaProducerCompressionTypeFlag) && AppConstants.FLAG_Y.equals(kafkaConfig.kafkaProducerCompressionTypeFlag)) {
			configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "lz4");
		}
		
		if (StringUtils.isNotBlank(kafkaConfig.kafkaUsername) && StringUtils.isNotBlank(kafkaConfig.kafkaPassword)) {
			configProps.put("security.protocol", "SASL_SSL");
			configProps.put("sasl.mechanism", "SCRAM-SHA-512");
			configProps.put("sasl.jaas.config", MessageFormat.format(saslJaasConfig, kafkaConfig.kafkaUsername, kafkaConfig.kafkaPassword));
			configProps.put("ssl.truststore.location", kafkaConfig.kafkaTrustStoreLocation);
			configProps.put("ssl.truststore.password", kafkaConfig.kafkaTrustStorePassword);
		}
		
		return configProps;
	}
	
	protected static ProducerFactory<String, String> producerFactorySendMsg(KafkaConfig kafkaConfig, String clientId) throws Exception {
		return new DefaultKafkaProducerFactory<>(producerConfig(kafkaConfig, clientId, StringSerializer.class));
	}
	protected static ProducerFactory<String, String> producerFactorySendMsg(Map<String, Object> producerConfig) throws Exception {
		return new DefaultKafkaProducerFactory<>(producerConfig);
	}
	
	// kafka template
	protected static KafkaTemplate<String, String> kafkaTemplateSendMsg(ProducerFactory<String, String> producerFactory) throws Exception {
		return new KafkaTemplate<>(producerFactory);
	}
	
	
	/*
	 * Kafka Consumer
	 */
	// consumer config
	protected static Map<String, Object> consumerConfig(KafkaConfig kafkaConfig, String groupId, String clientId, Integer maxPollRecords, boolean allowAutoCreateTopicFlag, boolean enableAutoCommit) {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.kafkaServerPort);
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConfig.kafkaConsumerAutoOffsetReset);
		config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
		config.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, allowAutoCreateTopicFlag);
		config.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
		config.put(ConsumerConfig.RECEIVE_BUFFER_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerReceiveBufferBytes));
		config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerFetchMaxWaitMs));
		config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerFetchMinBytes));
		config.put(ConsumerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerConnMaxIdleMs));
		config.put(ConsumerConfig.SEND_BUFFER_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerSendBufferBytes));
		config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerPollMs));
		config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerFetchMaxBytes));
		config.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerMaxPartitionFetchBytes));
		config.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerHeartbeatIntervalMs));
		config.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, Integer.parseInt(kafkaConfig.kafkaConsumerSessionTimeoutMs));
		
		if (StringUtils.isNotBlank(kafkaConfig.kafkaUsername) && StringUtils.isNotBlank(kafkaConfig.kafkaPassword)) {
			config.put("security.protocol", "SASL_SSL");
			config.put("sasl.mechanism", "SCRAM-SHA-512");
			config.put("sasl.jaas.config", MessageFormat.format(saslJaasConfig, kafkaConfig.kafkaUsername, kafkaConfig.kafkaPassword));
			config.put("ssl.truststore.location", kafkaConfig.kafkaTrustStoreLocation);
			config.put("ssl.truststore.password", kafkaConfig.kafkaTrustStorePassword);
		}
		
		return config;
	}
	
	// consumer kafka
	protected static KafkaConsumer<String, String> kafKaConsumer(KafkaConfig kafkaConfig, String groupId, String clientId, Integer maxPollRecords, boolean allowAutoCreateTopicFlag, boolean enableAutoCommit) throws Exception {
		return new KafkaConsumer<>(consumerConfig(kafkaConfig, groupId, clientId, maxPollRecords, allowAutoCreateTopicFlag, enableAutoCommit));
	}
	protected static KafkaConsumer<String, String> kafKaConsumer(Map<String, Object> config) throws Exception {
		return new KafkaConsumer<>(config);
	}
	
	// consumer factory for consumer reply kafka
	protected static ConsumerFactory<String, String> consumerFactory(KafkaConfig kafkaConfig, String groupId, String clientId, Integer maxPollRecords, boolean allowAutoCreateTopicFlag, boolean enableAutoCommit) throws Exception {
		return new DefaultKafkaConsumerFactory<>(consumerConfig(kafkaConfig, groupId, clientId, maxPollRecords, allowAutoCreateTopicFlag, enableAutoCommit));
	}
	protected ConsumerFactory<String, String> consumerFactory(Map<String, Object> config) throws Exception {
		return new DefaultKafkaConsumerFactory<>(config);
	}
	
	// Concurrent Kafka Listener Container Factory
	protected static ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory,  Integer concurrency) throws Exception {
	    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory);
	    factory.setConcurrency(1);
	    if (null != concurrency) {
	    	factory.setConcurrency(concurrency);
	    }
	    return factory;
	}
	protected static ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory, KafkaTemplate<String, String> kafkaTemplate) throws Exception {
	    ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory);
	    factory.setReplyTemplate(kafkaTemplate);
	    factory.setConcurrency(1);
	    factory.setAutoStartup(true);
	    return factory;
	}
	
	// Concurrent Message Listener Container
	protected static ConcurrentMessageListenerContainer<String, String> concurrentMessageListenerContainer(ConcurrentKafkaListenerContainerFactory<String, String> factory, String replyTopic, String groupIdReplyTopic) throws Exception {
		ConcurrentMessageListenerContainer<String, String> replyContainer = factory.createContainer(replyTopic);
		replyContainer.getContainerProperties().setMissingTopicsFatal(true);
		replyContainer.getContainerProperties().setGroupId(groupIdReplyTopic);
		replyContainer.setAutoStartup(true);
		return replyContainer;
	}
}
