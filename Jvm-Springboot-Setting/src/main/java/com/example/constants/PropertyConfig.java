package com.example.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyConfig {

	@Value("${pod.name}")
	public String podName;

	@Value("${cluster.name}")
	public String clusterName;
	
	@Value("${app.log.level}")
	public String appLogLevel;

	@Value("${redis.max.pool.total}")
	public String redisMaxPoolTotal;

	@Value("${redis.max.pool.idle}")
	public String redisMaxPoolIdle;

	@Value("${redis.min.pool.idle}")
	public String redisMinPoolIdle;

	@Value("${redis.min.evictable.idle.time.millis}")
	public String redisMinEvictableIdleTimeMillis;

	@Value("${redis.time.between.eviction.runs.millis}")
	public String redisTimeBetweenEvictionRunsMillis;

	@Value("${redis.num.tests.per.eviction.run}")
	public String redisNumTestsPerEvictionRun;

	@Value("${redis.sentinel.address}")
	public String redisSentinelAddress;

	@Value("${redis.master.name}")
	public String redisMasterName;

	@Value("${redis.password:#{null}}")
	public String redisPassword;

	@Value("${redis.sentinel.password:#{null}}")
	public String redisSentinelPassword;
	
	@Value("${redis.max.wait.millis}")
	public String redisMaxWaitMillis;
}
