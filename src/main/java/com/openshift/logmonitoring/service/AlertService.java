package com.openshift.logmonitoring.service;

import com.openshift.logmonitoring.config.AlertProperties;
import com.openshift.logmonitoring.model.AlertRule;
import com.openshift.logmonitoring.util.EmailAlertSender;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {

    @Autowired
    private LogPollingService logPollingService;

    @Autowired
    private AlertProperties alertProperties;

    @Autowired
    private EmailAlertSender emailSender;

    private final Map<String, Instant> lastAlertSent = new HashMap<>();

    @Scheduled(fixedRate = 60000) // every 1 min
    public void checkAlerts() {
        List<AlertRule> rules = alertProperties.getRules();
        Map<String, Map<String, Long>> stats = logPollingService.getKeywordStatsSnapshot();

        for (AlertRule rule : rules) {
            String key = rule.getService() + ":" + rule.getKeyword();
            long count = stats
                .getOrDefault(rule.getService(), new HashMap<>())
                .getOrDefault(rule.getKeyword(), 0L);

            if (count >= rule.getThreshold()) {
                Instant lastSent = lastAlertSent.getOrDefault(key, Instant.EPOCH);
                if (Instant.now().minusSeconds(600).isAfter(lastSent)) { // 10 min cooldown
                    sendAlert(rule, count);
                    lastAlertSent.put(key, Instant.now());
                }
            }
        }
    }

    private void sendAlert(AlertRule rule, long count) {
        String subject = "[ALERT] Keyword '" + rule.getKeyword() + "' triggered!";
        String body = "Service: " + rule.getService() +
                "\nKeyword: " + rule.getKeyword() +
                "\nCount: " + count +
                "\nThreshold: " + rule.getThreshold() +
                "\nTime: " + Instant.now();
        emailSender.send(alertProperties.getEmailTo(), subject, body);
    }
}
