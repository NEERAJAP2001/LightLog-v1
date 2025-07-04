package com.openshift.logmonitoring.config;

import io.fabric8.kubernetes.client.KubernetesClient;


import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// KubernetesClient is the interface we use to interact with the OpenShift/Kubernetes API 
@Configuration
public class KubernetesClientConfig {

    @Bean
    public KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }
}