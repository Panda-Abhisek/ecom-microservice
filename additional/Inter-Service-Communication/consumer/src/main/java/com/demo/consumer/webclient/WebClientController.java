package com.demo.consumer.webclient;

import com.demo.consumer.restclient.ProviderRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/web-client")
@RequiredArgsConstructor
public class WebClientController {

    private final ProviderWebClient providerWebClient;

    @GetMapping("/instance")
    public Mono<String> getInstance() {
        return providerWebClient.getInstanceInfo();
    }
//    @GetMapping("/instance")
//    public Mono<String> getInstance() {
//        WebClient webClient = WebClient.create();
//        Mono<String> response = webClient.get()
//                .uri("http://localhost:8810/instance-info")
//                .retrieve().bodyToMono(String.class);
//        return response;
//    }
}
