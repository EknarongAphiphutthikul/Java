package com.example.bean;

import java.io.Serializable;
import java.util.Date;

import com.example.constants.LogConstants.SystemPropertyKeyWord;
import com.example.utils.CalendarUtil;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class MetricBean implements Serializable {
	
	private static final long serialVersionUID = -2089267903167001459L;
	@SerializedName(value="@timestamp")
	private Date logDatetime;
	@SerializedName(value="log_level")
	private String logLevel = "-";
	@SerializedName(value="kube_namespace")
	private String kubeNamespace = "-";
	@SerializedName(value="kube_pod_name")
	private String kubePodName = "-";
	@SerializedName(value="app_metric_name")
	private String metricName = "-";
	@SerializedName(value="app_metric_value")
	private Long metricValue = 0L;
	
	public MetricBean(String logLevel, String metricName, Long metricValue) {
		this.logDatetime = CalendarUtil.getCurrentCalendarTimeZoneBangkok().getTime();
		this.metricName = metricName;
		this.metricValue = metricValue;
		this.logLevel = logLevel;
		this.kubePodName = System.getProperty(SystemPropertyKeyWord.KUBERNETES_PODNAME);
		this.kubeNamespace = System.getProperty(SystemPropertyKeyWord.KUBERNETES_NAMESPACE);
	}
}
