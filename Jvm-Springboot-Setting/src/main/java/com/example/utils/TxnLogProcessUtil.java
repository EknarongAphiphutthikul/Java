package com.example.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.example.bean.InfoLogBean;
import com.example.bean.LogBean;
import com.example.bean.TxnLogBean;
import com.example.constants.AppConstants;
import com.example.constants.LogConstants;
import com.example.constants.LogConstants.LogLevel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TxnLogProcessUtil {

	private static String logLevel = System.getProperty("APP_LOG_LEVEL");
	private static Logger logger = LoggerFactory.getLogger(LogConstants.LOG_DEBUG);
	private static Logger loggerfile = LoggerFactory.getLogger(LogConstants.LOG_DEBUG_FILE);
	private static Gson gson = new GsonBuilder().disableHtmlEscaping().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'").create();
	private static boolean logFileEnable = AppConstants.FLAG_E.equals(System.getProperty("LOG_FILE_ENABLE", "E"));

	public static void finish(LogBean logBean) {
		if (null == logBean) {
			return;
		}
		logTxnLogBeanMap(logBean.getTxnLogMap());
		logInfoLogBeanList(logBean.getInfoLogList());
	}
	
	public static void logInfoLogBean(String logLevel, String errorDesc, Throwable ex) {
		InfoLogBean infoLogBean = new InfoLogBean( 
				logLevel, 
				errorDesc, 
				getStackTrace(ex));
		logInfoLogBean(infoLogBean);
	}
	
	public static void logInfoLogBean(String logLevel, String msg) {
		InfoLogBean infoLogBean = new InfoLogBean(logLevel, 
				msg);
		logInfoLogBean(infoLogBean);
	}
	
	public static void logInfoLogBeanList(List<InfoLogBean> infoLogBeanList) {
		if (null != infoLogBeanList) {
			for (InfoLogBean each : infoLogBeanList) {
				logInfoLogBean(each);
			}
		}
	}
	
	public static void logInfoLogBean(InfoLogBean infoLogBean) {
		if (null == infoLogBean) {
			return;
		}
		
		if (checkLogLevel(infoLogBean.getLogLevel())) {
			logger.info(gson.toJson(infoLogBean));
			
			if (TxnLogProcessUtil.logFileEnable) {
				StringBuilder logFile = new StringBuilder(CalendarUtil.convertDateToString(infoLogBean.getLogDatetime(), CalendarUtil.ISO_8601))
						.append("|")
						.append(StringUtils.defaultString(infoLogBean.getLogLevel(), "-"))
						.append("|")
						.append("-")
						.append("|")
						.append("-");
				
				logFile.append("|").append(StringUtils.defaultString(infoLogBean.getMsg(), "-"));
				
				if (StringUtils.isNotBlank(infoLogBean.getErrorDesc()) && !"-".equals(infoLogBean.getErrorDesc())) {
					logFile.append(", ErrorDesc = ").append(infoLogBean.getErrorDesc());
				}
				if (StringUtils.isNotBlank(infoLogBean.getErrorStacktrace()) && !"-".equals(infoLogBean.getErrorStacktrace())) {
					logFile.append("\n").append(infoLogBean.getErrorStacktrace());
				}
				loggerfile.info(logFile.toString());
			}
		}
	}
	
	public static void logTxnLogBeanMap(Map<String, TxnLogBean> txnLogBeanMap) {
		if (null != txnLogBeanMap) {
			for (Map.Entry<String, TxnLogBean> each : txnLogBeanMap.entrySet()) {
				logTxnLogBean(each.getValue());
			}
		}
	}
	
	public static void logTxnLogBean(TxnLogBean txnLogBean) {
		if (null == txnLogBean) {
			return;
		}
		if (checkLogLevel(txnLogBean.getLogLevel())) {
			logger.info(gson.toJson(txnLogBean));
			
			if (TxnLogProcessUtil.logFileEnable) {
				StringBuilder logFile = new StringBuilder(CalendarUtil.convertDateToString(txnLogBean.getLogDatetime(), CalendarUtil.ISO_8601))
						.append("|")
						.append(StringUtils.defaultString(txnLogBean.getLogLevel(), "-"))
						.append("|")
						.append("request = ")
						.append(StringUtils.defaultString(txnLogBean.getRequest(), "-"))
						.append(", ")
						.append(CalendarUtil.convertDateToString(txnLogBean.getLogDatetimeResp(), CalendarUtil.ISO_8601))
						.append(" response = ")
						.append(StringUtils.defaultString(txnLogBean.getResponse(), "-"))
						.append(", totalTime = ")
						.append(txnLogBean.getTotalTime());
				
				if (StringUtils.isNotBlank(txnLogBean.getErrorDesc()) && !"-".equals(txnLogBean.getErrorDesc())) {
					logFile.append(", ErrorDesc = ").append(txnLogBean.getErrorDesc());
				}
				if (StringUtils.isNotBlank(txnLogBean.getErrorStacktrace()) && !"-".equals(txnLogBean.getErrorStacktrace())) {
					logFile.append("\n").append(txnLogBean.getErrorStacktrace());
				}
				loggerfile.info(logFile.toString());
			}
		}
	}
	
	public static void setThreadContext(String key, String value) {
		MDC.put(key, value);
	}
	
	public static String getThreadContext(String key) {
		return MDC.get(key);
	}
	
	public static void clearThreadContext() {
		MDC.clear();
	}
	
	private static boolean checkLogLevel(String logLevel) {
		if (StringUtils.isBlank(logLevel)) {
			return false;
		}
		if (LogLevel.DEBUG.equalsIgnoreCase(TxnLogProcessUtil.logLevel)) {
			return true;
		}
		if (LogLevel.INFO.equalsIgnoreCase(TxnLogProcessUtil.logLevel)) {
			if (LogLevel.DEBUG.equals(logLevel)) {
				return false;
			}
			return true;
		}
		if (LogLevel.WARN.equalsIgnoreCase(TxnLogProcessUtil.logLevel)) {
			if (LogLevel.DEBUG.equals(logLevel) || LogLevel.INFO.equals(logLevel)) {
				return false;
			}
			return true;
		}
		if (LogLevel.ERROR.equalsIgnoreCase(TxnLogProcessUtil.logLevel)) {
			if (LogLevel.ERROR.equals(logLevel) || LogLevel.FATAL.equals(logLevel)) {
				return true;
			}
			return false;
		}
		if (LogLevel.FATAL.equalsIgnoreCase(TxnLogProcessUtil.logLevel)) {
			if (LogLevel.FATAL.equals(logLevel)) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	public static String getStackTrace(Throwable e) {
		String originalMessage = null;
		String originalCause = null;
		try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw);) {
			e.printStackTrace(pw);
			originalMessage = sw.toString();
		} catch (Throwable ignoreex) {
			originalMessage = e.getMessage();
		} finally {
			if (e.getCause() != null) {
				originalCause = e.getCause().getMessage();
			}
		}
		
		StringBuilder message = new StringBuilder();
		if (StringUtils.isNotBlank(originalCause)) {
			message.append("ThrowingCause = ").append(originalCause);
		}
		
		if (StringUtils.isNotBlank(originalMessage)) {
			message.append(", ThrowingMassage = ").append(originalMessage);
		}
		
		return message.toString();
	}
}
