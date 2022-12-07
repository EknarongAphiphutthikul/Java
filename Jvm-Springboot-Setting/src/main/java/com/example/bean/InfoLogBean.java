package com.example.bean;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InfoLogBean extends ParentLogBean implements Serializable {

	private static final long serialVersionUID = 289301940162480310L;
	
	@SerializedName(value="msg")
	private String msg = "-";
	
	public InfoLogBean() {
		super();
	}
	
	public InfoLogBean(String logLevel, String msg) {
		super();
		super.setLogDatetime(Calendar.getInstance().getTime());
		super.setLogLevel(logLevel);
		if (StringUtils.isNotBlank(msg)) {
			this.msg = msg;
		}
	}
	
	public InfoLogBean(String logLevel, String msg, String errorDesc, String errorStacktrace) {
		super();
		super.setLogDatetime(Calendar.getInstance().getTime());
		super.setLogLevel(logLevel);
		if (StringUtils.isNotBlank(errorDesc)) {
			super.setErrorDesc(errorDesc);
		}
		if (StringUtils.isNotBlank(errorStacktrace)) {
			super.setErrorStacktrace(errorStacktrace);
		}
		if (StringUtils.isNotBlank(msg)) {
			this.msg = msg;
		}
	}
	
	public InfoLogBean(String logLevel, String errorDesc, String errorStacktrace) {
		super();
		super.setLogDatetime(Calendar.getInstance().getTime());
		super.setLogLevel(logLevel);
		if (StringUtils.isNotBlank(errorDesc)) {
			super.setErrorDesc(errorDesc);
		}
		if (StringUtils.isNotBlank(errorStacktrace)) {
			super.setErrorStacktrace(errorStacktrace);
		}
	}
	
	@Override
	public InfoLogBean clone() {
		InfoLogBean newObj = new InfoLogBean();
		newObj.setKubeNamespace(super.getKubeNamespace());
		newObj.setKubePodName(super.getKubePodName());
		return newObj;
	}

}
