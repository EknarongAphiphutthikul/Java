package com.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarUtil {
	
	public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
	public static final TimeZone TIME_ZONE_ASIA_BANGKOK = TimeZone.getTimeZone("Asia/Bangkok");
	public static final Locale LOCALE_US = Locale.US;
	public static final Locale LOCALE_TH = new Locale("th", "TH");

	public static final String DATE_FORMATE_YYYYMMDD_HHMMSSSSS = "yyyyMMdd HH:mm:ss:SSS";
	public static final String DATE_FORMATE_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String DATE_FORMATE_YYYYMMDD = "yyyyMMdd";
	public static final String DATE_FORMATE_YYMMDD = "yyMMdd";
	public static final String DATE_FORMATE_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_FORMATE_YYYY_MM_DD_HHMMSSSSS = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String DATE_FORMATE_YYYY_MM_DD_HHMMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_FORMATE_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_DDMMYYYY_HHmmssSS = "dd/MM/yyyy HH:mm:ss:SS";
	public static final String DATE_FORMAT_DD_MM_YYYY_HHmmss = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_FORMAT_MMDD = "MMdd";
	public static final String TIME_FORMAT_HHMMSS = "HHmmss";
	public static final String TIME_FORMAT_MMSS = "mmss";
	public static final String TIME_FORMAT_HHMMSSSSSS = "HHmmsssss";
	public static final String DATE_FORMAT_DDMMYYYY_HHmmss = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_FORMATE_DD_MM_YY_HHMM = "dd/MM/yy HH:mm";
	public static final String TIME_FORMAT_HHMM = "HH:mm";
	public static final String TIME_FORMAT_HHMMS = "HHmms";
	public static final String BS_DATE_TIME_FORMAT_YYYY_MM_DDTHHmmssSSS = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final String DATE_FORMAT_DDMMYYYY = "dd/MM/yyyy";
	public static final String DATE_FORMAT_DD = "dd";
	public static final String DATE_FORMAT_MM = "mm";
	public static final String DATE_FORMAT_HH_MM_SS = "HH:mm:ss";
	public static final String DATE_FORMAT_MM_DD_HH_MM_SS = "MMddHHmmss";
	public static final String DATE_FORMAT_YY_DDD_HH = "yyDDDHH";
	public static final String ISO_8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'+07:00'";
	
	public static Calendar getCurrentCalendarTimeZoneBangkok() {
		return Calendar.getInstance(TIME_ZONE_ASIA_BANGKOK);
	}
	
	public static Calendar getCurrentCalendarTimeZoneGMT() {
		return Calendar.getInstance(TIME_ZONE_GMT);
	}
	
	private static String getDefaultFormat(String format) {
		if (CommonUtil.checkStringIsNull(format))
			format = DATE_FORMATE_YYYYMMDD_HHMMSSSSS;
		return format;
	}
	
	public static Date convertStrToDate(String strDate, String format) throws ParseException {

		if (CommonUtil.checkStringIsNull(strDate)) {
			return null;
		}

		format = getDefaultFormat(format);
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		sdf.setTimeZone(TIME_ZONE_ASIA_BANGKOK);
		try {
			Date date = sdf.parse(strDate);
			return date;
		} catch (ParseException e) {
			throw e;
		}
	}
	
	public static String convertDateToString(Date date, String format) {
		if (date == null)
			return null;

		format = getDefaultFormat(format);

		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		sdf.setTimeZone(TIME_ZONE_ASIA_BANGKOK);
		return sdf.format(date);
	}

	public static Calendar convertStrToCalendar(String strDate, String format) throws ParseException {
		Calendar cal = getCurrentCalendarTimeZoneBangkok();
		Date date = convertStrToDate(strDate, format);
		cal.setTime(date);
		return cal;
	}

	public static String convertCalToString(Calendar cal, String format) {
		return convertDateToString(cal.getTime(), format);
	}
	
	public static String getCurrentDateTimeFormat(String format) {
		return convertCalToString(getCurrentCalendarTimeZoneBangkok(), format);
	}
	
	public static String getCurrentDateTimeFormatTimeZone(String format, TimeZone timezone) {
		Calendar cal = Calendar.getInstance(timezone);

		format = getDefaultFormat(format);

		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
		sdf.setTimeZone(timezone);
		return sdf.format(cal.getTime());
	}

	public static Calendar getCurrentCalWithOutTime() {
		Calendar cal = getCurrentCalendarTimeZoneBangkok();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		return cal;
	}
}
