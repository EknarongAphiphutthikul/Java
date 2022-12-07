package com.example.bean;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.example.utils.CalendarUtil;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TxnLogBean extends ParentLogBean implements Serializable {

	private static final long serialVersionUID = 4471298566535702445L;
	@SerializedName(value="log_datetime_resp")
	private Date logDatetimeResp;
	@SerializedName(value="total_time")
	private Long totalTime = 0L;
	@SerializedName(value="request")
	private String request = "-";
	@SerializedName(value="response")
	private String response = "-";
	
	public TxnLogBean() {
		super();
	}
	
	public TxnLogBean(Date reqDtm, String logLevel, String request) {
		super();
		super.setLogDatetime(reqDtm);
		super.setLogLevel(logLevel);
        this.setLogDatetimeResp(CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime());
        this.setTotalTime(this.getLogDatetimeResp().getTime() - reqDtm.getTime());
        if (StringUtils.isNotBlank(request)) {
        	this.setRequest(request);
        }
	}
	
	public TxnLogBean(Date reqDtm, String logLevel, String request, String response, String errorDesc) {
		super();
		super.setLogDatetime(reqDtm);
		super.setLogLevel(logLevel);
        this.setLogDatetimeResp(CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime());
        this.setTotalTime(this.getLogDatetimeResp().getTime() - reqDtm.getTime());
        if (StringUtils.isNotBlank(request)) {
        	this.setRequest(request);
        }
        if (StringUtils.isNotBlank(response)) {
        	this.setResponse(response);
        }
        if (StringUtils.isNotBlank(errorDesc)) {
        	super.setErrorDesc(errorDesc);
        }
	}
	
	public TxnLogBean(Date reqDtm, String logLevel,  String request, String response, String errorDesc, String errorStacktrace) {
		super();
		super.setLogDatetime(reqDtm);
		super.setLogLevel(logLevel);
        this.setLogDatetimeResp(CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime());
        this.setTotalTime(this.getLogDatetimeResp().getTime() - reqDtm.getTime());
        if (StringUtils.isNotBlank(request)) {
        	this.setRequest(request);
        }
        if (StringUtils.isNotBlank(response)) {
        	this.setResponse(response);
        }
        if (StringUtils.isNotBlank(errorDesc)) {
        	super.setErrorDesc(errorDesc);
        }
	    if (StringUtils.isNotBlank(errorStacktrace)) {
	        super.setErrorStacktrace(errorStacktrace);
	    }
	}
	
	@Override
	public TxnLogBean clone() {
		TxnLogBean newObj = new TxnLogBean();
		newObj.setKubeNamespace(super.getKubeNamespace());
		newObj.setKubePodName(super.getKubePodName());
		return newObj;
	}
}
