package com.nttdata.accounts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.accounts.entity.Account;
import com.nttdata.accounts.model.Response;
import com.nttdata.accounts.service.AccountService;
import com.nttdata.accounts.service.ClienteWebService;
import com.nttdata.accounts.util.ResourseApplication;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cuentas")
public class AccountController {

    public static Logger logger = LogManager.getLogger();

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClienteWebService clienteService;

    @GetMapping
    public ResponseEntity<Response> getAllAccounts() {
        Response response = new Response();
        List<Account> accounts;
        try {
            accounts = accountService.getAllAccounts();
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), accounts);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getAllAccounts method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getAccountById(@PathVariable(value = "id") Long id) {
        Response response = new Response();
        Account account;
        try {
            account = accountService.getAccountById(id);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), account);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getAccountById method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public Mono<ResponseEntity<Response>> createAccount(@RequestBody Account account) {
        Response response = new Response();

        return clienteService.getClienteById(account.getClienteId())
                .flatMap(client -> {
                    Account accountSaved = accountService.saveAccount(account);
                    response.setResponse(response, ResourseApplication.properties.getProperty("success.account.create"), accountSaved);
                    return Mono.just(new ResponseEntity<>(response, HttpStatus.OK));
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    logger.error("Error while executing createAccount method.", e);
                    String errorMessage = extractErrorMessage(e.getResponseBodyAsString());
                    response.setResponse(response, errorMessage, null);
                    return Mono.just(new ResponseEntity<>(response, HttpStatus.valueOf(e.getStatusCode().value())));
                })
                .onErrorResume(e -> {
                    logger.error("Error while executing createAccount method.", e);
                    response.setResponse(response, e.getMessage(), null);
                    return Mono.just(new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateAccount(@PathVariable(value = "id") Long id, @RequestBody Account updatedAccount) {
        Response response = new Response();
        Account accountUpdate;
        try {
            accountUpdate = accountService.updateAccount(id, updatedAccount);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.account.update"), accountUpdate);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing updateAccount method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteAccount(@PathVariable(value = "id") Long id) {
        Response response = new Response();
        Account accountDeleted;
        try {
            accountDeleted = accountService.deleteAccount(id);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.account.delete"), accountDeleted);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing deleteAccount method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractErrorMessage(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            return root.path("message").asText();
        } catch (JsonProcessingException e) {
            logger.error("Error parsing error response body", e);
            return "An unexpected error occurred";
        }
    }
}
