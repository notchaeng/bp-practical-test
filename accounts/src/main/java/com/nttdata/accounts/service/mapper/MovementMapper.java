package com.nttdata.accounts.service.mapper;

import org.mapstruct.Mapper;

import com.nttdata.accounts.domain.dto.MovementDTO;
import com.nttdata.accounts.domain.entity.Movement;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    Movement toMovement(MovementDTO movementDTO);

    MovementDTO toMovementDTO(Movement movement);
}
