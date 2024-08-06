package com.nttdata.clients.domain.dto;

import lombok.Data;

@Data
public class ClientDTO {

    private Long id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String password;
    private Integer estado;
}
