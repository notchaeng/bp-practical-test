package com.nttdata.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.accounts.model.Response;

import reactor.core.publisher.Mono;

@Service
public class ClienteWebService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${clientes.service.url}")
    private String clientesServiceUrl;

    public Mono<Response> getClienteById(Long id) {
        return webClientBuilder.build()
                .get()
                .uri(clientesServiceUrl + "/clientes/" + id)
                .retrieve()
                .bodyToMono(Response.class);
    }
}
