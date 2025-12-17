package com.demo.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "provider-service", url = "http://localhost:8810")
public interface ProviderFeignClientService {
    @GetMapping("/instance-info")
    String getInstanceInfo();
}
