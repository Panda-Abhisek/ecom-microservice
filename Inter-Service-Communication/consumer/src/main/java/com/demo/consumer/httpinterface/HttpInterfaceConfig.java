package com.demo.consumer.httpinterface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {
//    @Bean
//    public ProviderHttpInterface providerHttpInterface() {
//        // 1. Create the HTTP client (RestClient in this case)
//        RestClient restClient = RestClient.create("http://localhost:8810");
//
//        // 2. Create an adapter for the client
//        RestClientAdapter adapter = RestClientAdapter.create(restClient);
//
//        // 3. Create a factory for generating HTTP interface proxies
//        HttpServiceProxyFactory factory =
//                HttpServiceProxyFactory.builderFor(adapter).build();
//
//        // 4. Create and return the proxy implementation of the interface
//        return factory.createClient(ProviderHttpInterface.class);
//    }

    @Bean
    public ProviderHttpInterface providerHttpInterface() {
        // Using WebClient instead of RestClient
        WebClient webClient =
                WebClient.builder().baseUrl("http://localhost:8810").build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ProviderHttpInterface.class);
    }
}
