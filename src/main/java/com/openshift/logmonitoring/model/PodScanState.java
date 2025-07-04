package com.openshift.logmonitoring.model;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PodScanState {
    private Instant lastScannedAt = Instant.now().minusSeconds(60);
    private Map<String, Long> keywordCounts = new ConcurrentHashMap<>();

    public Instant getLastScannedAt() { return lastScannedAt; }
    public void setLastScannedAt(Instant lastScannedAt) { this.lastScannedAt = lastScannedAt; }

    public Map<String, Long> getKeywordCounts() { return keywordCounts; }
}