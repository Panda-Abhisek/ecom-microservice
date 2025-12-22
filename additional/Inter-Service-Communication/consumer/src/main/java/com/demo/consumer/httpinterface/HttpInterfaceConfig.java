package com.demo.consumer.httpinterface;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {
//    @Bean
//    @LoadBalanced
//    @Primary
//    public RestClient.Builder httpRestClient() {
//        return RestClient.builder();
//    }
//    @Bean
//    public ProviderHttpInterface providerHttpInterfaceRestClient(RestClient.Builder restClientBuilder) {
//        // 1. Create the HTTP client (RestClient in this case)
////      RestClient restClient = RestClient.create("http://localhost:8810");
//        RestClient restClient = restClientBuilder.build();
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
    @LoadBalanced
    @Primary
    public WebClient.Builder loadBalancedRestClientWeb() {
        return WebClient.builder();
    }

    @Bean
    public ProviderHttpInterface providerHttpInterfaceWebClient(WebClient.Builder webClientbuilder) {
        // Using WebClient instead of RestClient
//        WebClient webClient =
//                WebClient.builder().baseUrl("http://localhost:8810").build();
        WebClient webClient =
                webClientbuilder.build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ProviderHttpInterface.class);
    }
}
