package com.openshift.logmonitoring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


public class AlertRule {
    private String service;
    private String keyword;
    private long threshold;
    
    
    
	public AlertRule(String service, String keyword, long threshold) {
		super();
		this.service = service;
		this.keyword = keyword;
		this.threshold = threshold;
	}
	public AlertRule() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public long getThreshold() {
		return threshold;
	}
	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

    // Getters and Setters
    
}
