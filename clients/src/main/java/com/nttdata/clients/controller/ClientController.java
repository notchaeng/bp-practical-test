package com.nttdata.clients.controller;

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

import com.nttdata.clients.entity.Client;
import com.nttdata.clients.model.Response;
import com.nttdata.clients.service.ClientService;
import com.nttdata.clients.util.ResourseApplication;

@RestController
@RequestMapping("/clientes")
public class ClientController {

    public static Logger logger = LogManager.getLogger();

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<Response> getAllClients() {
        Response response = new Response();
        List<Client> clients;
        try {
            clients = clientService.getAllClients();
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), clients);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getAllClients method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getClientById(@PathVariable(value = "id" ) Long id) {
        Response response = new Response();
        Client client;
        try {
            client = clientService.getClientById(id);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), client);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getClientById method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Response> createClient(@RequestBody Client client) {
        Response response = new Response();
        Client clientSaved;
        try {
            clientSaved = clientService.saveClient(client);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.client.create"), clientSaved);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing createClient method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateClient(@PathVariable(value = "id") Long id, @RequestBody Client updatedClient) {
        Response response = new Response();
        Client clientUpdates;
        try {
            clientUpdates = clientService.updateClient(id, updatedClient);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.client.update"), clientUpdates);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing updateClient method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteClient(@PathVariable(value = "id") Long id) {
        Response response = new Response();
        Client clientDeleted;
        try {
            clientDeleted = clientService.deleteClient(id);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.client.delete"), clientDeleted);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing deleteClient method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
