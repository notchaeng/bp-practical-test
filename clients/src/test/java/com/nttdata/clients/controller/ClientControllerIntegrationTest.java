package com.nttdata.clients.controller;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nttdata.clients.entity.Client;
import com.nttdata.clients.repository.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    public void setUp() {
        // clientRepository.deleteAll();
        Client client = new Client();
        client.setNombre("John Doe");
        client.setDireccion("123 Main St");
        client.setTelefono("555-1234");
        clientRepository.save(client);
    }

    @Test
    public void testGetAllClients() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data", hasSize(1)));
    }

    @Test
    public void testCreateClient() throws Exception {
        Client newClient = new Client();
        newClient.setNombre("Jane Doe");
        newClient.setDireccion("456 Elm St");
        newClient.setTelefono("555-5678");

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"nombre\": \"Jane Doe\", \"direccion\": \"456 Elm St\", \"telefono\": \"555-5678\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.nombre").value("Jane Doe"));
    }
}
