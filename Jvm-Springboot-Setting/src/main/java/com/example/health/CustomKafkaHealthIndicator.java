package com.example.health;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.example.constants.KafkaConfig;
import com.example.constants.LogConstants.LogLevel;
import com.example.utils.TxnLogProcessUtil;

@Component("custom-kafka")
@ConditionalOnProperty(value="management.health.custom-kafka.enabled", matchIfMissing = true)
@DependsOn("PropertyConfigHelper")
public class CustomKafkaHealthIndicator extends AbstractHealthIndicator {
	
	private boolean up = false;
	private int count = Integer.parseInt(System.getProperty("CUSTOM_KAFKA_CHECK_HEALTH_COUNT", "2"));
	
	@Autowired
	private KafkaConfig kafkaConfig;

	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		if (!this.up && this.count > 0) {
			Set<String> topicList = new HashSet<>(Arrays.asList(kafkaConfig.kafkaTopic));
			Properties properties = new Properties();
			properties.put("bootstrap.servers", kafkaConfig.kafkaServerPort);
			try (AdminClient client = AdminClient.create(properties)) {
				ListTopicsResult resultTopic = client.listTopics();
				for (String topicName : resultTopic.names().get()) {
					if (topicList.contains(topicName)) {
						topicList.remove(topicName);
					}
				}
				if (topicList.isEmpty()) {
					this.up = true;
				} else {
					TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "Topic missing : " + topicList);
				}
			} catch (Exception e) {
				this.up = false;
			}
			this.count--;
		}
		if (!this.up) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "CustomKafkaHealthIndicator Status.DOWN");
		}
		builder.status(this.up ? Status.UP : Status.DOWN);
	}

}
