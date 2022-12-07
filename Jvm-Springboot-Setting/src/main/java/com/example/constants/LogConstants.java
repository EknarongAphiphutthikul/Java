package com.example.constants;

public class LogConstants {

	public static final String LOG_DEBUG = "LogDebug";
	public static final String LOG_DEBUG_FILE = "LogDebug-File";
	
	public interface SystemPropertyKeyWord {
		public static final String KUBERNETES_PODNAME = "KUBERNETES_PODNAME";
		public static final String KUBERNETES_NAMESPACE = "KUBERNETES_NAMESPACE";
	}
	
	public interface KeyLogMap {
		public static final String SERVICE = "SERVICE";
		public static final String SUBSERVICE = "SUBSERVICE";
	}
	
	public interface LogLevel {
		public static final String DEBUG = "DEBUG";
		public static final String INFO = "INFO";
		public static final String WARN = "WARN";
		public static final String ERROR = "ERROR";
		public static final String FATAL = "FATAL";
	}
}
