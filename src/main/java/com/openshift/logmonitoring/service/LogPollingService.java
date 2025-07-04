package com.openshift.logmonitoring.service;


import jakarta.annotation.PostConstruct; // or javax.annotation.PostConstruct in older setups


import com.openshift.logmonitoring.model.LogStatsResponse;
import com.openshift.logmonitoring.model.PodScanState;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LogPollingService {

    @Autowired
    private KubernetesClient client;

    @Value("#{'${logmonitor.services}'.split(',')}")
    private List<String> services;

    @Value("#{'${logmonitor.keywords}'.split(',')}")
    private List<String> keywords;
    
    @Value("${monitoring.local-mode:false}")
    private boolean localMode;
    
    @PostConstruct
    public void init() {
        pollLogs("default"); // Replace "default" with your actual namespace if needed
    }

    private Map<String, PodScanState> scanStateMap = new ConcurrentHashMap<>();

    public void pollLogs(String namespace) {
        for (String service : services) {
            List<Pod> pods = client.pods().inNamespace(namespace).withLabel("app", service).list().getItems();
            for (Pod pod : pods) {
                try {
                    String podName = pod.getMetadata().getName();
                    String key = namespace + ":" + podName;
                    PodScanState state = scanStateMap.getOrDefault(key, new PodScanState());

                    String log = client.pods()
                        .inNamespace(namespace)
                        .withName(podName)
                        .sinceTime(state.getLastScannedAt().toString())
                        .getLog();

                    Map<String, Long> counts = new HashMap<>();
                    for (String keyword : keywords) counts.put(keyword, 0L);

                    for (String line : log.split("\n")) {
                        for (String keyword : keywords) {
                            if (line.toLowerCase().contains(keyword.toLowerCase())) {
                                counts.put(keyword, counts.get(keyword) + 1);
                            }
                        }
                    }

                    state.setLastScannedAt(Instant.now());
                    counts.forEach((k, v) -> state.getKeywordCounts().merge(k, v, Long::sum));
                    scanStateMap.put(key, state);
                } catch (Exception e) {
                    // Could be CrashLoopBackOff or terminated pod, skip
                }
            }
        }
    }

    public List<LogStatsResponse> getLatestStats() {
        Map<String, Map<String, Long>> serviceCounts = new HashMap<>();
        
        scanStateMap.forEach((key, state) -> {
            String service = key.split(":")[1].split("-")[0]; // Extract service from pod name
            serviceCounts.putIfAbsent(service, new HashMap<>());
            for (Map.Entry<String, Long> entry : state.getKeywordCounts().entrySet()) {
                serviceCounts.get(service).merge(entry.getKey(), entry.getValue(), Long::sum);
            }
        });

        List<LogStatsResponse> responses = new ArrayList<>();
        serviceCounts.forEach((service, map) -> responses.add(new LogStatsResponse(service, map)));
        return responses;
    }
    
    public Map<String, Map<String, Long>> getKeywordStatsSnapshot() {
        Map<String, Map<String, Long>> result = new HashMap<>();
        scanStateMap.forEach((key, state) -> {
            String service = key.split(":")[1].split("-")[0];
            result.putIfAbsent(service, new HashMap<>());
            state.getKeywordCounts().forEach((k, v) -> 
                result.get(service).merge(k, v, Long::sum));
        });
        return result;
    }

}