package com.nttdata.clients.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nttdata.clients.domain.dto.ClientDTO;
import com.nttdata.clients.exception.ResourceFoundException;
import com.nttdata.clients.exception.ResourceNotFoundException;
import com.nttdata.clients.repository.ClientRepository;
import com.nttdata.clients.service.ClientService;
import com.nttdata.clients.service.mapper.ClientMapper;
import com.nttdata.clients.util.Constants;
import com.nttdata.clients.util.ResourseApplication;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientDTO> getAllClients() {
        List<ClientDTO> clients = clientRepository.findByEstado(Constants.ACTIVE_STATUS).stream().map(clientMapper::toClientDTO).toList();
        if (clients.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("clients.not.found"));
        }
        return clients;
    }

    @Override
    public ClientDTO getClientByIdentification(String identification) {
        return clientRepository.findByIdentificacionAndEstado(identification, Constants.ACTIVE_STATUS).map(clientMapper::toClientDTO).orElseThrow(()
                -> new ResourceNotFoundException(ResourseApplication.properties.getProperty("client.not.found")));
    }

    @Override
    public ClientDTO saveClient(ClientDTO client) {
        clientRepository.findByIdentificacionAndEstado(client.getIdentificacion(), Constants.ACTIVE_STATUS).ifPresent(res -> {
            throw new ResourceFoundException(ResourseApplication.properties.getProperty("client.found"));
        });
        return clientMapper.toClientDTO(clientRepository.save(clientMapper.toClient(client)));
    }

    @Override
    public ClientDTO updateClient(String identification, ClientDTO clientToUpdate) {
        ClientDTO client = getClientByIdentification(identification);
        client.setNombre(clientToUpdate.getNombre());
        client.setDireccion(clientToUpdate.getDireccion());
        client.setTelefono(clientToUpdate.getTelefono());
        client.setPassword(clientToUpdate.getPassword());
        return clientMapper.toClientDTO(clientRepository.save(clientMapper.toClient(client)));
    }

    @Override
    public ClientDTO deleteClient(String identification) {
        ClientDTO client = getClientByIdentification(identification);
        client.setEstado(Constants.DELETED_STATUS);
        return clientMapper.toClientDTO(clientRepository.save(clientMapper.toClient(client)));
    }
}
