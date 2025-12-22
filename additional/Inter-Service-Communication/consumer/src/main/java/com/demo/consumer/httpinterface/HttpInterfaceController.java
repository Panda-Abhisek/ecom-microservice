package com.demo.consumer.httpinterface;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/http-interface")
@RequiredArgsConstructor
public class HttpInterfaceController {
    // Inject the auto-implemented HTTP interface proxy
    private final ProviderHttpInterface providerHttpInterface;

    @GetMapping("/instance")
    public String getInstance() {
        // The HTTP call happens behind the scenes here!
        return providerHttpInterface.getInstanceInfo();
    }
}


