package com.example.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.constants.PropertyConfig;
import com.example.redis.JedisManager;

@Configuration
public class RedisConfig {

	public static JedisManager jedisManager = null;
	
	@Bean
	public JedisManager createJedisManager(PropertyConfig propertyConstants) throws Exception {
		checkConfig(propertyConstants);
		Integer maxTotal = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisMaxPoolTotal, "10"));
		Integer maxIdle = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisMaxPoolIdle, "10"));
		Integer minIdle = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisMinPoolIdle, "10"));
		Integer minEvictableIdleTimeMillis = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisMinEvictableIdleTimeMillis, "60000"));
		Integer timeBetweenEvictionRunsMillis = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisTimeBetweenEvictionRunsMillis, "30000"));
		Integer numTestsPerEvictionRun = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisNumTestsPerEvictionRun, "3"));
		Integer maxWaitMillis = Integer.parseInt(StringUtils.defaultIfEmpty(propertyConstants.redisMaxWaitMillis, "10000"));
		String sentinelAddress = propertyConstants.redisSentinelAddress;
		String masterName = propertyConstants.redisMasterName;
		String password = propertyConstants.redisPassword;
		String sentinelPassword = propertyConstants.redisSentinelPassword;
		Integer index = 0;
		RedisConfig.jedisManager = new JedisManager(maxTotal, maxIdle, minIdle, minEvictableIdleTimeMillis, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, sentinelAddress, masterName, password, index, sentinelPassword, maxWaitMillis);
		return RedisConfig.jedisManager;
	}
	
	private void checkConfig(PropertyConfig propertyConstants) throws Exception {
		if (StringUtils.isBlank(propertyConstants.redisSentinelAddress)) {
			throw new Exception("Not found Env Key : REDIS_SENTINEL_ADDRESS");
		}
		if (StringUtils.isBlank(propertyConstants.redisMasterName)) {
			throw new Exception("Not found Env Key : REDIS_MASTER_NAME");
		}
		if (StringUtils.isBlank(propertyConstants.redisPassword)) {
			throw new Exception("Not found Env Key : REDIS_PASSWORD");
		}
		if (StringUtils.isBlank(propertyConstants.redisSentinelPassword)) {
			throw new Exception("Not found Env Key : REDIS_SENTINEL_PASSWORD");
		}
	}
}
