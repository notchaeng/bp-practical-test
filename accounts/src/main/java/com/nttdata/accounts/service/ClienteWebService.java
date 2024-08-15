package com.nttdata.accounts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.nttdata.accounts.domain.dto.ClientDTO;
import com.nttdata.accounts.exception.ResourceNotFoundException;

import reactor.core.publisher.Mono;

@Service
public class ClienteWebService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${clientes.service.url}")
    private String clientesServiceUrl;

    public Mono<ClientDTO> getClienteById(String identification) {
        return webClientBuilder.build()
                .get()
                .uri(clientesServiceUrl + "/clientes/" + identification)
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    return Mono.error(new ResourceNotFoundException(e.getResponseBodyAsString()));
                })
                .onErrorResume(e -> {
                    return Mono.error(new RuntimeException(e.getMessage()));
                });
    }
}
