package com.demo.consumer.httpinterface;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "http://localhost:8810")
public interface ProviderHttpInterface {
    @GetExchange("/instance-info")
    String getInstanceInfo();
}
