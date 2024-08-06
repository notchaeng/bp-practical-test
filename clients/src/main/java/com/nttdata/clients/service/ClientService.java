package com.nttdata.clients.service;

import java.util.List;

import com.nttdata.clients.domain.entity.Client;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    Client saveClient(Client client);

    Client updateClient(Long id, Client updatedClient);

    Client deleteClient(Long id);
}
