package com.example.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {

	@Value("${kafka.topic}")
	public String kafkaTopic;
	
	@Value("${kafka.server.port}")
	public String kafkaServerPort;

	@Value("${kafka.username}")
	public String kafkaUsername;

	@Value("${kafka.password}")
	public String kafkaPassword;

	@Value("${kafka.truststore.location}")
	public String kafkaTrustStoreLocation;

	@Value("${kafka.truststore.password}")
	public String kafkaTrustStorePassword;
	
	@Value("${kafka.producer.acks}")
	public String kafkaProducerAcks;
	
	@Value("${kafka.producer.enable.idempotence.flag}")
	public String kafkaProducerEnableIdempotenceFlag;
	
	@Value("${kafka.producer.max.in.flight.req.per.conn}")
	public String kafkaProducerMaxInFlightReqPerConn;
	
	@Value("${kafka.producer.conn.max.idle.ms}")
	public String kafkaProducerConnMaxIdleMs;
	
	@Value("${kafka.producer.batch.size}")
	public String kafkaProducerBatchSize;
	
	@Value("${kafka.producer.linger.ms}")
	public String kafkaProducerLingerMs;
	
	@Value("${kafka.producer.buffer.memory}")
	public String kafkaProducerBufferMemory;
	
	@Value("${kafka.producer.compressiontype.flag}")
	public String kafkaProducerCompressionTypeFlag;
	
	@Value("${kafka.producer.max.block.ms}")
	public String kafkaProducerMaxBlockMs;
	
	@Value("${kafka.producer.send.buffer.bytes}")
	public String kafkaProducerSendBufferBytes;
	
	@Value("${kafka.producer.receive.buffer.bytes}")
	public String kafkaProducerReceiveBufferBytes;
	
	@Value("${kafka.consumer.thread}")
	public String kafkaConsumerThread;
	
	@Value("${kafka.consumer.poll.ms}")
	public String kafkaConsumerPollMs;
	
	@Value("${kafka.consumer.max.poll.records}")
	public String kafkaConsumerMaxPollRecords;
	
	@Value("${kafka.consumer.conn.max.idle.ms}")
	public String kafkaConsumerConnMaxIdleMs;
	
	@Value("${kafka.consumer.send.buffer.bytes}")
	public String kafkaConsumerSendBufferBytes;
	
	@Value("${kafka.consumer.receive.buffer.bytes}")
	public String kafkaConsumerReceiveBufferBytes;
	
	@Value("${kafka.consumer.fetch.max.wait.ms}")
	public String kafkaConsumerFetchMaxWaitMs;
	
	@Value("${kafka.consumer.fetch.min.bytes}")
	public String kafkaConsumerFetchMinBytes;
	
	@Value("${kafka.consumer.fetch.max.bytes}")
	public String kafkaConsumerFetchMaxBytes;
	
	@Value("${kafka.consumer.max.partition.fetch.bytes}")
	public String kafkaConsumerMaxPartitionFetchBytes;
	
	@Value("${kafka.consumer.heartbeat.interval.ms}")
	public String kafkaConsumerHeartbeatIntervalMs;
	
	@Value("${kafka.consumer.session.timeout.ms}")
	public String kafkaConsumerSessionTimeoutMs;
	
	@Value("${kafka.consumer.auto.offset.reset}")
	public String kafkaConsumerAutoOffsetReset;

}
