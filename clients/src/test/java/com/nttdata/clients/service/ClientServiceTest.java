// ClientServiceTest.java
package com.nttdata.clients.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.nttdata.clients.domain.entity.Client;
import com.nttdata.clients.exception.ResourceNotFoundException;
import com.nttdata.clients.repository.ClientRepository;
import com.nttdata.clients.service.impl.ClientServiceImpl;
import com.nttdata.clients.util.Constants;
import com.nttdata.clients.util.ResourseApplication;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClientById() {
        Client client = new Client();
        client.setId(1L);
        client.setNombre("Test Client");
        client.setEstado(Constants.ACTIVE_STATUS);

        when(clientRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(client);

        Client foundClient = clientService.getClientById(1L);

        assertNotNull(foundClient);
        assertEquals("Test Client", foundClient.getNombre());
    }

    @Test
    public void testGetClientByIdNotFound() {
        when(clientRepository.findByIdAndEstado(1L, Constants.ACTIVE_STATUS)).thenReturn(null);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(1L);
        });

        assertEquals(ResourseApplication.properties.getProperty("client.not.found"), exception.getMessage());
    }

    // Agrega más pruebas unitarias según sea necesario para otros métodos en ClientServiceImpl
}
