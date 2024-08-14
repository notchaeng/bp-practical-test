package com.nttdata.clients.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.nttdata.clients.domain.dto.ClientDTO;
import com.nttdata.clients.domain.entity.Client;
import com.nttdata.clients.exception.ResourceNotFoundException;
import com.nttdata.clients.repository.ClientRepository;
import com.nttdata.clients.service.impl.ClientServiceImpl;
import com.nttdata.clients.service.mapper.ClientMapper;
import com.nttdata.clients.util.Constants;
import com.nttdata.clients.util.ResourseApplication;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllClients() {
        Client client = new Client();
        client.setId(1L);
        client.setNombre("Test Client");
        client.setEstado(Constants.ACTIVE_STATUS);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setNombre("Test Client");
        clientDTO.setEstado(Constants.ACTIVE_STATUS);

        when(clientRepository.findByEstado(Constants.ACTIVE_STATUS)).thenReturn(List.of(client));
        when(clientMapper.toClientDTO(client)).thenReturn(clientDTO);

        List<ClientDTO> clients = clientService.getAllClients();

        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals("Test Client", clients.get(0).getNombre());
    }

    @Test
    public void testGetAllClientsNotFound() {
        when(clientRepository.findByEstado(Constants.ACTIVE_STATUS)).thenReturn(List.of());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getAllClients();
        });

        assertEquals(ResourseApplication.properties.getProperty("clients.not.found"), exception.getMessage());
    }

    @Test
    public void testGetClientByIdentification() {
        Client client = new Client();
        client.setIdentificacion("123456");
        client.setNombre("Test Client");
        client.setEstado(Constants.ACTIVE_STATUS);

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setIdentificacion("123456");
        clientDTO.setNombre("Test Client");
        clientDTO.setEstado(Constants.ACTIVE_STATUS);

        when(clientRepository.findByIdentificacionAndEstado("123456", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(client));
        when(clientMapper.toClientDTO(client)).thenReturn(clientDTO);

        ClientDTO foundClientDTO = clientService.getClientByIdentification("123456");

        assertNotNull(foundClientDTO);
        assertEquals("Test Client", foundClientDTO.getNombre());
    }

    @Test
    public void testGetClientByIdentificationNotFound() {
        when(clientRepository.findByIdentificacionAndEstado("123456", Constants.ACTIVE_STATUS)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientByIdentification("123456");
        });

        assertEquals(ResourseApplication.properties.getProperty("client.not.found"), exception.getMessage());
    }

    @Test
    public void testSaveClient() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setIdentificacion("123456");
        clientDTO.setNombre("New Client");

        Client client = new Client();
        client.setIdentificacion("123456");
        client.setNombre("New Client");

        when(clientRepository.findByIdentificacionAndEstado("123456", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(client));
        when(clientMapper.toClient(clientDTO)).thenReturn(client);
        when(clientMapper.toClientDTO(client)).thenReturn(clientDTO);
        when(clientRepository.save(client)).thenReturn(client);

        ClientDTO savedClientDTO = clientService.saveClient(clientDTO);

        assertNotNull(savedClientDTO);
        assertEquals("New Client", savedClientDTO.getNombre());
    }

    @Test
    public void testUpdateClient() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setIdentificacion("123456");
        clientDTO.setNombre("Updated Client");

        Client client = new Client();
        client.setIdentificacion("123456");
        client.setNombre("Old Client");

        Client updatedClient = new Client();
        updatedClient.setIdentificacion("123456");
        updatedClient.setNombre("Updated Client");

        when(clientRepository.findByIdentificacionAndEstado("123456", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(client));
        when(clientMapper.toClient(clientDTO)).thenReturn(updatedClient);
        when(clientMapper.toClientDTO(updatedClient)).thenReturn(clientDTO);
        when(clientRepository.save(updatedClient)).thenReturn(updatedClient);

        ClientDTO updatedClientDTO = clientService.updateClient("123456", clientDTO);

        assertNotNull(updatedClientDTO);
        assertEquals("Updated Client", updatedClientDTO.getNombre());
    }

    @Test
    public void testDeleteClient() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setIdentificacion("123456");
        clientDTO.setEstado(Constants.DELETED_STATUS);

        Client client = new Client();
        client.setIdentificacion("123456");
        client.setEstado(Constants.ACTIVE_STATUS);

        Client deletedClient = new Client();
        deletedClient.setIdentificacion("123456");
        deletedClient.setEstado(Constants.DELETED_STATUS);

        when(clientRepository.findByIdentificacionAndEstado("123456", Constants.ACTIVE_STATUS)).thenReturn(Optional.of(client));
        when(clientMapper.toClient(clientDTO)).thenReturn(deletedClient);
        when(clientMapper.toClientDTO(deletedClient)).thenReturn(clientDTO);
        when(clientRepository.save(deletedClient)).thenReturn(deletedClient);

        ClientDTO deletedClientDTO = clientService.deleteClient("123456");

        assertNotNull(deletedClientDTO);
        assertEquals(Constants.DELETED_STATUS, deletedClientDTO.getEstado());
    }
}
