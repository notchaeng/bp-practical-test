package com.nttdata.clients.controller;

import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nttdata.clients.domain.entity.Client;
import com.nttdata.clients.repository.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static String clientId;

    @BeforeAll
    public static void setUp(@Autowired ClientRepository clientRepository) {
        Client client = new Client();
        client.setNombre("John Doe");
        client.setDireccion("123 Main St");
        client.setTelefono("555-1234");
        client.setIdentificacion("123456");
        client.setEstado(1);
        clientId = clientRepository.save(client).getIdentificacion();
    }

    @Test
    public void testGetAllClients() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetClientById() throws Exception {
        mockMvc.perform(get("/clientes/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("John Doe"))
                .andExpect(jsonPath("$.direccion").value("123 Main St"))
                .andExpect(jsonPath("$.telefono").value("555-1234"));
    }

    @Test
    public void testCreateClient() throws Exception {
        String newClientJson = "{ \"nombre\": \"Jane Doe\", \"direccion\": \"456 Elm St\", \"telefono\": \"555-5678\" }";

        mockMvc.perform(post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Jane Doe"))
                .andExpect(jsonPath("$.direccion").value("456 Elm St"))
                .andExpect(jsonPath("$.telefono").value("555-5678"));
    }

    @Test
    public void testUpdateClient() throws Exception {
        String updatedClientJson = "{ \"nombre\": \"John Smith\", \"direccion\": \"789 Oak St\", \"telefono\": \"555-9876\" }";

        mockMvc.perform(put("/clientes/" + clientId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedClientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("John Smith"))
                .andExpect(jsonPath("$.direccion").value("789 Oak St"))
                .andExpect(jsonPath("$.telefono").value("555-9876"));
    }

    @Test
    public void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/clientes/" + clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("John Smith"))
                .andExpect(jsonPath("$.direccion").value("789 Oak St"))
                .andExpect(jsonPath("$.telefono").value("555-9876"));
    }
}
