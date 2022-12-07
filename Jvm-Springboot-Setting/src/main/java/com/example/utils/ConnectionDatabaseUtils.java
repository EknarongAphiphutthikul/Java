package com.example.utils;

import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.lang3.StringUtils;

import com.example.constants.LogConstants.LogLevel;

public class ConnectionDatabaseUtils {
	
	private ConnectionDatabaseUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static Connection getConnectionDb2(String classDriver, String url, String username, String password) throws Exception {
		if(checkConfigDatabaseInvalid(classDriver, url, username, password)) {
			throw new Exception("Config database invalid.");
		}
		Class.forName(classDriver);
		return DriverManager.getConnection(url, username, password);
	}
	
	private static boolean checkConfigDatabaseInvalid(String classDriver, String url, String username, String password) {
		boolean result = false;
		if (StringUtils.isBlank(classDriver)) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "classDriver database is blank.");
			result = true;
		}
		if (StringUtils.isBlank(url)) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "classDriver url is blank.");
			result = true;
		}
		if (StringUtils.isBlank(username)) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "classDriver username is blank.");
			result = true;
		}
		if (StringUtils.isBlank(password)) {
			TxnLogProcessUtil.logInfoLogBean(LogLevel.ERROR, "classDriver password is blank.");
			result = true;
		}
		return result;
	}
}
