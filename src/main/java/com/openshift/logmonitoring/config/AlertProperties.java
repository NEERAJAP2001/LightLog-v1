package com.openshift.logmonitoring.config;

import com.openshift.logmonitoring.model.AlertRule;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "alert")
public class AlertProperties {
    private List<AlertRule> rules;
    private String emailTo;

    public List<AlertRule> getRules() {
        return rules;
    }

    public void setRules(List<AlertRule> rules) {
        this.rules = rules;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
}

