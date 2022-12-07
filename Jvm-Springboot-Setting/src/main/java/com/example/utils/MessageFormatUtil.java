package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageFormatUtil {

	private MessageFormatUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static Gson gsonWithDateFormat = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
	
	public static String getJsonFromObjectWithDateFormat(Object object) {
		return gsonWithDateFormat.toJson(object);
	}
	
	public static <T> T getStringJsonToObjectWithDateFormat(String jsonString,Class<T> classOfResponse) {
		return gsonWithDateFormat.fromJson(jsonString,classOfResponse);
	
	}
}
