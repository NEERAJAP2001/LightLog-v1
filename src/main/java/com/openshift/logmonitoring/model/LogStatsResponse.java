package com.openshift.logmonitoring.model;

import java.util.Map;

public class LogStatsResponse {
    private String serviceName;
    private Map<String, Long> keywordCounts;

    public LogStatsResponse(String serviceName, Map<String, Long> keywordCounts) {
        this.serviceName = serviceName;
        this.keywordCounts = keywordCounts;
    }

    public String getServiceName() { return serviceName; }
    public Map<String, Long> getKeywordCounts() { return keywordCounts; }
}