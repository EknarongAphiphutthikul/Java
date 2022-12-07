package com.example.utils;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import com.example.constants.AppConstants;

public class CommonUtil {

	private static final java.util.regex.Pattern LTRIM = java.util.regex.Pattern.compile("^\\s+");
	private static final java.util.regex.Pattern RTRIM = java.util.regex.Pattern.compile("\\s+$");
	
	private CommonUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static String substring(String value, Integer sn, Integer ln) {
		if (value == null) {
			return null;
		}
		if (ln < 0) {
			ln = value.length() + ln;
		}
		if (sn < 0) {
			sn = value.length() + sn;
		}
		if (ln > value.length()) {
			ln = value.length();
		}
		if (sn > ln) {
			return AppConstants.SPACE;
		}
		if (sn < 0) {
			sn = 0;
		}
		if (ln < 0) {
			ln = 0;
		}
		return value.substring(sn, ln);
	}

	public static boolean checkStringIsNull(String token) {
		return StringUtils.isBlank(token);
	}

	public static String ltrim(String s) {
		return LTRIM.matcher(s).replaceAll("");
	}

	public static String rtrim(String s) {
		return RTRIM.matcher(s).replaceAll("");
	}

	public static String padding(String inputStr, int maxLength) {
		String outputStr = "";
		if (inputStr == null) {
			return outputStr;
		}
		outputStr = paddingZero(maxLength - inputStr.length()) + inputStr;
		return outputStr;
	}

	public static String paddingZero(int lenPadding) {
		StringBuilder outputZero = new StringBuilder("");
		for (int i = 0; i < lenPadding; i++) {
			outputZero.append("0");
		}
		return outputZero.toString();
	}

	public static String getLastNCharacters(String inputString, int subStringLength) {
		int length = inputString.length();
		if (length <= subStringLength) {
			return inputString;
		}
		int startIndex = length - subStringLength;
		return inputString.substring(startIndex);
	}

	public static String substringLast(String value, Integer n) {
		return substringStartWith(value, value.length() - n);
	}

	public static String substringStartWith(String value, Integer n) {
		if (value == null) {
			return null;
		}
		if (n < 0) {
			n = value.length() + n;
		}
		if (n < 0) {
			n = 0;
		}
		if (n > value.length()) {
			return AppConstants.SPACE;
		}
		return value.substring(n);
	}

	public static String replaceParamInMessage(String message, Object[] params) {
		String matchExp = null;
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				matchExp = "{" + i + "}";
				if (params[i] != null) {
					message = StringUtils.replace(message, matchExp, params[i].toString());
				}
			}
		}
		return message;
	}
	
	public static void throwNewException(String errorDesc) throws Exception {
		throw new Exception(errorDesc);
	}

	public static String upperCaseString(String token){

		if(checkStringIsNull(token)){
			return "";
		}

		String value = token.toUpperCase();
		return value;
	}
	
	public static BigDecimal checkBigDecimalIsNull(BigDecimal token) {
		if (null == token)
			return BigDecimal.ZERO;

		return token;
	}


}
