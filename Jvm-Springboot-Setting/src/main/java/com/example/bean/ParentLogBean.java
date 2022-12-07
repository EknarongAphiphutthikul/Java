package com.example.bean;

import java.io.Serializable;
import java.util.Date;

import com.example.constants.LogConstants.SystemPropertyKeyWord;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class ParentLogBean implements Serializable {
	
	private static final long serialVersionUID = -2089267903167001459L;
	@SerializedName(value="@timestamp")
	private Date logDatetime;
	@SerializedName(value="log_level")
	private String logLevel = "-";
	@SerializedName(value="error_desc")
	private String errorDesc = "-";
	@SerializedName(value="error_stacktrace")
	private String errorStacktrace = "-";
	@SerializedName(value="kube_namespace")
	private String kubeNamespace = "-";
	@SerializedName(value="kube_pod_name")
	private String kubePodName = "-";
	
	public ParentLogBean() {
		this.kubePodName = System.getProperty(SystemPropertyKeyWord.KUBERNETES_PODNAME);
		this.kubeNamespace = System.getProperty(SystemPropertyKeyWord.KUBERNETES_NAMESPACE);
	}
}
