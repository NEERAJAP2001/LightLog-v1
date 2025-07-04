package com.openshift.logmonitoring.controller;

import com.openshift.logmonitoring.model.LogStatsResponse;

import com.openshift.logmonitoring.service.LogPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogStatsController {

    @Autowired
    private LogPollingService logPollingService;

    @GetMapping("/stats")
    public List<LogStatsResponse> getStats() {
        return logPollingService.getLatestStats();
    }
}