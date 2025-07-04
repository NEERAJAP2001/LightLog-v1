package com.openshift.logmonitoring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LogScanScheduler {

    @Autowired
    private LogPollingService pollingService;

    @Scheduled(fixedRate = 60000)
    public void scan() {
        pollingService.pollLogs("default"); // Set your namespace
    }
}