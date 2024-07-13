package com.nttdata.clients.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.clients.entity.Client;
import com.nttdata.clients.exception.ResourceNotFoundException;
import com.nttdata.clients.repository.ClientRepository;
import com.nttdata.clients.service.ClientService;
import com.nttdata.clients.util.Constants;
import com.nttdata.clients.util.ResourseApplication;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = clientRepository.findByEstado(Constants.ACTIVE_STATUS);
        if (clients.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("clients.not.found"));
        }
        return clients;
    }

    @Override
    public Client getClientById(Long id) {
        Client client = clientRepository.findByIdAndEstado(id, Constants.ACTIVE_STATUS);
        if (null == client) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("client.not.found"));
        }
        return client;
    }

    @Override
    public Client saveClient(Client client) {
        Client validateClient = clientRepository.findByNombreAndEstado(removeSpaces(client.getNombre()), Constants.ACTIVE_STATUS);
        if (null != validateClient) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("client.found"));
        }
        return clientRepository.save(client);
    }

    @Override
    public Client updateClient(Long id, Client updatedClient) {
        Client client = getClientById(id);
        client.setNombre(removeSpaces(updatedClient.getNombre()));
        client.setDireccion(updatedClient.getDireccion());
        client.setTelefono(updatedClient.getTelefono());
        client.setPassword(updatedClient.getPassword());
        client.setEstado(updatedClient.getEstado());
        return clientRepository.save(client);
    }

    @Override
    public Client deleteClient(Long id) {
        Client client = getClientById(id);
        client.setEstado(Constants.DELETED_STATUS);
        return clientRepository.save(client);
    }

    private String removeSpaces(String string) {
        return string.trim().replaceAll("\\s+", " ");
    }
}
