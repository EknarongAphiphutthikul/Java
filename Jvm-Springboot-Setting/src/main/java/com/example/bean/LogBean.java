package com.example.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.example.constants.LogConstants.KeyLogMap;
import com.example.constants.LogConstants.LogLevel;
import com.example.utils.TxnLogProcessUtil;

public class LogBean implements Serializable {
	
	private static final long serialVersionUID = -2089267903167001459L;
	private Map<String, TxnLogBean> txnLogMap = new HashMap<>();
	private List<InfoLogBean> infoLogList = new ArrayList<>();
	
	/*
	 * Txn Log Service
	 */
	public void initLogRequestService(Date startDateTime, String request) {
		TxnLogBean txnLogBean = new TxnLogBean();
		if (null == startDateTime) {
			txnLogBean.setLogDatetime(Calendar.getInstance().getTime());
		} else {
			txnLogBean.setLogDatetime(startDateTime);
		}
		txnLogBean.setLogLevel(LogLevel.INFO);
		txnLogBean.setRequest(request);
		
		this.txnLogMap.put(KeyLogMap.SERVICE, txnLogBean);
	}
	
	public String getMsgRequestService() {
		TxnLogBean txnLogBean = this.txnLogMap.get(KeyLogMap.SERVICE);
		if (null == txnLogBean) {
			return null;
		}
		return txnLogBean.getRequest();
	}
	
	public void setLogResponseService(String response) {
		setLogResponseService(response, null, null);
	}
	
	public void setLogResponseService(String response, String errorDesc) {
		setLogResponseService(response, errorDesc, null);
	}
	
	public void setLogResponseServiceException(String response, String errorDesc, Throwable ex) {
		if (null != ex) {
			setLogResponseService(response, errorDesc, TxnLogProcessUtil.getStackTrace(ex));
		} else {
			setLogResponseService(response, errorDesc, null);
		}
	}
	
	public void setLogResponseService(String response, String errorDesc, String errorStacktrace) {
		TxnLogBean txnLogBean = this.txnLogMap.get(KeyLogMap.SERVICE);
		txnLogBean.setLogDatetimeResp(Calendar.getInstance().getTime());
		txnLogBean.setTotalTime(Math.abs(txnLogBean.getLogDatetimeResp().getTime() - txnLogBean.getLogDatetime().getTime()));
		if (StringUtils.isNotBlank(response)) {
			txnLogBean.setResponse(response);
		}
		if (StringUtils.isNotBlank(errorStacktrace)) {
			txnLogBean.setErrorStacktrace(errorStacktrace);
		}
		if (StringUtils.isNotBlank(errorDesc)) {
			txnLogBean.setErrorDesc(errorDesc);
		}
	}
	
	public void addInfoLogBean(InfoLogBean infoLogbean) {
		this.infoLogList.add(infoLogbean);
	}
	
	public void putTxnLogBeanToMap(String key, TxnLogBean txnLogbean) {
		this.txnLogMap.put(key, txnLogbean);
	}
	
	public TxnLogBean getTxnLogBeanFromMapByKey(String key) throws Exception {
		return this.txnLogMap.get(key);
	}
	
	public List<InfoLogBean> getInfoLogList() {
		return this.infoLogList;
	}
	
	public Map<String, TxnLogBean> getTxnLogMap() {
		return this.txnLogMap;
	}
}
