package com.tparch.autoretry;

import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class ServiceConfig {
    private Map<String,Object> consumers;
    private Set<String> retryMethods;
    private String defaultConsumerKey;
    private String configId;
}
