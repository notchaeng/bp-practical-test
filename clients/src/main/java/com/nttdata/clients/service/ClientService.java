package com.nttdata.clients.service;

import java.util.List;

import com.nttdata.clients.domain.dto.ClientDTO;

public interface ClientService {

    List<ClientDTO> getAllClients();

    ClientDTO getClientByIdentification(String identification);

    ClientDTO saveClient(ClientDTO client);

    ClientDTO updateClient(String identification, ClientDTO clientToUpdate
    );

    ClientDTO deleteClient(String identification);
}
