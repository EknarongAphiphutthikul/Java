package com.example.redis;

import redis.clients.jedis.Jedis;

public class JedisManager extends JedisPoolConfiguration {

	private Integer index = null;
	
	public JedisManager(Integer maxTotal, Integer maxIdle, Integer minIdle, Integer minEvictableIdleTimeMillis, Integer timeBetweenEvictionRunsMillis, Integer numTestsPerEvictionRun, String sentinelAddress, String masterName, String password, Integer index, String sentinelPassword, Integer maxWaitMillis) {
		super(maxTotal, maxIdle, minIdle, minEvictableIdleTimeMillis, timeBetweenEvictionRunsMillis, numTestsPerEvictionRun, sentinelAddress, masterName, password, sentinelPassword, maxWaitMillis);
		this.index = index;
	}

	public void setKeyValue(String key, String data) throws Exception {
		setKeyValueAndExpire(key, data, -1);
	}

	public void setKeyValueAndExpire(String key, String data, int expireSec) throws Exception {
		Jedis jedis = null;
		try {
			jedis = getConnectionPool();
			if (expireSec >= 0) {
				jedis.setex(key, expireSec, data);
			} else {
				jedis.set(key, data);
			}
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}

	}

	public String getValueAndDelete(String key) throws Exception {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getConnectionPool();
			result = jedis.get(key);
			jedis.del(key);
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}

	public String getValue(String key) throws Exception {
		return getValue(key, -1);
	}
	
	public String getValue(String key, int expireSec) throws Exception {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = getConnectionPool();
			result = jedis.get(key);
			if (expireSec >= 0) {
				jedis.expire(key, expireSec);
			}
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
		return result;
	}
	
	public void delKey(String key) throws Exception {
		Jedis jedis = null;
		try {
			jedis = getConnectionPool();
			jedis.del(key);
		} finally {
			if (null != jedis) {
				jedis.close();
			}
		}
	}
	
	public Jedis getConnectionPool() throws Exception {
		if (null == index) {
			throw new Exception("index is null");
		}
		Jedis jedis = getJedisConnectionPool();
		jedis.select(index);
		return jedis;
	}
}
