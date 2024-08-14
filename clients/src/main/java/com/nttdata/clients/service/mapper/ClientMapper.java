package com.nttdata.clients.service.mapper;

import org.mapstruct.Mapper;

import com.nttdata.clients.domain.dto.ClientDTO;
import com.nttdata.clients.domain.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toClient(ClientDTO clientDto);

    ClientDTO toClientDTO (Client client);

}
