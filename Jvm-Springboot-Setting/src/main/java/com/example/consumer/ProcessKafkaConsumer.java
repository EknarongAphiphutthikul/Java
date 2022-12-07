package com.example.consumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.config.KafkaConfigUtils;
import com.example.constants.KafkaConfig;
import com.example.constants.MetricConstants;
import com.example.constants.PropertyConfig;
import com.example.constants.LogConstants.LogLevel;
import com.example.service.ProcessService;
import com.example.utils.CalendarUtil;
import com.example.utils.MessageFormatUtil;
import com.example.utils.MetricUtil;
import com.example.utils.TxnLogProcessUtil;

@Component
public class ProcessKafkaConsumer extends KafkaConfigUtils {
	
	private Long consumerPoll;
	private int consumerMaxPollRecords;
	private String topic;
	private String groupId;
	private ExecutorService executor;
	private final CountDownLatch latch;
	private ThreadPoolExecutor executorWorker;
	private KafkaConsumer<String, String> consumer;
	
	@Value("${kafka.commitSync.enable:false}")
	private boolean booleanKafkaCommitSync;
	@Autowired
	private PropertyConfig propertyConfig;
	@Autowired
	private KafkaConfig kafkaConfig;
	@Autowired
	private ProcessService procesService;
	
	public ProcessKafkaConsumer() {
		latch = new CountDownLatch(1);
	}
	
	public ThreadPoolExecutor getExecutorServiceWorker() {
		return this.executorWorker;
	}

	@PostConstruct
	public void start() throws Exception {
		prepareConfig();
		startListening();
	}
	
	private void prepareConfig() throws Exception {
		TxnLogProcessUtil.logInfoLogBean(LogLevel.INFO, "Kafka Config : " + MessageFormatUtil.getJsonFromObjectWithDateFormat(kafkaConfig));
		TxnLogProcessUtil.logInfoLogBean(LogLevel.INFO, "kafka.commitSync.enable : " + booleanKafkaCommitSync);
		consumerPoll = Long.parseLong(StringUtils.defaultIfEmpty(kafkaConfig.kafkaConsumerPollMs, "1800"));
		consumerMaxPollRecords = Integer.parseInt(StringUtils.defaultIfEmpty(kafkaConfig.kafkaConsumerMaxPollRecords, "20"));
		topic = kafkaConfig.kafkaTopic;
		groupId = "jvm-springboot-setting";
		executor = Executors.newSingleThreadExecutor();
		executorWorker = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(StringUtils.defaultIfEmpty(kafkaConfig.kafkaConsumerThread, "5")));
		executorWorker.prestartAllCoreThreads();
		consumer = kafKaConsumer(kafkaConfig, groupId, propertyConfig.clusterName + "_" + propertyConfig.podName, consumerMaxPollRecords, false, false);
	}
	
	private void startListening() {
		executor.submit(getListenTask());
	}
	
	private Runnable getListenTask() {
		return () -> {
			consumer.subscribe(Collections.singletonList(topic));
			try {
				while (true) {
				    try {
				        pollRecords();
				    }catch (InterruptedException ex) {
				        TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "InterruptedException : ", ex);
				        Thread.currentThread().interrupt();
				    } catch (ExecutionException ex) {
				        TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "ExecutionException : ", ex);
				    }
				}
			} catch (WakeupException ex) {
				TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "WakeupException : ", ex);
			} catch (Exception ex) {
				TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "Exception : ", ex);
			} finally {
				if (!this.booleanKafkaCommitSync) {
					try {
						consumer.commitSync();
					} catch (Exception e) {
						TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "consumer.commitSync() Exception : ", e);
					}
				}
				consumer.close();
				latch.countDown();
			}
		};
	}
	
	private void pollRecords() throws InterruptedException, ExecutionException {
		ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(consumerPoll));
		if (records.count() != 0) {
			Date now = CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime();
			if (this.booleanKafkaCommitSync) {
				consumer.commitSync();
			} else {
				consumer.commitAsync();
			}
			Long commitTime = CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime().getTime() - now.getTime();
			MetricUtil.log(LogLevel.INFO, now, MetricConstants.CONSUMER_TIME_COMMIT_TOPIC + this.topic, commitTime);
			Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
			List<Callable<Void>> task = new ArrayList<>();
			while (iterator.hasNext()) {
				ConsumerRecord<String, String> record = iterator.next();
				task.add(new Callable<Void>() {
					public Void call() throws Exception {
						process(record.value(), now, commitTime);
				        return null;
				    }
				});
			}
			
			executorWorker.invokeAll(task);
		}			
	}
	
	private void process(String message, Date startDateTime, Long commitTime) {
		procesService.process(message, startDateTime, commitTime);
	}
	
	@PreDestroy
	private void addShutDownHook() {
		stopConsumer();
		closeExecutor();
	}

	public void stopConsumer() {
		consumer.wakeup();
		try {
			latch.await();
		} catch (InterruptedException ex) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "InterruptedException : ", ex);
			Thread.currentThread().interrupt();
		}
	}

	public void closeExecutor() {
		executorWorker.shutdownNow();
		executor.shutdown();
		try {
			if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
				executor.shutdownNow();
			}
		} catch (InterruptedException ex) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "InterruptedException : ", ex);
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
