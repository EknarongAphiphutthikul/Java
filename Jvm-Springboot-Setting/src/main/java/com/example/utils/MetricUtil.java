package com.example.utils;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.example.bean.MetricBean;
import com.example.constants.AppConstants;
import com.example.constants.LogConstants.LogLevel;
import com.example.constants.MetricConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MetricUtil {
	
	private static String logLevel = System.getProperty("APP_LOG_LEVEL");
	private static Logger logger = LoggerFactory.getLogger(MetricConstants.LOG_NAME);
	private static Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'").create();
	private static boolean logPerformanceEnable = AppConstants.FLAG_E.equals(System.getProperty("LOG_PERFORMANCE_ENABLE", "D"));
	
	public static void log(String logLevel, String metricName, Long metricValue) {
		if (!MetricUtil.logPerformanceEnable) {
			return;
		}
		MetricBean metric = new MetricBean(logLevel, metricName, metricValue);
		
		logToJson(metric);
	}
	
	public static void log(String logLevel, Date date, String metricName, Long metricValue) {
		if (!MetricUtil.logPerformanceEnable) {
			return;
		}
		MetricBean metric = new MetricBean(logLevel, metricName, metricValue);
		metric.setLogDatetime(date);
		
		logToJson(metric);
	}
	
	private static void logToJson(MetricBean metric) {
		if (null == metric) {
			return;
		}
		if (checkLogLevel(metric.getLogLevel())) {
			logger.info(gson.toJson(metric));
		}
	}
			

	public static String getThreadContext(String key) {
		return MDC.get(key);
	}
	
	private static boolean checkLogLevel(String logLevel) {
		if (StringUtils.isBlank(logLevel)) {
			return false;
		}
		if (LogLevel.DEBUG.equalsIgnoreCase(MetricUtil.logLevel)) {
			return true;
		}
		if (LogLevel.INFO.equalsIgnoreCase(MetricUtil.logLevel)) {
			if (LogLevel.DEBUG.equals(logLevel)) {
				return false;
			}
			return true;
		}
		if (LogLevel.WARN.equalsIgnoreCase(MetricUtil.logLevel)) {
			if (LogLevel.DEBUG.equals(logLevel) || LogLevel.INFO.equals(logLevel)) {
				return false;
			}
			return true;
		}
		if (LogLevel.ERROR.equalsIgnoreCase(MetricUtil.logLevel)) {
			if (LogLevel.ERROR.equals(logLevel) || LogLevel.FATAL.equals(logLevel)) {
				return true;
			}
			return false;
		}
		if (LogLevel.FATAL.equalsIgnoreCase(MetricUtil.logLevel)) {
			if (LogLevel.FATAL.equals(logLevel)) {
				return true;
			}
			return false;
		}
		return false;
	}
}
